package com.kyadevs.stepdroid

import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import android.content.Intent

class MainScreenActivity : FullScreenActivity() {
    private lateinit var startButton: ImageView
    private lateinit var settingsButton: ImageView
    private lateinit var bgLoop: VideoView
    private lateinit var tapToStart: TextView
    private lateinit var tvStart: TextView
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var fadeOut: Animation
    private lateinit var fadeIn: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        setupViews()
        setupAnimations()
        setupListeners()

        mediaPlayer = MediaPlayer.create(this, R.raw.title_loop)
    }
    private fun setupListeners() {
        val startClickListener = View.OnClickListener {
            startButton.apply {
                visibility = View.VISIBLE
                startAnimation(fadeIn)
            }
        }

        bgLoop.setOnClickListener(startClickListener)
        startButton.setOnClickListener(startClickListener)
        tvStart.setOnClickListener(startClickListener)
        tapToStart.setOnClickListener(startClickListener)

        settingsButton.setOnClickListener { showEditFragment() }
    }
    override fun onStart() {
        super.onStart()
        playBackgroundVideo()
        startButton.startAnimation(fadeOut)
        tapToStart.animation = AnimationUtils.loadAnimation(this, R.anim.zoom_splash)

        mediaPlayer?.apply {
            isLooping = true
            start()
        }
    }

    private fun setupViews() {
        val customFont = Typeface.createFromAsset(assets, "fonts/font.ttf")

        findViewById<ConstraintLayout>(R.id.constraintlogo).setOnClickListener { showStartButton() }
        bgLoop = findViewById<VideoView>(R.id.bg_loop).apply {
            setOnPreparedListener { mp ->
                mp.isLooping = true
                mp.setVolume(0f, 0f)
            }
            setOnErrorListener { _, _, _ -> true }
            setOnClickListener { showStartButton() }
        }

        startButton = findViewById<ImageView>(R.id.startGameButton).apply {
            setOnClickListener { showStartButton() }
        }

        tapToStart = findViewById<TextView>(R.id.taptostart).apply {
            typeface = customFont
            setOnClickListener { showStartButton() }
        }

        tvStart = findViewById<TextView>(R.id.tv___start).apply {
            setOnClickListener { showStartButton() }
        }

        settingsButton = findViewById<ImageView>(R.id.imageViewSetting).apply {
            setOnClickListener { showEditFragment() }
        }
    }

    private fun setupAnimations() {
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in).apply {
            duration = 250
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    showLoadingAnimation()
                    releaseMediaPlayer()
                    startGame()
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }

        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_start).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    startButton.isVisible = false
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    private fun showStartButton() {
        startButton.apply {
            isVisible = true
            animation = fadeIn
        }
    }

    private fun playBackgroundVideo() {
        val uriVideoBg = Uri.parse("android.resource://${packageName}/${R.raw.bgmain2}")
        bgLoop.setVideoURI(uriVideoBg)
    }

    private fun showEditFragment() {
        mediaPlayer?.pause()

        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
    }

    private fun startGame() {
        val gameIntent = Intent(this, SongList::class.java)
        startActivity(gameIntent)
        finish()
    }

    private fun showLoadingAnimation() {
        val loadingFragment = FragmenStartMenu()
        FragmenStartMenu.loadingScreen = true
        loadingFragment.show(fragmentManager, "")
    }

    override fun onPause() {
        super.onPause()
        releaseMediaPlayer()
    }

    override fun onPostResume() {
        super.onPostResume()
        bgLoop.start()
    }

    private fun releaseMediaPlayer() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
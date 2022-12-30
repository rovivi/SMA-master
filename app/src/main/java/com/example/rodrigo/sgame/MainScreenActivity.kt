package com.example.rodrigo.sgame

import android.widget.VideoView
import android.widget.TextView
import android.media.MediaPlayer
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.graphics.Typeface
import android.media.MediaPlayer.OnPreparedListener
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import java.lang.Exception

class MainScreenActivity : FullScreenActivity() {
    lateinit var startButton: ImageView
    lateinit var settingsButton: ImageView
    lateinit var bgLoop: VideoView
    lateinit var tapToStart: TextView
    lateinit var tvStart: TextView
    var mp: MediaPlayer? = MediaPlayer()
    private lateinit var fadeOut: Animation
    private lateinit var fadeIn: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintlogo)
        bgLoop = findViewById(R.id.bg_loop)
        startButton = findViewById(R.id.startGameButton)
        tapToStart = findViewById(R.id.taptostart)
        settingsButton = findViewById(R.id.imageViewSetting)
        tvStart = findViewById(R.id.tv___start)
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_start)
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeIn.duration = 250
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                showLoadingAnimation()
                releaseMediaPlayer()
                startGame()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                startButton.visibility = View.INVISIBLE
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        val customFont = Typeface.createFromAsset(assets, "fonts/font.ttf")
        tapToStart.typeface = customFont
        bgLoop.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer ->
            mp.isLooping = true
            mp.setVolume(0f, 0f)
        })
        bgLoop.setOnErrorListener { _: MediaPlayer?, _: Int, _: Int -> true }
        val listenerStart = View.OnClickListener { v: View? ->
            startButton.visibility = View.VISIBLE
            startButton.animation = fadeIn
        }
        bgLoop.setOnClickListener(listenerStart)
        startButton.setOnClickListener(listenerStart)
        tvStart.setOnClickListener(listenerStart)
        tapToStart.setOnClickListener(listenerStart)
        constraintLayout.setOnClickListener(listenerStart)
        //startButton.setOnClickListener { }
        settingsButton.setOnClickListener { showEditFragment() }
        mp = MediaPlayer.create(this, R.raw.title_loop)
    }

    override fun onStart() {
        super.onStart()
        val uriVideoBg = Uri.parse("android.resource://" + packageName + "/" + R.raw.bgmain2)
        bgLoop.setVideoURI(uriVideoBg)


        //set animation
        //new AnimationUtils();
        val controller = AnimationUtils.loadAnimation(this, R.anim.zoom_splash)
        startButton.startAnimation(fadeOut)
        tapToStart.animation = controller
        if (mp != null) {
            mp!!.isLooping = true
            mp!!.start()
        }
    }

    private fun showEditFragment() {
        if (mp != null) {
            mp!!.pause()
        }
        val i = Intent(this, SettingsActivity::class.java)
        startActivity(i)
    }

    override fun onPause() {
        super.onPause()
        releaseMediaPlayer()
    }

    override fun onPostResume() {
        super.onPostResume()
        bgLoop.start()
    }

    private fun startGame() {
        val i = Intent(this, SongList::class.java)
        startActivity(i)
        finish()
    }

    private fun releaseMediaPlayer() {
        try {
            if (mp != null) {
                if (mp!!.isPlaying) mp!!.stop()
                mp!!.release()
                mp = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showLoadingAnimation() {
        val newFragment = FragmenStartMenu() //newFragment.setSongList(this);
        FragmenStartMenu.loadingScreen = true
        newFragment.show(fragmentManager, "")
    }


}
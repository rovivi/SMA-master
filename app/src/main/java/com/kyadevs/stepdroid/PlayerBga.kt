package com.kyadevs.stepdroid

import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Point
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import com.kyadevs.stepdroid.CommonGame.Common
import com.kyadevs.stepdroid.CommonGame.ParamsSong
import com.kyadevs.stepdroid.CommonGame.SSC
import com.kyadevs.stepdroid.Player.GamePlay
import com.kyadevs.stepdroid.Player.MainThread
import parsers.FileSSC
import java.io.FileInputStream

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;
class PlayerBga : FullScreenActivity() {
    lateinit var  gpo: GamePlay
    lateinit var bg: VideoView
    lateinit var bgPad: ImageView
    var tvMsj: TextView? = null
    var hilo: MainThread? = null
    var gl: Guideline? = null
    var i: Intent? = null
    var audio: AudioManager? = null
    var gamePlayError = false
    var handler = Handler()
    var nchar = 0
    var indexMsj = 0
    var pad = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private val msjs = arrayOf("", "Ready", "GO!")
    var textAnimator: Runnable = object : Runnable {
        override fun run() {
            if (indexMsj < msjs.size) {
                tvMsj!!.text = msjs[indexMsj]
                tvMsj!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        baseContext, R.anim.zoom_in
                    )
                )
                handler.postDelayed(this, 500)
                indexMsj++
            } else {
                tvMsj!!.text = ""
                Common.AnimateFactor = 0
                bgPad!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        baseContext, R.anim.fade_in
                    )
                )
                tvMsj!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        baseContext, android.R.anim.slide_out_right
                    )
                )
                startGamePlay()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i = Intent(this, EvaluationActivity::class.java)
        setContentView(R.layout.activity_playerbga)
        tvMsj = findViewById(R.id.gamemsg)
        audio = getSystemService(AUDIO_SERVICE) as AudioManager
        //evaluationIntet = new Intent(this, EvaluationActivity.class);
        bg = findViewById(R.id.vvBGA)
        gl = findViewById(R.id.guideline)
        nchar = intent.extras!!.getInt("nchar")
        gpo = findViewById(R.id.gamePlay)
        bgPad = findViewById(R.id.bg_pad)
        hilo = gpo.mainTread
        val pathImg = intent.extras!!.getString("pathDisc", null)
        if (pathImg != null) {
            bgPad.setImageBitmap(BitmapFactory.decodeFile(pathImg))
        }
        bg.setOnPreparedListener(OnPreparedListener { mp ->
            mp.isLooping = true
            mp.setVolume(0f, 0f)
        })
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent): Boolean {
        return super.dispatchGenericMotionEvent(ev)
    }

    public override fun onPause() {
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
    }

    fun setVideoPath(uri: String?) {
        if (uri != null) {
            bg!!.setVideoPath(uri)
        }
    }

    fun startVideo() {
        bg!!.setOnPreparedListener { mediaPlayer: MediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.setVolume(0f, 0f)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mediaPlayer.playbackParams =
                    mediaPlayer.playbackParams.setSpeed(ParamsSong.rush) // Esto será para el rush
            }
        }
        bg!!.start()
    }

    fun startEvaluation(params: IntArray?) {
        i!!.putExtra("evaluation", params)
        i!!.putExtra("pathbg", "")
        i!!.putExtra("name", "Noame")
        i!!.putExtra("nchar", nchar)
        startActivity(i)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onStart() {
        super.onStart()
        if (Common.ANIM_AT_START) {
            textAnimator.run()
        } else {
            startGamePlay()
        }
    }

    fun getresolution(): Point {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return Point(width, height)
    }

    private fun startGamePlay() {
        try {
            gpo!!.top = 0
            val rawscc = intent.extras!!.getString("ssc")
            val path = intent.extras!!.getString("path")
            val s = Common.convertStreamToString(FileInputStream(rawscc))
            try {
                val step = FileSSC(s, nchar).parseData()
                step.path = path!!
                gpo!!.build1Object(
                    baseContext,
                    SSC(s, false),
                    nchar,
                    path,
                    this,
                    pad,
                    Common.WIDTH,
                    Common.HEIGHT
                )
                //             gpo.build1Object(bg,step);
            } catch (e: Exception) {
                e.printStackTrace()
                gamePlayError = true
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val params = gl!!.layoutParams as ConstraintLayout.LayoutParams
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.guidePercent = 1f
            gl!!.layoutParams = params
        } else {
            params.guidePercent = 0.65f
            gl!!.layoutParams = params
        }
        bg!!.setOnErrorListener { mp: MediaPlayer?, what: Int, extra: Int ->
            val path2 = "android.resource://" + packageName + "/" + R.raw.bgaoff
            bg!!.setVideoPath(path2)
            bg!!.start()
            true
        }
        if (!gamePlayError && gpo != null) {
            gpo!!.startGame()
        } else {
            finish()
        }
    }
}
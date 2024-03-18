package com.kyadevs.stepdroid

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import az.plainpie.PieView
import az.plainpie.animation.PieAngleAnimation
import com.kyadevs.stepdroid.CommonGame.Common
import com.kyadevs.stepdroid.CommonGame.ParamsSong
import com.kyadevs.stepdroid.CommonGame.TransformBitmap
import com.kyadevs.stepdroid.Player.NoteSkin
import com.kyadevs.stepdroid.databinding.ActivityEvaluationBinding
import com.squareup.picasso.Picasso
import java.io.File
import kotlin.math.roundToInt

@Suppress("UNCHECKED_CAST")
class EvaluationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEvaluationBinding


    var textView = arrayOfNulls<TextView>(12)
    var percent = 0f
    var handler = Handler()
    var indexLabel = 0
    var soundPool: SoundPool? = null

    //Animation animation;
    var animation2: Animation? = null
    var animationDown: Animation? = null
    var animationZoom: Animation? = null
    var letter = "f"
    var letterGradeBitmap: Bitmap? = null
    lateinit var lettersArray: Array<Bitmap>
    var spScore = 0
    var animateInfo: Runnable = object : Runnable {
        override fun run() {
            if (indexLabel < 6) {
                textView[indexLabel]!!.visibility = View.VISIBLE
                textView[(indexLabel + 6)%textView.size ]!!.visibility = View.VISIBLE
                textView[indexLabel]!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        baseContext, R.anim.bounce
                    )
                )
                textView[(indexLabel + 6)%textView.size ]!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        baseContext, android.R.anim.slide_in_left
                    )
                )
                indexLabel++
                soundPool!!.play(spScore, 0.1f, 0.9f, 0, 0, 1.1f)
                soundPool!!.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f)
                soundPool!!.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f)
                soundPool!!.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f)
                soundPool!!.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f)
                soundPool!!.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f)
                soundPool!!.play(spScore, 0.9f, 0.9f, 0, 0, 1.2f)
                handler.postDelayed(this, 350)
            } else {
                soundPool!!.stop(spScore)
                showGrade()

            }
        }
    }
    private var animationZoom2: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //publicidad
        //  animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        initUI()
    }


    private fun initUI() {
        with(binding) {

            animation2 = AnimationUtils.loadAnimation(this@EvaluationActivity, android.R.anim.slide_in_left)
            animationDown = AnimationUtils.loadAnimation(this@EvaluationActivity, R.anim.translate_down)
            animationZoom = AnimationUtils.loadAnimation(this@EvaluationActivity, R.anim.zoom_in).apply {
                duration = 500
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        animateInfo.run()
                    }
                    override fun onAnimationRepeat(animation: Animation) {}
                })
            }
            animationZoom2 = AnimationUtils.loadAnimation(this@EvaluationActivity, R.anim.zoom_in).apply {
                duration = 600
            }

            //val skins: ArrayList<String> = NoteSkin.arraySkin(this@EvaluationActivity) as ArrayList<String>
            this@EvaluationActivity.supportActionBar!!.hide()
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            val params = intent.getIntArrayExtra("evaluation")


            textView = arrayOf(tvPerfect1,tvGood,tvMiss,tvCombo,tvPerfect2,tvGreat2,tvGood2,tvBad2,tvMiss2,tvMaxCombo)


            //
            letterGradeBitmap = BitmapFactory.decodeResource(resources, R.drawable.letters)
            lettersArray =
                TransformBitmap.customSpriteArray(letterGradeBitmap, 1, 8, 0, 1, 2, 3, 4, 5, 6, 7)
            val bgImage = File(intent.extras!!.getString("pathbg"))
            if (bgImage.exists() && bgImage.isFile) {
                var ww = BitmapFactory.decodeFile(bgImage.path)
                if (ww != null ) {
                    ww = TransformBitmap.makeTransparent(
                        TransformBitmap.myblur(ww, applicationContext),
                        130
                    )
                }
                bgBlurEvaluation.setImageBitmap(ww)
            }
            titleEvaluation.text = (intent.extras!!.getString("name"))
            val custom_font = Typeface.createFromAsset(assets, "fonts/font.ttf")
            titleEvaluation.setTypeface(custom_font)
            textTp.setTypeface(custom_font)
            val custom_font2 = Typeface.createFromAsset(assets, "fonts/font2.ttf")
            textLvlEcal.setTypeface(custom_font2)
            //animatedPie.setMainBackgroundColor(Color.BLUE);
            var index = 0
            for (tv in textView) {
                tv!!.setTypeface(custom_font)
                tv.textSize = 25f
                tv.visibility = View.GONE
                if (index > 5) {
                    tv.text = params!![index - 6].toString() + ""
                }
                index++
            }
            soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 100)
            spScore = soundPool!!.load(this@EvaluationActivity, R.raw.tick, 0)
            val total = (params!![0] + params[1] + params[2] + params[3] + params[4]).toFloat()
            percent =
                params[0].toFloat() / total + params[1].toFloat() / total * 0.8f + params[2].toFloat() / total * 0.6f + params[3].toFloat() / total * 0.4f
            pieView.percentage = percent * 100
            pieView.setPercentageTextSize(16f)
            pieView.setInnerText(
                ((100 * percent * 100000.0).roundToInt().toDouble() / 100000.0).toString() + "%"
            )
            pieView.pieInnerPadding = 10
            //listener
            val secretName = intent.extras!!
                .getString("name") + intent.extras!!.getInt("nchar")
            if (Common.compareRecords(this@EvaluationActivity, secretName, percent * 100)) {
                Common.writeRecord(this@EvaluationActivity, secretName, percent * 100)
            }

            buttonContinue.setTypeface(custom_font)
            buttonContinue.setOnClickListener(View.OnClickListener { finish() })
            if (percent == 1f) {
                letter = "SS"
            } else if (params[2] == 0 && params[3] == 0 && params[4] == 0) {
                letter = "S"
            } else if (params[4] == 0) {
                letter = "s"
            } else if (percent >= 0.9f) {
                letter = "a"
            } else if (percent >= 0.8f) {
                letter = "b"
            } else if (percent >= 0.7f) {
                letter = "c"
            } else if (percent >= 0.6f) {
                letter = "d"
            }
            if (ParamsSong.stepType2Evaluation.contains("double")) {
                Picasso.get().load(R.drawable.hexa_double).into(imageLevel)
            } else if (ParamsSong.stepType2Evaluation.contains("single")) {
                Picasso.get().load(R.drawable.hexa_single).into(imageLevel)
            } else {
                Picasso.get().load(R.drawable.hexa_performance).into(imageLevel)
            }
            /*skinImage.setImageBitmap(
                NoteSkin.maskImage(
                    skins[ParamsSong.skinIndex],
                    this@EvaluationActivity
                )
            )*/
            setTxtJudge()
            textLvlEcal.text = ParamsSong.stepLevel

        }
    }

    override fun onStart() {
        super.onStart()
        with(binding) {
            danceGradeText.startAnimation(animationZoom2)
            buttonContinue.startAnimation(animationZoom2)
            lvlJudmentEval.startAnimation(animationZoom2)
            imageLevel.startAnimation(animationZoom2)
            titleEvaluation.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.fade_in))
            pieView.startAnimation(
                AnimationUtils.loadAnimation(
                    baseContext,
                    android.R.anim.slide_in_left
                )
            )
            layout1.startAnimation(animationZoom)
            val animation3 = PieAngleAnimation(pieView)
            animation3.duration = 2000 //This is the duration of the animation in millis
            pieView.startAnimation(animation3)
        }
        //handler.postDelayed(animateInfo,10);
    }

    fun showGrade() {
        val mediaPlayer: MediaPlayer
        val mediaPlayerbg: MediaPlayer
        var res = -1
        var res2 = -1
        with(binding) {
            when (letter) {
                "SS" -> {
                    res = R.raw.rank_0
                    res2 = R.raw.rank_0_b
                    letterGrade.setImageBitmap(lettersArray[0])
                }

                "S" -> {
                    res = R.raw.rank_1
                    res2 = R.raw.rank_1_b
                    letterGrade.setImageBitmap(lettersArray[1])
                }

                "s" -> {
                    res = R.raw.rank_2
                    res2 = R.raw.rank_2_b
                    letterGrade.setImageBitmap(lettersArray[2])
                }

                "a" -> {
                    res = R.raw.rank_3
                    res2 = R.raw.rank_3_b
                    letterGrade.setImageBitmap(lettersArray[3])
                }

                "b" -> {
                    res = R.raw.rank_4
                    res2 = R.raw.rank_4_b
                    letterGrade.setImageBitmap(lettersArray[4])
                }

                "c" -> {
                    res = R.raw.rank_5
                    res2 = R.raw.rank_5_b
                    letterGrade.setImageBitmap(lettersArray[5])
                }

                "d" -> {
                    res = R.raw.rank_6
                    res2 = R.raw.rank_6_b
                    letterGrade.setImageBitmap(lettersArray[6])
                }

                "f" -> {
                    res = R.raw.rank_7
                    res2 = R.raw.rank_7_b
                    letterGrade.setImageBitmap(lettersArray[7])
                }
            }
            letterGrade.startAnimation(
                AnimationUtils.loadAnimation(
                    baseContext,
                    R.anim.zoom_letter
                )
            )
        }
        mediaPlayer = MediaPlayer.create(this, res)
        mediaPlayerbg = MediaPlayer.create(this, res2)
        mediaPlayer.start()
        mediaPlayerbg.start()
    }

    private fun setTxtJudge() {
        var text = ""
        when (ParamsSong.judgment) {
            0 -> text = "SJ"
            1 -> text = "EJ"
            2 -> text = "NJ"
            3 -> text = "HJ"
            4 -> text = "VJ"
            5 -> text = "XJ"
            6 -> text = "UJ"
        }
        binding.lvlJudmentEval.text = text
    }
}

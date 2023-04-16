package com.kyadevs.stepdroid.PlayerNew

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.VideoView
import com.kyadevs.stepdroid.CommonGame.Common
import com.kyadevs.stepdroid.R
import java.util.Locale
import kotlin.math.abs

class BgPlayer(
    private val path: String,
    data: String,
    private val player: VideoView,
    private val context: Context,
    private val BPM: Double
) {
    private var currentBg = 0
    private val BGList = mutableListOf<bgChange>()
    private var changeVideo = false
    var isRunning = false

    init {
        val listBG = data.replace("\n", "").replace("\r", "").split(",")

        for (bg in listBG) {
            BGList.add(bgChange(bg))
        }
    }

    fun update(beat: Double) {
        if (isRunning && BGList.size > currentBg) {
            val bg = BGList[currentBg]
            if (changeVideo && bg.beat <= beat) {
                try {
                    bg.fileName?.substring(bg.fileName!!.lastIndexOf("."))?.lowercase(Locale.getDefault())
                        .let { ext ->
                            when (ext) {
                                ".avi", ".mov", ".mp4", ".3gp", ".ogg", ".mpeg", ".mpg", ".flv" -> {
                                    Common.checkBGADir(path, bg.fileName, context)?.let { bgaDir ->
                                        player.background = null
                                        player.setVideoPath(bgaDir)
                                        if (bg.beat < 0) player.seekTo(
                                            abs(
                                                Common.beat2Second(
                                                    bg.beat.toDouble(),
                                                    BPM
                                                ) * 1000 + 100
                                            ).toInt()
                                        )
                                        player.start()
                                    } ?: playBgaOff()
                                }
                                ".png", ".webp", ".jpg", ".bpm", ".tiff" -> {
                                    Common.checkBGADir(path, bg.fileName, context)?.let { bgaDir ->
                                        player.background = BitmapDrawable(
                                            BitmapFactory.decodeFile(bgaDir)
                                        )
                                    } ?: playBgaOff()
                                }
                            }
                        }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    changeVideo = false
                }
            }
            if (beat > BGList[currentBg].beat) {
                currentBg++
                changeVideo = true
            }
        }
    }


    private fun playBgaOff() {
        val path2 = "android.resource://${context.packageName}/${R.raw.bgaoff}"
        player.setVideoPath(path2)
        player.start()
    }

    fun start(beat: Double) {
        if (BGList.isEmpty()) {
            playBgaOff()
        } else {
            isRunning = true
            changeVideo = true
            update(beat)
        }
    }

    inner class bgChange(data: String) {
        var beat = 0f
        private var prop1 = 0f
        var fileName: String? = null
        private var prop2: Byte = 0
        private var prop3: Byte = 0
        private var prop4: Byte = 0

        init {
            val info: Array<String?> = data.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (info.size > 1) {
                try {
                    beat = info[0]!!.toFloat()
                    fileName = info[1]
                    prop1 = info[2]!!.toFloat()
                    prop2 = info[3]!!.toByte()
                    prop3 = info[4]!!.toByte()
                    prop4 = info[5]!!.toByte()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else {
                beat = -1f
                if (info[0] != null && info[0] !== "") {
                    fileName = info[0]
                }
            }
        }
    }
}
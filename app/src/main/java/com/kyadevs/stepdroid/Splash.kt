package com.kyadevs.stepdroid

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDex

class Splash : FullScreenActivity() {
    var handler: Handler? = null
    private lateinit var splashImage: ImageView
    var anim: Animation? = null
    var startActivity = Runnable { startSongList() }
    var runAnimation = Runnable {
        splashImage.visibility = View.VISIBLE
        splashImage.animation = anim
        splashImage.animate().withEndAction { requestPermission() }.start()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    70
                )

            }
        } else
            handler!!.postDelayed(startActivity, 1500)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                    startActivity(intent)
                } catch (ex: Exception) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivity(intent)
                }
                // Do something for lollipop and above versions
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MultiDex.install(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashImage = findViewById(R.id.ivSpash)
        anim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        handler = Handler()
        splashImage.visibility = View.GONE

    }

    private fun startSongList() {
        splashImage.visibility = View.GONE
        val i = Intent(this, MainScreenActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onStart() {
        super.onStart()
        handler!!.postDelayed(runAnimation, 1000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            69 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    handler!!.postDelayed(startActivity, 1500)
                } else {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        //Can add more as per requirement
                        Toast.makeText(
                            baseContext,
                            "Read archive permission is needed",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            123
                        )
                    }
                }
                return
            }
            70 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    handler!!.postDelayed(startActivity, 1500)
                } else {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        //Can add more as per requirement
                        Toast.makeText(
                            baseContext,
                            "Read archive permission is needed",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            123
                        )
                    }
                }
                return
            }
        }
    }
}
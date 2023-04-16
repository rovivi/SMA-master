package com.kyadevs.stepdroid
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDex
import kotlinx.coroutines.*

class Splash : FullScreenActivity() {
    private val splashActivityJob = Job()
    private val splashActivityScope = CoroutineScope(Dispatchers.Main + splashActivityJob)
    private lateinit var splashImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        MultiDex.install(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashImage = findViewById<ImageView>(R.id.ivSpash).apply {
            visibility = View.GONE
        }

        splashActivityScope.launch {
            delay(1000)
            splashImage.apply {
                visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(this@Splash, R.anim.fade_in))
            }
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                70
            )
        } else {
            startSongListDelayed()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri).apply {
                action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            }
            startActivity(intent)
        }
    }

    private fun startSongListDelayed() {
        splashActivityScope.launch {
            delay(1500)
            startActivity(Intent(this@Splash, MainScreenActivity::class.java))
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 70 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startSongListDelayed()
        } else {
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

    override fun onDestroy() {
        super.onDestroy()
        splashActivityJob.cancel()
    }
}

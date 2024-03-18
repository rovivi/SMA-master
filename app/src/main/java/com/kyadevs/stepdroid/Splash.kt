
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.multidex.MultiDex
import kotlinx.coroutines.*

class Splash : FullScreenActivity() {
    private val splashActivityJob = Job()
    private val splashActivityScope = CoroutineScope(Dispatchers.Main + splashActivityJob)
    private lateinit var splashImage: ImageView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startSongListDelayed()
            } else {
                Toast.makeText(this, "Read archive permission is needed", Toast.LENGTH_LONG).show()
                startSongListDelayed()
            }
        }

    @RequiresApi(Build.VERSION_CODES.R)
    private val requestAllFilesAccessLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Environment.isExternalStorageManager()) {
                startSongListDelayed()
            } else {
                Toast.makeText(this, "All files access is needed", Toast.LENGTH_LONG).show()
                finish()
            }
        }

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
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                startSongListDelayed()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager() -> {
                val uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                requestAllFilesAccessLauncher.launch(intent)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun startSongListDelayed() {
        splashActivityScope.launch {
            delay(1500)
            startActivity(Intent(this@Splash, MainScreenActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        splashActivityJob.cancel()
    }
}

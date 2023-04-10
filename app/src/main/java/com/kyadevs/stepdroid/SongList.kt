package com.kyadevs.stepdroid

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import com.kyadevs.stepdroid.ScreenSelectMusic.MusicThread
import android.media.SoundPool
import com.kyadevs.stepdroid.ScreenSelectMusic.SongsGroup
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.recyclerview.widget.RecyclerView
import com.kyadevs.stepdroid.ScreenSelectMusic.AdapterLevel
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.graphics.Typeface
import com.kyadevs.stepdroid.Player.NoteSkin
import com.kyadevs.stepdroid.ScreenSelectMusic.RecyclerItemClickListener
import com.squareup.picasso.Picasso
import android.media.AudioManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.kyadevs.stepdroid.ScreenSelectMusic.AdapterSSC
import android.app.ActivityManager
import android.net.Uri
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.Guideline
import com.kyadevs.stepdroid.CommonGame.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.Exception
import java.util.*

class SongList : FullScreenActivity(), View.OnClickListener {
    private var files: Array<File>? = null
    var currentSSC: String? = null
    var paths: String? = null
    var path = ""
    var pathImage: String? = null
    var fadeOut: ObjectAnimator? = null
    var spCode = 0

    @JvmField
    var spOpenWindow = 0

    @JvmField
    var spSelect = 0

    @JvmField
    var spSelectSong = 0
    var mediaPlayer: MediaPlayer? = null
    var musicTimer: MusicThread? = null

    //VideoView bg;
    private var changeMusic: SoundPool? = null
    val songs: List<String> = ArrayList()
    private val lvl: MutableList<String> = ArrayList()
    var currentSongIndex = 0
    private var currentSSCIndex = 0

    //ThemeElements themeElements;
    var i: Intent? = null
    lateinit var preview: VideoView
    var backgroundBlur: ImageView? = null
    lateinit var btnLevel: ImageView
    lateinit var btnSpeed: ImageView
    private var bgSongList: ImageView? = null

    lateinit var imageSkin: ImageView
    lateinit var lvlText: TextView
    lateinit var titleCurrentSong: TextView
    var authorCurrent: TextView? = null
    private lateinit var txtOpen: TextView
    private var tvRecord: TextView? = null

    @JvmField
    public var tvVelocity: TextView? = null

    @JvmField
    var tvJudgement: TextView? = null
    private var tvLook: TextView? = null
    lateinit var tvBPM: TextView
    var levelArrayList = ArrayList<Level>()
    private var errorAuxImage: BitmapDrawable? = null
    private var songsGroup: SongsGroup? = SongsGroup()
    lateinit var recyclerView: RecyclerView
    var startImage: ImageView? = null
    private lateinit var backImage: ImageView
    lateinit var recyclerViewLevels: RecyclerView
    var currentDF: FragmenStartMenu? = null
    private var adapterLevel: AdapterLevel? = null

    //Sprites
    private var root: View? = null

    // Sprite flashSpeedSprite, sriteMask;
    //  ThreadSprite threadSprite;
    private var listenerButton = View.OnClickListener { showStartSongFragment(false) }
    var listenerlvl = View.OnClickListener { openLevelList() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Windows Decorator

        setContentView(R.layout.activity_songlist)

        //------Se llenan los valosres de las lineas guia----//
        root = findViewById(R.id.rootId_songList)
        var guideLine = findViewById<Guideline>(R.id.guideStartList)
        var params = guideLine.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = 0.40f // 45% // range: 0 <-> 1
        guideLine.layoutParams = params
        guideLine = findViewById(R.id.guideEndLIst)
        params = guideLine.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = 1.1f // 45% // range: 0 <-> 1
        guideLine.layoutParams = params


        //------Fin de las lineas guia-----//
        //------get Elemets----------------//
        btnLevel = findViewById(R.id.imagelevl)
        btnSpeed = findViewById(R.id.imagelevl2)
        lvlText = findViewById(R.id.numberlevel)
        backgroundBlur = findViewById(R.id.bgBlur)
        recyclerView = findViewById(R.id.recyclerSongs)
        preview = findViewById(R.id.preview)
        startImage = findViewById(R.id.startButton)
        titleCurrentSong = findViewById(R.id.current_song_name)
        authorCurrent = findViewById(R.id.current_text_author)
        txtOpen = findViewById(R.id.more_txt)
        tvRecord = findViewById(R.id.record_text)
        tvVelocity = findViewById(R.id.text_speed)
        tvJudgement = findViewById(R.id.text_judment)
        tvLook = findViewById(R.id.text_apararience)
        tvBPM = findViewById(R.id.text_bpm)
        backImage = findViewById(R.id.back_image)
        imageSkin = findViewById(R.id.image_note_preview)
        titleCurrentSong.setSelected(true)
        titleCurrentSong.setSingleLine(true)
        bgSongList = findViewById(R.id.bg_song_list)
        preview.setOnClickListener { showStartSongFragment(false) }
        //lista
        backImage.setOnClickListener {
            startActivity(
                Intent(
                    this@SongList,
                    MainScreenActivity::class.java
                )
            )
        }
        if (ParamsSong.listCuadricula) {
            recyclerView.layoutManager = GridLayoutManager(this, 3)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        recyclerView.isNestedScrollingEnabled = true
        val msgLvl = findViewById<TextView>(R.id.yourlvlmsjtv)
        val customFont = Typeface.createFromAsset(assets, "fonts/font.ttf")
        val customFont2 = Typeface.createFromAsset(assets, "fonts/font2.ttf")
        //Typeface custom_font3 = Typeface.createFromAsset(getAssets(), "fonts/font3.ttf");
        msgLvl.typeface = customFont2
        txtOpen.typeface = customFont
        tvBPM.typeface = customFont
        lvlText.typeface = customFont2
        preview.setOnErrorListener { _: MediaPlayer?, what: Int, extra: Int ->
            playVideoError()
            true
        }


        //---------Listeners-------//
        btnLevel.setOnClickListener(listenerlvl)
        btnLevel.setOnLongClickListener { false }
        imageSkin.setImageBitmap(NoteSkin.maskImage(ParamsSong.nameNoteSkin, this))
        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        changeSong(
                            position
                        )
                    }
                    override fun onItemLongClick(view: View, position: Int) {}
                })
        )
        txtOpen.setOnClickListener { showEditFragment() }
        Picasso.get().load(R.drawable.bg_disc_square).into(bgSongList)
        //-------------se crea la lista de canciones------------//
        try {
            changeMusic = SoundPool(10, AudioManager.STREAM_MUSIC, 0)
            spCode = changeMusic!!.load(this, R.raw.change_song, 0)
            spOpenWindow = changeMusic!!.load(this, R.raw.select_3, 0)
            spSelect = changeMusic!!.load(this, R.raw.command_mod, 0)
            spSelectSong = changeMusic!!.load(this, R.raw.transformation, 0)
            i = Intent(this, MainActivity::class.java)
            i = Intent(this, PlayerBga::class.java)
            mediaPlayer = MediaPlayer()
            preview.setOnPreparedListener { mediaPlayer: MediaPlayer ->
                mediaPlayer.isLooping = true
                mediaPlayer.setVolume(0f, 0f)
            }
            btnSpeed.setOnClickListener(View.OnClickListener { v: View? ->
                ParamsSong.speed += 0.5f
                if (ParamsSong.speed > 7) {
                    ParamsSong.speed = 1f
                }
                //                flashSpeedSprite.image.play();
                showStartSongFragment(false)
            })
        } catch (e: Exception) {
            Toast.makeText(baseContext, e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

        ///-----------
        adapterLevel = AdapterLevel(levelArrayList, this, assets)
        recyclerViewLevels = findViewById(R.id.rv_leves)
        recyclerViewLevels.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        //recyclerViewLevels.setLayoutManager(new GridLayoutManager(this, 7, GridLayoutManager.VERTICAL, false));
        recyclerViewLevels.setAdapter(adapterLevel)
        recyclerViewLevels.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        changeLvl(position)
                    }

                    override fun onItemLongClick(view: View, position: Int) {}
                })
        )
        try {
            //   loadSongs();
        } catch (e: Exception) {
            Toast.makeText(baseContext, e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        if (ParamsSong.av == 0) {
            //tv_velocity.set
        }
    }

    private fun setMetadata(
        stepData: SSC,
        dataCurrentChar: String?,
        dataGeneral: String?
    ): ArrayList<Array<Float>?>? {
        if (dataCurrentChar != null) {
            return stepData.arrayListSpeed(dataCurrentChar)
        } else if (dataGeneral != null && dataGeneral != "") {
            return stepData.arrayListSpeed(dataGeneral)
        }
        return null
    }

    fun playSoundPool(spCode: Int) {
        changeMusic!!.play(spCode, 0.5f, 0.5f, 1, 0, 1.1f)
    }

    fun changeSong(position: Int) {
        changeMusic!!.play(spCode, 1f, 1f, 1, 0, 1.0f)
        releaseMediaPlayer()
        currentSSCIndex = position
        paths = songsGroup!!.listOfSongs[position].path.path
        try {
            // themeElements.flash.play();
            val auxStep = songsGroup!!.listOfSongs[position]
            val sampleStart = auxStep.songInfo["SAMPLESTART"]!!.toFloat()
            val offset = (sampleStart * 1000).toInt()
            titleCurrentSong.text =
                if (auxStep.songInfo["TITLE"] != null) auxStep.songInfo["TITLE"] else "No title"
            val bpms = setMetadata(auxStep, null, auxStep.songInfo["BPMS"])
            val checkBPM = bpms != null && bpms[0] != null
            tvBPM.text = if (checkBPM) "B.P.M. " + bpms!![0]!![1].toInt() else "B.P.M. ???"
            tvBPM.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.zoom_in))
            authorCurrent!!.text =
                if (auxStep.songInfo["ARTIST"] != null) "Composed by:" + auxStep.songInfo["ARTIST"] else "No Artist"
            titleCurrentSong.startAnimation(
                AnimationUtils.loadAnimation(
                    baseContext, R.anim.zoom_in
                )
            )
            authorCurrent!!.startAnimation(
                AnimationUtils.loadAnimation(
                    baseContext, R.anim.fade_in
                )
            )
            currentSSC = songsGroup!!.listOfSongs[position].pathSSC
            lvl.clear()
            levelArrayList.clear()
            adapterLevel!!.notifyDataSetChanged()
            adapterLevel!!.lastPosition = -1
            for (currentChart in auxStep.chartsInfo) {
                if (currentChart == null) {
                    break
                }
                val lvl = if (currentChart["METER"] != null) currentChart["METER"].toString()
                    .toInt() else 0
                val tipo =
                    if (currentChart["STEPSTYPE"] != null) currentChart["STEPSTYPE"].toString() else ""
                val tag =
                    if (currentChart["STEPSTYPE"] != null) currentChart["STEPSTYPE"].toString() else ""
                levelArrayList.add(Level(lvl, tipo, tag))
            }

            //if the song provides from SD
            val audio = File(paths + "/" + auxStep.songInfo["MUSIC"])
            val video = File(paths + "/" + auxStep.songInfo["PREVIEWVID"])
            val bg = File(paths + "/" + auxStep.songInfo["BACKGROUND"])
            val transparent: Bitmap
            if (video.exists() && (video.path.endsWith(".mpg") || video.path.endsWith(".mp4") || video.path.endsWith(
                    ".avi"
                ))
            ) {
                preview.background = null
                // Uri uri = CustomAPEZProvider.buildUri(video.getPath().replace(obbMountPath + "/", ""));
                //preview.setVideoURI(uri);
                preview.setVideoPath(video.path)
                preview.start()
                transparent =
                    TransformBitmap.makeTransparent(BitmapFactory.decodeFile(bg.path), 180)
                errorAuxImage = BitmapDrawable(transparent)
            } else {
                playVideoError()
                if (bg.exists() && bg.isFile) {
                    transparent =
                        TransformBitmap.makeTransparent(BitmapFactory.decodeFile(bg.path), 180)
                    errorAuxImage = BitmapDrawable(transparent)
                } else {
                    transparent = TransformBitmap.makeTransparent(
                        BitmapFactory.decodeResource(
                            resources, R.drawable.no_banner
                        ), 180
                    )
                    errorAuxImage = BitmapDrawable(transparent)
                }
                preview.background = errorAuxImage
            }
            if (bg.exists() && bg.isFile) {
                val ww = TransformBitmap.makeTransparent(
                    TransformBitmap.myblur(
                        BitmapFactory.decodeFile(bg.path), applicationContext
                    ), 150
                )
                backgroundBlur!!.setImageBitmap(BitmapFactory.decodeFile(bg.path))
                pathImage = bg.path
                backgroundBlur!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        baseContext, android.R.anim.slide_in_left
                    )
                )
                // Picasso.get().load(bg.getPath()).into(backgroundBluour);
            }
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setVolume(1f, 1f)

            mediaPlayer!!.setDataSource(audio.path)

            mediaPlayer!!.prepare()
            mediaPlayer!!.seekTo(offset)
            musicTimer = MusicThread()
            musicTimer!!.player = mediaPlayer
            musicTimer!!.time = (auxStep.songInfo["SAMPLELENGTH"]!!.toDouble() * 1000).toLong()
            musicTimer!!.start()
            mediaPlayer!!.start()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setRecord()
        if (recyclerViewLevels.visibility == View.INVISIBLE) {
            changeLevel()
        }
    }

    override fun onStart() {
        super.onStart()
        startImage!!.setOnClickListener(listenerButton)
    }

    public override fun onResume() {
        super.onResume()
        Common.setParamsGlobal(this)
        if (fadeOut != null && fadeOut!!.isRunning) {
            fadeOut!!.cancel() // Cancel the opposite animation if it is running or else you get funky looks
        }
        if (songsGroup!!.listOfSongs.size == 0 || Common.RELOAD_SONGS) {
            loadSongs()
        } else {
            try {
                changeSong(currentSSCIndex)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (musicTimer != null) {
            musicTimer!!.isRunning = false
        }
        releaseMediaPlayer()
    }

    public override fun onPause() {
        super.onPause()
        try {
            releaseMediaPlayer()
        } catch (_: Exception) {
        }
    }

    private fun releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                preview.suspend()
                if (mediaPlayer!!.isPlaying) mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null
                if (musicTimer != null) {
                    if (musicTimer!!.isRunning) {
                        musicTimer!!.isRunning = false
                        musicTimer!!.join()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showEditFragment() {
        val newFragment = FragmenSongOptions()
        newFragment.setSongList(this)
        newFragment.show(fragmentManager, "")
        playSoundPool(spOpenWindow)
    }

    private fun showStartSongFragment(type: Boolean) {
        val newFragment = FragmenStartMenu()
        currentDF = newFragment
        newFragment.setSongList(this)
        FragmenStartMenu.loadingScreen = type
        newFragment.show(fragmentManager, "")
    }

    private fun changeLevel() {
        if (levelArrayList.size == 0) {
            return
        }
        if (currentSongIndex >= levelArrayList.size) {
            currentSongIndex = 0
        }
        ParamsSong.stepType2Evaluation = levelArrayList[currentSongIndex].gameType
        ParamsSong.stepLevel =
            if (levelArrayList[currentSongIndex].numberLevel < 99) levelArrayList[currentSongIndex].numberLevel.toString() + "" else "??"
        setRecord()
        animateLvl()
    }

    fun changeLvl(index: Int) {
        currentSongIndex = index
        changeMusic!!.play(spCode, 1f, 1f, 1, 0, 1.0f)
        btnLevel.setOnClickListener(listenerlvl)
        recyclerViewLevels.startAnimation(
            AnimationUtils.loadAnimation(
                baseContext, android.R.anim.slide_out_right
            )
        )
        btnLevel.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.zoom_in))
        lvlText.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.zoom_in))
        recyclerViewLevels.visibility = View.INVISIBLE
        ParamsSong.stepType2Evaluation = levelArrayList[currentSongIndex].gameType
        ParamsSong.stepLevel =
            if (levelArrayList[currentSongIndex].numberLevel < 99) levelArrayList[currentSongIndex].numberLevel.toString() + "" else "??"
        animateLvl()
        setRecord()
    }

    private fun openLevelList() {
        playSoundPool(spOpenWindow)
        btnLevel.setOnClickListener(null)
        recyclerViewLevels.visibility = View.VISIBLE
        btnLevel.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.fade_out))
        lvlText.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.fade_out))
        recyclerViewLevels.startAnimation(
            AnimationUtils.loadAnimation(
                baseContext, android.R.anim.slide_in_left
            )
        )
    }

    private fun animateLvl() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.blink)
        lvlText.text =
            if (levelArrayList[currentSongIndex].numberLevel > 95) "DP" else levelArrayList[currentSongIndex].numberLevel.toString() + ""
        anim.repeatCount = 0
        btnLevel.startAnimation(anim)
        if (levelArrayList[currentSongIndex].gameType.endsWith("double")) {
            Picasso.get().load(R.drawable.hexa_double).into(btnLevel)

            //  lvlText.setTextColor(Color.rgb(188, 244, 66));
        } else if (levelArrayList[currentSongIndex].gameType.endsWith("single")) {
            //  lvlText.setTextColor(Color.rgb(239, 140, 33));
            Picasso.get().load(R.drawable.hexa_single).into(btnLevel)
        } else if (levelArrayList[currentSongIndex].gameType.endsWith("routine")) {
            //  lvlText.setTextColor(Color.rgb(92, 66, 244))
            // ;
            Picasso.get().load(R.drawable.hexa_performance).into(btnLevel)
        }
    }

    private fun loadSongs() {
        songsGroup = SongsGroup.readIt(baseContext) //busca su propio archivo
        if (songsGroup == null || Common.RELOAD_SONGS) { //Si no existe o si se esta recargando
            reloadSongs()
            val adapterSSC = AdapterSSC(songsGroup, currentSongIndex)
            recyclerView.adapter = adapterSSC
            //  themeElements.biuldObject(this, songsGroup);
        } else {
            val adapterSSC = AdapterSSC(songsGroup, currentSongIndex)
            recyclerView.adapter = adapterSSC
            concurrentSort(songsGroup!!.listOfSongs, songsGroup!!.listOfSongs)
            changeSong(0)
        }
        changeLevel()
    }

    private fun hide() {
        backgroundBlur!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    override fun onClick(v: View) {
        hide()
    }


    private fun playVideoError() {
        val uriVideoBgError =
            Uri.parse("android.resource://" + packageName + "/" + R.raw.bg_no_banner)
        preview.setVideoURI(uriVideoBgError)
        preview.start()
    }

    fun startSong() {
        // root.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out));
        releaseMediaPlayer()
        preview.suspend()
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val availableMegs = (mi.availMem / 0x100000L).toDouble()
        val percentAvail = mi.availMem / mi.totalMem.toDouble() * 100.0
        if (availableMegs < 100) {
            // System.gc();
        }
        i!!.putExtra("ssc", currentSSC)
        i!!.putExtra("nchar", currentSongIndex)
        i!!.putExtra("path", paths)
        i!!.putExtra("pathDisc", pathImage)
        startActivity(i)
        //finish();
        startImage!!.setOnClickListener(null)
    }

    private fun setRecord() {
        val secretName: String = titleCurrentSong!!.text as String + currentSongIndex
        if (Common.getRecords(this, secretName).contains("N")) {
            tvRecord!!.text = "Non Played"
        } else {
            tvRecord!!.text = Common.getRecords(this, secretName)
        }
    }

    private fun reloadSongs() {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        songsGroup = SongsGroup()
        val aux = Common.checkDirSongsFolders(this)
        if (aux == null) { //implement Chose folder stuff
        } else {
            files = File(aux.path + "/songs").listFiles()
            if (files == null) {
                Toast.makeText(
                    baseContext,
                    "There aren't files in" + aux.path + "/songs please choose a folder or put song data",
                    Toast.LENGTH_LONG
                ).show()
            }
            if ((files as Array<out File>?)?.isEmpty()!!) {
                Toast.makeText(
                    baseContext,
                    "There aren't files in" + aux.path + "/songs please choose a folder or put song data",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //implements add From assets Folder
                songsGroup!!.addList(File(aux.path + "/songs"))
            }
        }
        if (songsGroup!!.listOfSongs.size < 1) {
            startActivity(Intent(this@SongList, MainScreenActivity::class.java))
            finish()
            Toast.makeText(
                baseContext,
                "No song found please verify  ¯\\_(⊙_ʖ⊙)_/¯",
                Toast.LENGTH_LONG
            ).show()
        } else { //all things are ok
            concurrentSort(songsGroup!!.listOfSongs, songsGroup!!.listOfSongs)
            try {
                songsGroup!!.saveIt(this)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val prefsEditor = mPrefs.edit()
            prefsEditor.putBoolean("reload_songs", false)
            prefsEditor.apply()
            changeSong(0)
        }
    }

    companion object {
        fun concurrentSort(
            key: List<SSC>, vararg lists: List<*>
        ) {
            // Create a List of indices
            val indices: MutableList<Int> = ArrayList()
            for (i in key.indices) indices.add(i)
            // Sort the indices list based on the key
            indices.sortWith { i, j ->
                key[i!!].songInfo["TITLE"]!!
                    .compareTo(key[j!!].songInfo["TITLE"]!!, ignoreCase = true)
            }
            // Create a mapping that allows sorting of the List by N swaps.
            // Only swaps can be used since we do not know the type of the lists
            val swapMap: MutableMap<Int, Int> = HashMap(indices.size)
            val swapFrom: MutableList<Int> = ArrayList(indices.size)
            val swapTo: MutableList<Int> = ArrayList(indices.size)
            for (i in key.indices) {
                var k = indices[i]
                while (i != k && swapMap.containsKey(k)) k = swapMap[k]!!
                swapFrom.add(i)
                swapTo.add(k)
                swapMap[i] = k
            }
            // use the swap order to sort each list by swapping elements
            for (list in lists) for (i in list.indices) Collections.swap(
                list,
                swapFrom[i],
                swapTo[i]
            )
        }
    }
}
package com.example.tictactoe

import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var _trackPlayer: MediaPlayer? = null
    private val trackPlayer: MediaPlayer get() =  _trackPlayer!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _trackPlayer = MediaPlayer.create(applicationContext, R.raw.track)
    }

    override fun onStart() {
        super.onStart()
        trackPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        trackPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        trackPlayer.release()
        _trackPlayer = null
    }
}
package com.masaibar.rxjavasample

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class AsyncSampleActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AsyncSampleActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync_sample)
    }
}

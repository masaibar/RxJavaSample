package com.masaibar.rxjavasample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sync_sample.*

class SyncSampleActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "SyncSampleActivity"

        fun start(context: Context) {
            val intent = Intent(context, SyncSampleActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync_sample)

        button_save.setOnClickListener {
            onSaveClick()
        }
    }

    private fun onSaveClick() {
        saveSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { text_saved.text = it.message },
                        onSuccess = {
                            Log.d(TAG, "result = $it")
                            if (it) {
                                text_saved.text = getPref().getString(TAG, "null")
                            }
                        }
                )
    }

    private fun saveSingle(): Single<Boolean> {
        val text = edit_text.text.toString()

        if (text.isBlank() || text.isEmpty()) {
            return Single.fromCallable { false }
        }

        return Single.fromCallable { getPref().edit().putString(TAG, text).commit() }
    }

    private fun getPref(): SharedPreferences {
        return getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}

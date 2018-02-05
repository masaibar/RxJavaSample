package com.masaibar.rxjavasample

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_async_sample.*
import java.util.*

class AsyncSampleActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "AsyncSampleActivity"

        fun start(context: Context) {
            val intent = Intent(context, AsyncSampleActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    private val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    private val accelerometer: Sensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_sample)
    }

    override fun onResume() {
        super.onResume()
        disposable = naiveObserveSensor(sensorManager, accelerometer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { text_accelerometer.text = it.message },
                        onComplete = { Log.d(TAG, "onComplete called.") },
                        onNext = {
                            val result = Arrays.toString(it.values)
                            text_accelerometer.text = result
                        }
                )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun naiveObserveSensor(
            sensorManager: SensorManager, sensor: Sensor): Observable<SensorEvent> {

        return Observable.create { emitter ->
            val sensorEventListener = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    //ignore
                }

                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        emitter.onNext(it)
                    }
                }
            }

            emitter.setCancellable {
                Log.d(TAG, "dispose() called.")
                sensorManager.unregisterListener(sensorEventListener)
            }

            sensorManager.registerListener(
                    sensorEventListener,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
            )
        }

    }
}

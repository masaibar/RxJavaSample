package com.masaibar.rxjavasample

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    private val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            Log.d(
                    TAG,
                    "onAccuracyChanged() - sensor: ${sensor?.name}"
            )
        }

        override fun onSensorChanged(event: SensorEvent?) {
            Log.d(
                    TAG,
                    "onSensorChanged() - time: ${event?.timestamp}," +
                            " values: ${Arrays.toString(event?.values)}"
            )
            text_accelerometer.text = Arrays.toString(event?.values)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_sample)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
                sensorEventListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }
}

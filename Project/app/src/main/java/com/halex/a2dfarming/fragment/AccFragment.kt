package com.halex.a2dfarming.fragment

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.halex.a2dfarming.R
import com.halex.a2dfarming.view.PointViewModel


class AccFragment : Fragment(), SensorEventListener {

    //ViewModel per l'aggiornamento dei punteggi
    private lateinit var viewModel: PointViewModel

    //Variable for accellerometer
    private lateinit var sensorManager: SensorManager
    private lateinit var sAccelerometer: Sensor
    private var lastUpdate: Long = 0
    private var last_x = 0f
    private var last_y: Float = 0f
    private  var last_z: Float = 0f
    private val SHAKE_THRESHOLD = 600


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acc, container, false)
    }

    override fun onResume() {
        super.onResume()
        sAccelerometer.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        //If there is a new sensor data
        val mySensor: Sensor = sensorEvent.sensor

        if (mySensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = sensorEvent.values[0]
            val y = sensorEvent.values[1]
            val z = sensorEvent.values[2]

            val curTime = System.currentTimeMillis()

            if (curTime - lastUpdate > 100) {
                val diffTime: Long = curTime - lastUpdate
                lastUpdate = curTime
                val speed: Float = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000

                if (speed > SHAKE_THRESHOLD) {
                    viewModel.updateClick()
                }

                last_x = x
                last_y = y
                last_z = z
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}

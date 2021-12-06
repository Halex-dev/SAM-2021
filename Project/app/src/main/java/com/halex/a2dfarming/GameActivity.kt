package com.halex.a2dfarming

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.halex.a2dfarming.`class`.ItemModel
import com.halex.a2dfarming.`class`.MyAdapter
import com.halex.a2dfarming.view.PointViewModel

class GameActivity : AppCompatActivity(), SensorEventListener, MyAdapter.ClickListener{

    //ViewModel per l'aggiornamento dei punteggi
    private lateinit var viewModel: PointViewModel
    private lateinit var toast : Toast

    //Per accellerometro
    private lateinit var sensorManager: SensorManager
    private lateinit var sAccelerometer: Sensor
    private var lastUpdate: Long = 0
    private var last_x = 0f
    private var last_y: Float = 0f
    private  var last_z: Float = 0f
    private val SHAKE_THRESHOLD = 600


    //ObjList
    val MyAdapter = MyAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        InitViewModel()
        InitRecycler()

        //Inizializzazione dei sensori
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sAccelerometer , SensorManager.SENSOR_DELAY_NORMAL)

        val buttonFarming = findViewById<Button>(R.id.farming)
        buttonFarming.setOnClickListener {
            viewModel.updateClick()
        }

        val buttonMaps = findViewById<ImageButton>(R.id.mapsButton)
        buttonMaps.setOnClickListener {
            /*val mIntent = Intent(this@GameActivity, MapsActivity::class.java)
            //mIntent.putExtra("obj", viewModel)
            startActivity(mIntent)*/
            //finish()
        }
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

    //register
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

    override fun onStop() {
        super.onStop()
        viewModel.onStopAct()
    }

    override fun ClickedItem(item: ItemModel) {
        if(!viewModel.addItem(item.name))
            toast.show()
        else
            MyAdapter.setData(viewModel.returnData())
    }

    //Inizializzazione viewModel
    private fun InitViewModel(){
        viewModel =  ViewModelProvider(this).get(PointViewModel::class.java)
        observerView()
        viewModel.loadData()
        toast = Toast.makeText(applicationContext, "Non hai abbastanza soldi!", Toast.LENGTH_SHORT)
    }

    //Inizializzazione Recycler view
    private fun InitRecycler(){
        val recyclerView = findViewById<RecyclerView>(R.id.itemRecylerView)
        MyAdapter.setData(viewModel.returnData())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = MyAdapter
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //If sensor accuracy changes.
    }

    @SuppressLint("SetTextI18n")
    private fun observerView(){

        viewModel.point.observe(this, {
            val textPoint: TextView = (findViewById(R.id.valore))
            textPoint.text = it.toString()
        })

        viewModel.nickname.observe(this, {
            val textNickname: TextView = (findViewById(R.id.textNickname))
            textNickname.text = it.toString()
        })
    }
}


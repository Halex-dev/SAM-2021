package com.halex.a2dfarming

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.halex.a2dfarming.fragment.AccFragment
import com.halex.a2dfarming.fragment.MapsFragment
import com.halex.a2dfarming.fragment.PotionFragment
import com.halex.a2dfarming.view.PointViewModel

class MainActivity : AppCompatActivity() {

    //ViewModel per l'aggiornamento dei punteggi
    private lateinit var viewModel: PointViewModel
    private lateinit var toast : Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel =  ViewModelProvider(this).get(PointViewModel::class.java)
        observerView()
        if(viewModel.existUser() != true){
            viewModel.createUser("Halex")
        }

        viewModel.loadData()

        switchFragment(1)

        val buttonFarming = findViewById<Button>(R.id.farming)
        buttonFarming.setOnClickListener {
            viewModel.updateClick()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStopAct()
    }

    //----------------------------------- PRIVATE FUNCTION -------------------------------------
    private fun switchFragment(choose : Int){

        var fragment : Fragment? = null

        when(choose){
            1 -> {
                fragment = PotionFragment()
            }
            2 -> {
                fragment = AccFragment()
            }
            3 -> {
                fragment = MapsFragment()
            }
        }

        val fragmentTransition : FragmentTransaction = supportFragmentManager.beginTransaction()

        if (fragment != null) {
            fragmentTransition.replace(R.id.frameLayout, fragment)
        }

        fragmentTransition.addToBackStack(null)
        fragmentTransition.commit()
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

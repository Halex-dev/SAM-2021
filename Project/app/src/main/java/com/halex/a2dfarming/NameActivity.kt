package com.halex.a2dfarming

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.halex.a2dfarming.view.PointViewModel


class NameActivity : AppCompatActivity() {

    //ViewModel per l'aggiornamento dei punteggi
    private lateinit var viewModel: PointViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        viewModel =  ViewModelProvider(this).get(PointViewModel::class.java)

        if(viewModel.existUser() == true){
            val i = Intent(this@NameActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        val ButtonName = findViewById<Button>(R.id.ConfirmName)
        ButtonName.setOnClickListener {

            val name = (findViewById<EditText>(R.id.UserNickname)).text

            viewModel.createUser(name.toString())

            val i = Intent(this@NameActivity,  MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
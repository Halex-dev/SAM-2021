package com.halex.a2dfarming.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.halex.a2dfarming.R
import com.halex.a2dfarming.`class`.ItemModel
import com.halex.a2dfarming.database.Database
import com.halex.a2dfarming.obj.Player
import java.util.*

class PointViewModel(application: Application) : AndroidViewModel(application) {

    private val delayTask : Long = 1000

    //Database e Player
    private var db: Database = Database(application)
    private var user: Player? = db.getUser()

    //Dicchiarazione dei punteggi e delle pozioni
    private var _point = MutableLiveData<Long>()
    val point: LiveData<Long>
        get() = _point

    private var _nickname = MutableLiveData<String>()
    val nickname: LiveData<String>
        get() = _nickname

    private var t : Timer = Timer()

    override fun onCleared() {
        super.onCleared()
        t.cancel()
        //Elimina tutte le sottoscrizioni per evitare perdite di memoria
    }

    fun loadData(){
        startTimer()

        _point.postValue(user!!.returnPoint())
        _nickname.postValue(user!!.returnName())
    }

    fun startTimer(){

        t.purge()

        //Task del timer
         val task : TimerTask = object : TimerTask() {
            override fun run() {
                pointTask()
            }
        }

        t.scheduleAtFixedRate(task, 0, delayTask)
    }

    //Funzioni per la creazione dell'utente
    fun createUser(name: String){
        this.user = Player(name)
        db.addUser(this.user!!)
        db.createObj()
    }

    fun updateClick() {
        val point = user!!.updateClick()
        _point.postValue(point)
    }

    fun onStopAct() {
        db.updateUser(user!!)
        t.purge()
    }

    fun addItem(name: String): Boolean{
        val item = user!!.getItem(name)

        if(user!!.returnPoint() < item.returnCostAmount())
            return false

        _point.postValue(user!!.incAmountItem(name))

        return true
    }

    fun existUser(): Boolean {
        return db.existUser()
    }

    fun returnData(): ArrayList<ItemModel> {
        val objs = user!!.getAllObj()
        val array = ArrayList<ItemModel>()

        for ((key, item) in objs) {
            var draw = R.drawable.potion1
            if(key == "Pozione 2")
                draw = R.drawable.potion2
            if(key == "Pozione 3")
                draw = R.drawable.potion3
            if(key == "Pozione 4")
                draw = R.drawable.potion4
            if(key == "Pozione 5")
                draw = R.drawable.potion5

            array.add(ItemModel(draw, key, item))
        }

        array.sortBy{ it.name }
        return array
    }

    private fun pointTask(){
        val nuovo: Long = user!!.calculatePoint()
        _point.postValue(nuovo)
    }

}
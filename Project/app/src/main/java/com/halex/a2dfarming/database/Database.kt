package com.halex.a2dfarming.database

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.halex.a2dfarming.obj.ItemData
import com.halex.a2dfarming.obj.Player

class Database(context: Application): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "farm2d"

        //User Table
        private const val TABLE_USER = "user"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "nickname"
        private const val KEY_POINTS = "point"
        private const val KEY_MULTIPLER = "multipler"
        private const val KEY_SHARE = "share"
        private const val KEY_PRESTIGE = "prestige"

        //Obj Table
        private const val TABLE_OBJ = "object"
        private const val OBJ_KEY = "name"
        private const val OBJ_AMO = "amount"
        private const val OBJ_COST = "cost"
        private const val OBJ_COSTUP = "costup"
        private const val OBJ_SECOND = "point" //Quanti punti fa di base per secondo
        private const val OBJ_LEVEL = "level"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(
            "CREATE TABLE " + TABLE_USER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_POINTS + " INTEGER," +  KEY_MULTIPLER + " INTEGER,"
                    + KEY_SHARE + " INTEGER, " + KEY_PRESTIGE + " INTEGER)"
        )

        db?.execSQL(
            "CREATE TABLE " + TABLE_OBJ + "("
                    + OBJ_KEY + " TEXT PRIMARY KEY," + OBJ_AMO + " INTEGER,"
                    + OBJ_SECOND + " INTEGER," +  OBJ_LEVEL + " INTERGER,"
                    + OBJ_COST+ " INTEGER," + OBJ_COSTUP + " INTEGER )"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    //Insert User
    fun addUser(user: Player):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, user.returnID())
        contentValues.put(KEY_NAME, user.returnName())
        contentValues.put(KEY_POINTS,user.returnPoint())
        contentValues.put(KEY_MULTIPLER,user.returnMultipler())
        contentValues.put(KEY_SHARE,user.returnPrestige())
        contentValues.put(KEY_PRESTIGE,user.returnPrestige())

        // Inserting Row
        val success = db.insert(TABLE_USER, null, contentValues)
        db.close() // Closing database connection
        return success
    }

    //update User
    fun updateUser(user: Player): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_ID, user.returnID())
        contentValues.put(KEY_NAME, user.returnName())
        contentValues.put(KEY_POINTS,user.returnPoint())
        contentValues.put(KEY_MULTIPLER,user.returnMultipler())
        contentValues.put(KEY_SHARE,user.returnPrestige())
        contentValues.put(KEY_PRESTIGE,user.returnPrestige())

        // Inserting Row
        val success = db.update(TABLE_USER, contentValues, "$KEY_ID=" + user.returnID(), null)
        db.close() // Closing database connection

        updateObj(user)

        return success
    }

    //update Obj
    private fun updateObj(user: Player){
        val db = this.writableDatabase
        val contentValues = ContentValues()

        val hash = user.getAllObj()

        for ((key, item) in hash) {
            contentValues.put(OBJ_KEY, key)
            contentValues.put(OBJ_AMO, item.returnAmount())
            contentValues.put(OBJ_COST, item.returnCost())
            contentValues.put(OBJ_COSTUP, item.returnCostAmount())
            contentValues.put(OBJ_SECOND, item.returnPoint())
            contentValues.put(OBJ_LEVEL, item.returnLevel())

            db.update(TABLE_OBJ, contentValues, "$OBJ_KEY= \"$key\" ", null)
        }

        db.close() // Closing database connection
    }

    //Insert Obj
    private fun addObj(name: String, item: ItemData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(OBJ_KEY, name)
        contentValues.put(OBJ_AMO, item.returnAmount())
        contentValues.put(OBJ_COST, item.returnCost())
        contentValues.put(OBJ_COSTUP, item.returnCostAmount())
        contentValues.put(OBJ_SECOND, item.returnPoint())
        contentValues.put(OBJ_LEVEL, item.returnLevel())

        //Insert into database
        val success = db.insert(TABLE_OBJ, null, contentValues)
        db.close() // Closing database connection
        return success
    }

    //create Obj
    fun createObj() {
        addObj("Pozione 1", ItemData(0,20,1,1))
        addObj("Pozione 2", ItemData(0,100,5,1))
        addObj("Pozione 3", ItemData(0,500,10,1))
        addObj("Pozione 4", ItemData(0,1000,20,1))
        addObj("Pozione 5", ItemData(0,5000,100,1))
    }

    @SuppressLint("Recycle")
    fun existUser(): Boolean {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_USER"
        val result = db.rawQuery(query, null)

        return result.count > 0
    }

    @SuppressLint("Recycle")
    fun getUser(): Player? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_USER"
        val result = db.rawQuery(query, null)

        if(result.moveToFirst()){

            val user = Player(result.getString(result.getColumnIndex(KEY_NAME)).toString(),
                result.getString(result.getColumnIndex(KEY_POINTS)).toLong(),
                result.getString(result.getColumnIndex(KEY_SHARE)).toInt(),
                result.getString(result.getColumnIndex(KEY_PRESTIGE)).toInt(),
                result.getString(result.getColumnIndex(KEY_MULTIPLER)).toInt()
                )

            getObj(user)
            return user
        }

        return null
    }

    @SuppressLint("Recycle")
    private fun getObj(user: Player){
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_OBJ"
        val result = db.rawQuery(query, null)

        if(result.moveToFirst()){
            do{
                val name = result.getString(result.getColumnIndex(OBJ_KEY))

                val data = ItemData(result.getString(result.getColumnIndex(OBJ_AMO)).toInt(),
                    result.getString(result.getColumnIndex(OBJ_COST)).toInt(),
                    result.getString(result.getColumnIndex(OBJ_SECOND)).toInt(),
                    result.getString(result.getColumnIndex(OBJ_LEVEL)).toInt()
                )

                user.addItem(name, data)
            } while (result.moveToNext())
        }
    }
}
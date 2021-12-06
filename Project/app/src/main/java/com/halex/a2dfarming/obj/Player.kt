package com.halex.a2dfarming.obj


class Player {
    private var id: Int = 0
    private var nickname: String? = null
    private var points: Long = 0
    private var share: Int = 0
    private var multipler: Int = 1
    private var prestige = 0
    private var click = 1
    private var obj: HashMap<String, ItemData> = HashMap()

    constructor(nickname: String){
        this.nickname = nickname
    }

    constructor(nickname: String, points: Long, share: Int, prestige: Int, multipler: Int) {
        this.nickname = nickname
        this.points = points
        this.prestige = prestige
        this.share = share
        this.multipler = multipler + share + prestige
    }

    fun incAmountItem(name: String): Long {

        val item : ItemData = this.obj.get(name)!!

        this.points -= item.returnCostAmount()

        item.incAmount()

        return this.points
    }

    fun addItem(name: String, data: ItemData) {
        this.obj.put(name, data)
    }

    fun getItem(name: String): ItemData{
        return this.obj.get(name)!!
    }

    fun addClick(){
        this.click++
    }

    fun updateClick() : Long{
        this.points += click
        return this.points
    }

    fun calculatePoint(): Long {
        val valore =  this.points + (itemsPoint()) * (this.multipler + this.prestige)
        this.points = valore
        //Log.d("Soldi:","totale da aggiungere su utente ${valore}")
        return valore
    }

    private fun itemsPoint(): Int {
        var tot = 0

        for ((_, item) in this.obj) {
            tot += (item.returnPoint() * item.returnLevel() ) * item.returnAmount()
        }

        //Log.d("Soldi:","totale da aggiungere ${tot}")

        return tot
    }

    //Funzioni per il return dei dati (Se servono)
    fun returnName(): String? {
        return this.nickname
    }

    fun returnPoint(): Long {
        return this.points
    }

    fun returnMultipler(): Int {
        return this.multipler
    }

    fun returnPrestige(): Int {
        return this.prestige
    }

    fun returnID(): Int {
        return this.id
    }

    fun getAllObj(): HashMap<String, ItemData> {
        return this.obj
    }

}
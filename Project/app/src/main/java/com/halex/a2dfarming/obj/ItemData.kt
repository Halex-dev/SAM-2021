package com.halex.a2dfarming.obj

class ItemData {
    private var amount: Int = 0 //Quantità dell'oggetto
    private var cost: Int = 0 //Costo di base dell'oggetto
    private var point: Int = 1 //Quanti punti da di base
    private var level: Int = 0 // livello dell'oggetto
    private var costAmount: Int = cost //Costo upgrade di base

    constructor(amount: Int, cost: Int, point: Int , level: Int){
        this.amount = amount
        this.cost= cost
        this.point = point
        this.level = level
        this.costAmount = cost

        if(amount > 0)
            this.costAmount = cost * amount
    }

    fun incAmount(){
        this.amount++
        this.costAmount += (this.cost + ( 20 * amount))
    }

    fun incLevel(){
        this.level++
    }

    fun pointSecond(): Int{
        return (this.point * this.amount) * this.level
    }

    fun returnAmount(): Int {
        return this.amount
    }

    fun returnCost(): Int {
        return this.cost
    }

    fun returnPoint(): Int {
        return this.point
    }

    fun returnLevel(): Int {
        return this.level
    }

    fun returnCostAmount(): Int {
        return this.costAmount
    }
}
package com.example.mappokemon

import android.location.Location

class Pokemon{
    var name:String? = null
    var des:String? = null
    var image:Int? = null
    var power:Double? = null
    var isCatched:Boolean?=false
    var location:Location?=null
    constructor(image:Int,name:String,des:String,power:Double,latitude:Double,longitude:Double)
    {
        this.name=name
        this.des = des
        this.image=image
        this.power=power
        this.location=Location(name)
        this.location!!.latitude=latitude
        this.location!!.longitude=longitude
        this.isCatched=false
    }

}
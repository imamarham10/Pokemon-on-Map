package com.example.mappokemon

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
        LoadPokemon()
    }
    var ACCESSLOCATION = 123
    fun checkPermission ()
    {
        if(Build.VERSION.SDK_INT>=23){

            if(ActivityCompat.
                checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESSLOCATION)
                return
            }
        }
        GetUserLocation()
    }

    fun GetUserLocation()
    {
        Toast.makeText(this,"User location access on",Toast.LENGTH_LONG).show()
        //TODO: Will implement later

        var myLocation= MylocationListener()

        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

        var mythread=myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        when(requestCode)
        {
            ACCESSLOCATION->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    GetUserLocation()
                }
                else
                {
                    Toast.makeText(this,"We cannot access your location",Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        }

    var location:Location? = null
    //get user location

    inner class MylocationListener:LocationListener{
        constructor(){
            location= Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }

        override fun onLocationChanged(p0: Location) {
            location = p0
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    var oldLocation:Location?=null

    inner class myThread:Thread{
        constructor():super()
        {
            oldLocation = Location("Start")
            oldLocation!!.latitude=0.0
            oldLocation!!.longitude=0.0
        }
        override fun run()
        {
            while(true)
            {
                try {
                    if(oldLocation!!.distanceTo(location)==0f)
                    {
                        continue
                    }
                    oldLocation = location
                    runOnUiThread()
                    {
                        mMap!!.clear()

                        //show me

                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap!!.addMarker(
                            MarkerOptions()
                                .position(sydney)
                                .title("Me")
                                .snippet("here is my location"+"power:"+playerPower)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ash)))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))

                        //show Pokemons
                        for(i in 0..listofPokemon.size-1)
                        {
                            var newPokemon = listofPokemon[i]
                            if(newPokemon.isCatched==false)
                            {
                                val pokemonLoc = LatLng(
                                    newPokemon.location!!.latitude,
                                    newPokemon.location!!.longitude
                                )
                                mMap!!.addMarker(
                                    MarkerOptions()
                                        .position(pokemonLoc)
                                        .title(newPokemon.name!!)
                                        .snippet(newPokemon.des!! + ",power:"+newPokemon!!.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))
                                if (location!!.distanceTo(newPokemon.location)<=2)
                                {
                                    newPokemon.isCatched=true
                                    listofPokemon[i] = newPokemon
                                    playerPower += newPokemon.power!!
                                    Toast.makeText(applicationContext,"You catch a new POKEMON. Your new power is"+playerPower,Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    Thread.sleep(1000)
                } catch(ex:Exception) {}
            }
        }

    }
    var playerPower:Double = 0.0
    var listofPokemon = ArrayList<Pokemon>()
    fun LoadPokemon()
    {
        listofPokemon.add(
            Pokemon(R.drawable.charmander,"Charmander",
            "Charmander is from Japan",55.0,25.584350, 83.581220))
        listofPokemon.add(Pokemon(R.drawable.bulbasaur,"Bulbasaur",
            "Bulbasaur is living in USA",90.5,25.583050, 83.588650))
        listofPokemon.add(Pokemon(R.drawable.squirtle,"Squirtle",
            "Squirtle living in iraq", 33.5, 25.573267, 83.572542))
    }
}
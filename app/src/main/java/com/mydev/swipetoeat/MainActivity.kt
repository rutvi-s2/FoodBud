package com.mydev.swipetoeat

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mydev.swipetoeat.data.DataSource
import com.mydev.swipetoeat.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


private const val TAG = "MainActivity"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "-N__kCIaHXEKvFd2-HRBMdMoBDpQZhrelGNssS0wRMpeKnGo5oinLGXWjZZfcGl8DtZksQ4ZgcPnyNoyzwSCuA3XuvWwvyfsgo5FB95cL4cnDqEEWw27agn6m9t2Y3Yx"
class MainActivity : AppCompatActivity()  {
    private lateinit var binding : ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var spinnerLabel : String = ""
    var chosenCuisine : String = ""
    private var timeInput : Int = (System.currentTimeMillis()/1000).toInt()
    private var timeInputStr : String = ""
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // clear lists everytime the user starts on home screen
        DataSource.restaurants.clear()
        DataSource.swipedRightRestaurants.clear()

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(Toast.LENGTH_LONG.toLong()) // As I am using LENGTH_LONG in Toast
                    finish()
                    exitProcess(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        val cuisineAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            DataSource.cuisines
        )
        // set simple layout resource file for each item of spinner
        cuisineAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter data on the spinner
        binding.desiredCuisineSpinner.adapter = cuisineAdapter


        // get the cuisine the user chooses from the spinner
        binding.desiredCuisineSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // first get the cuisine that the user selected
                chosenCuisine = parent?.getItemAtPosition(position).toString()
                // now set it to the alias so we can send it in the YELP request
                val indexOfAlias = DataSource.cuisines.indexOf(chosenCuisine)
                spinnerLabel = DataSource.cuisineAlias[indexOfAlias]
            }

        }


        // function that performs the yelp API request depending on user input
        fun yelpAPIForRestaurants() {
            val restaurants: MutableList<YelpRestaurant> = DataSource.restaurants

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val yelpService = retrofit.create(YelpService::class.java)
            Log.d("spinnerLabel", spinnerLabel)
            Log.d("time input", timeInput.toString())
            yelpService.searchRestaurants("Bearer $API_KEY",timeInput, spinnerLabel, binding.location.text.toString()).enqueue(object : Callback<YelpSearchResult> {
                override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive valid response body from Yelp API ... exit")

                        return
                    }
//                    restaurants.addAll(body.restaurants)
                    for (cur in body.restaurants){
                        if(cur.imageUrl != ""){
                            Log.i("not removed", "not removed restaurant")
                            restaurants.add(cur)
                        } else{
                            Log.i("removed", "removed restaurant")
                        }
                    }

                    DataSource.restaurants = restaurants

                    Log.i("we get in here", "omg")
                }

                override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                    val text = "Please check WIFI connection and re-open app"
                    val duration = Toast.LENGTH_LONG

                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                    thread.start()
                }
            })
        }


        binding.startSwiping.setOnClickListener {
            // ensure the user enters valid information for API request
            if (binding.location.text.isEmpty()) {
                val text = "Please enter information."
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            } else if (binding.location.text.length != 5) {
                val text = "Incorrect Zipcode length"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            } else {
                // intent to go to start swiping page
                GlobalScope.launch {
                    // function call to retrieve list of restaurants from yelp
                    yelpAPIForRestaurants()
                    delay(2000L)
                    val intent = Intent(this@MainActivity, SwipeActivity::class.java)
                    intent.putExtra("chosen cuisine", chosenCuisine)
                    startActivity(intent)
                }
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()


        val selectTimeButton = findViewById<Button>(R.id.desiredTimeBtn)
        val formatter = SimpleDateFormat("MMM dd yyyy", Locale.US)
        val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US)
        selectTimeButton.setOnClickListener{
            val now = Calendar.getInstance()
            var time: String
            val timePicker = TimePickerDialog(this, { view, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                time = timeFormatter.format(selectedTime.time)

                val displayFormat = SimpleDateFormat("HH:mm")
                val parseFormat = SimpleDateFormat("hh:mm a")
                val myTime : Date = parseFormat.parse(time)

                val sdf : SimpleDateFormat  = SimpleDateFormat("MMM dd yyyy");
                val c : Calendar = Calendar.getInstance()
                val date = sdf.format(c.getTime())

                timeInputStr = date + " " + displayFormat.format(myTime) + ":00.000"
                Log.d("my time one", timeInputStr)
                val df = SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS")
                val myDt : Date = df.parse(timeInputStr)
                Log.d("my time not parsed", myDt.toString())
                timeInput = (myDt.time / 1000).toInt()

                Log.d("my time", timeInput.toString())
                binding.selectedTimeText.text = "$date $time"
            },
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false)
            timePicker.show()


        }
    }


    private fun getCurrentLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                val location: Location? = getLastKnownLocation()
                if(location == null) {
                    Toast.makeText(this, "Exception: Location not fetched. Enter Manually.", Toast.LENGTH_SHORT).show()
                } else{
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    binding.location.setText((addresses[0].postalCode).toString())
                }
            } else {
                Toast.makeText(this, "Turn on Location in Settings", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }
    private fun getLastKnownLocation(): Location? {
        val mLocationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
                return null
            }
            val l: Location? = mLocationManager.getLastKnownLocation(provider)
            if (l == null) {
                continue
            }
            if (bestLocation == null
                || l.accuracy < bestLocation.accuracy
            ) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    private fun isLocationEnabled(): Boolean{
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION =100
    }
    private fun checkPermissions(): Boolean{
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Granted Location Permission", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else{
                Toast.makeText(applicationContext, "Denied Location Permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
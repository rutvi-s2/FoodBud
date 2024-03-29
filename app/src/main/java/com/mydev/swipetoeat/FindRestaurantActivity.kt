package com.mydev.swipetoeat

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mydev.swipetoeat.adapter.RestaurantCardAdapter
import com.mydev.swipetoeat.data.DataSource
import com.mydev.swipetoeat.databinding.ActivityFindRestaurantBinding
import kotlin.random.Random


private const val BASE_URL = "https://www.yelp.com/biz/"
class FindRestaurantActivity : AppCompatActivity(), RestaurantCardAdapter.OnItemClickListener{
    private lateinit var binding: ActivityFindRestaurantBinding
    // Creates layout for find restaurant page and makes it scrollable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_restaurant)
        binding = ActivityFindRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gridRecyclerView.adapter = RestaurantCardAdapter(this, this)

        // Specify fixed size to improve performance
        binding.gridRecyclerView.setHasFixedSize(true)



        // when randomly generate button is clicked, we choose a restaurant for the user
        binding.surpriseMeButton.setOnClickListener {
            if (DataSource.swipedRightRestaurants.isEmpty()) {
                val text = "You have not swiped right on any meals yet!"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            } else {
                val seed = System.nanoTime()
                Log.d("rightRestaurants", DataSource.swipedRightRestaurants.toString())
                DataSource.swipedRightRestaurants.shuffle(Random(seed))
                val index: Int = Random.nextInt(DataSource.swipedRightRestaurants.size)

                val intent = Intent(this, SurpriseMeActivity::class.java)
                intent.putExtra("restaurantIndex", index)
                startActivity(intent)
            }
        }

        // start over button
        binding.startOver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            DataSource.restaurants.clear()
            DataSource.swipedRightRestaurants.clear()
        }


    }

    // when RestaurantCardAdapter is clicked, send the data over the intent and start the intent
    // for yelp screen
    override fun onItemClick(position: Int, restaurants: List<YelpRestaurant>) {
        val restaurant: YelpRestaurant = DataSource.swipedRightRestaurants[position]
        var restaurantName = restaurant.name
        var restaurantCity = restaurant.location.city
        restaurantName = restaurantName.replace("\\s".toRegex(), "-")
        restaurantCity = restaurantCity.replace("\\s".toRegex(), "-")
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL.plus(restaurantName).plus("-").plus(restaurantCity)))
            startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, "No application can handle this request."
                        + " Please install a webbrowser", Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

}
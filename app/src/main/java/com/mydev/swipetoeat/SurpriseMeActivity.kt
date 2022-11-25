package com.mydev.swipetoeat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.mydev.swipetoeat.data.DataSource
import com.mydev.swipetoeat.databinding.ActivitySurpriseMeBinding
import java.util.concurrent.Executors
import android.os.Handler


class SurpriseMeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySurpriseMeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surprise_me)
        binding = ActivitySurpriseMeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val extras = intent.extras
        val restaurant: YelpRestaurant = DataSource.swipedRightRestaurants[extras?.getInt("restaurantIndex")!!]
        binding.restaurantGridName.text = restaurant.name
        binding.restaurantGridLocation.text = restaurant.location.displayLocation()
        val restaurantGridReviewsText = restaurant.numReviews.toString() + " reviews"
        binding.restaurantGridReviews.text = restaurantGridReviewsText
        val restaurantGridDistanceText = restaurant.displayDistance() + " from current location"
        binding.restaurantGridDistance.text = restaurantGridDistanceText
        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()
        // Once the executor parses the URL and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())
        // Initializing the image
        var image: Bitmap?
        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {
            // Image URL
            val imageURL = restaurant.imageUrl
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    binding.restaurantGridImage.setImageBitmap(image)
                }
            }
            // If the URL does not point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }


        // back button
        binding.back.setOnClickListener {
            val intent = Intent(this, FindRestaurantActivity::class.java)
            startActivity(intent)
        }
    }
}
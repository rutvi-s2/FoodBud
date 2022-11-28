package com.mydev.swipetoeat.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.BaseAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.mydev.swipetoeat.data.DataSource
import java.util.concurrent.Executors
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.mydev.swipetoeat.R
import com.mydev.swipetoeat.YelpRestaurant


class SwipeAdapter(
    private val list: List<YelpRestaurant>,
    private val context: Context)
    : BaseAdapter() {
    // initialize the data using the restaurants List found in DataSource
    private var restaurantOpts: List<YelpRestaurant> = list


    override fun getCount(): Int {
        return DataSource.restaurants.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertV = convertView
        // inflate the layout for each card
        if (convertV == null) {
            convertV = LayoutInflater.from(context).inflate(
                R.layout.item_koloda, parent, false
            )
        }

        // get the TextView for restuarant image
        val restaurantImageView: ImageView = convertV!!.findViewById(R.id.koloda_image)
        Log.d("restaurntsinswipeadap", DataSource.restaurants.toString())

        //sets the text for item name and item description from the current item object
        val restaurant = restaurantOpts[position]
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
                    restaurantImageView.setImageBitmap(image)
                }
            }

            // If the URL does not point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }


        // returns the view for the current row
        return convertV

    }


}


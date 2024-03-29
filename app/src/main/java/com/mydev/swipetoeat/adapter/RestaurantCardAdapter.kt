package com.mydev.swipetoeat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mydev.swipetoeat.R
import com.mydev.swipetoeat.data.DataSource
import com.mydev.swipetoeat.YelpRestaurant

class RestaurantCardAdapter (
    private val context: Context,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<RestaurantCardAdapter.RestaurantCardViewHolder>() {


    // initialize the data using the swiped right restaurants List found in DataSource
    private var restaurants: List<YelpRestaurant> = DataSource.swipedRightRestaurants


    /**
     * Initialize view elements
     */
    inner class RestaurantCardViewHolder(view: View?): RecyclerView.ViewHolder(view!!),
        View.OnClickListener{
        // declare and initialize all of the bunny list item UI components
        val restaurantImageView: ImageView = view!!.findViewById(R.id.restaurant_grid_image)
        val restaurantNameTextView: TextView = view!!.findViewById(R.id.restaurant_grid_name)
        val restaurantDistanceTextView: TextView = view!!.findViewById(R.id.restaurant_grid_distance)
        val restaurantLocationTextView: TextView = view!!.findViewById(R.id.restaurant_grid_location)

        // set up the onClick so restaurant card can be clickable in order to view YELP screen
        init {
            if (view != null) {
                view.setOnClickListener(this)
            }
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, restaurants)
            }
        }
    }

    // declare the onItemClick to be initialized in MainActivity2.kt
    interface OnItemClickListener {
        fun onItemClick(position: Int, restaurants: List<YelpRestaurant>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantCardAdapter.RestaurantCardViewHolder {
        // determine which list item should be used and set layout accordingly
        // grid vs vertical/horizontal

        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_grid_item, parent, false)
        // inflate layout and return
        return RestaurantCardViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        // return size of restaurant data set
        return restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantCardAdapter.RestaurantCardViewHolder, position: Int) {
        //get the data at the current position
        val restaurant = restaurants[position]

        //set text for name, distance, and hours of restaurant
        holder.restaurantNameTextView.text = restaurant.name
        //show rating for now
        holder.restaurantDistanceTextView.text = restaurant.displayDistance()
        holder.restaurantLocationTextView.text = restaurant.location.displayLocation()
        Glide.with(context).load(restaurant.imageUrl).apply(
            RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
        )).into(holder.restaurantImageView)

    }

}
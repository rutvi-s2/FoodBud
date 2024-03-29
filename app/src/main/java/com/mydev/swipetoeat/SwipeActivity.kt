package com.mydev.swipetoeat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mydev.swipetoeat.adapter.SwipeAdapter
import com.mydev.swipetoeat.data.DataSource
import com.mydev.swipetoeat.databinding.ActivitySwipeBinding
import com.yalantis.library.Koloda
import com.yalantis.library.KolodaListener

class SwipeActivity : AppCompatActivity() {
    private lateinit var adapter: SwipeAdapter
    private lateinit var list: MutableList<YelpRestaurant>
    private lateinit var koloda: Koloda
    var toast : Toast? = null
    //    var mediaPlayer : MediaPlayer? = null
//    var countSwiped : Int = 0
    private lateinit var binding : ActivitySwipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)
        binding = ActivitySwipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // set the cuisine the user chose to the text shown on the screen
        val extras = intent.extras
        val chosenCuisine: String = extras?.getString("chosen cuisine")!!
        val displayCuisine: String
        if (chosenCuisine.isEmpty()) {
            displayCuisine = "Desired Cuisine: All"
        } else {
            displayCuisine = "Desired Cuisine: $chosenCuisine"
        }

        if(DataSource.restaurants.isEmpty()){
            binding.endText.visibility = View.VISIBLE
            binding.startOver.visibility = View.VISIBLE
            binding.reset.visibility = View.INVISIBLE
            binding.doneSwiping.visibility = View.INVISIBLE
        }
        binding.desiredCuisine.text = displayCuisine



        koloda = findViewById(R.id.koloda)

        list = (DataSource.restaurants).toCollection(mutableListOf())
        var start = 0
        while (start < DataSource.countSwiped){
            list.removeFirst()
            start++
        }
        adapter = SwipeAdapter(list, this)
        koloda.adapter = adapter
        Log.d("the first length is", list.size.toString())



        koloda.kolodaListener = object : KolodaListener {

            override fun onCardSwipedLeft(position: Int) {
                val text = "DISLIKE!"
                val duration = Toast.LENGTH_SHORT

                // fade in
                binding.thumbsDown.visibility = View.VISIBLE
                //loading our custom made animations
                var animation = AnimationUtils.loadAnimation(this@SwipeActivity, R.anim.fade_in)
                //starting the animation
                binding.thumbsDown.startAnimation(animation)


                // fade out
                animation = AnimationUtils.loadAnimation(this@SwipeActivity , R.anim.fade_out)
                binding.thumbsDown.startAnimation(animation)
                //textview will be invisible after the specified amount
                // of time elapses, here it is 1000 milliseconds
                Handler().postDelayed({
                    binding.thumbsDown.visibility = View.GONE
                }, 1000)

                DataSource.countSwiped++
                if (toast!= null) {
                    toast!!.cancel()
                }
                toast = Toast.makeText(applicationContext, text, duration)
                toast!!.show()

                if(position == list.size - 2){
                    binding.doneSwipingText.visibility = View.VISIBLE
                }
            }

            override fun onCardSwipedRight(position: Int) {
                val text = "LIKE!"
                val duration = Toast.LENGTH_SHORT

                // fade in
                binding.thumbsUp.bringToFront()
                binding.thumbsUp.visibility = View.VISIBLE
                //loading our custom made animations
                var animation = AnimationUtils.loadAnimation(this@SwipeActivity, R.anim.fade_in)
                //starting the animation
                binding.thumbsUp.startAnimation(animation)


                // fade out
                animation = AnimationUtils.loadAnimation(this@SwipeActivity , R.anim.fade_out)
                binding.thumbsUp.startAnimation(animation)
                //textview will be invisible after the specified amount
                // of time elapses, here it is 1000 milliseconds
                Handler().postDelayed({
                    binding.thumbsUp.visibility = View.GONE
                }, 1000)


                DataSource.countSwiped++
                if (toast!= null) {
                    toast!!.cancel()
                }
                toast = Toast.makeText(applicationContext, text, duration)
                toast!!.show()

                // get the restaurant swiped right on
                val restaurant: YelpRestaurant = list[position + 1]
                DataSource.swipedRightRestaurants.add(restaurant)

                if(position == list.size - 2){
                    binding.doneSwipingText.visibility = View.VISIBLE
                }
            }

        }



        // Intent takes user to the find restaurants page once they are done swiping
        val restaurantsPage = findViewById<Button>(R.id.done_swiping)
        restaurantsPage.setOnClickListener {

            var start = 0
            while (start < DataSource.countSwiped){
                list.removeFirst()
                start++
            }
            DataSource.countSwiped = 0
            val intent = Intent(this, FindRestaurantActivity::class.java)
            startActivity(intent)
        }

        val goBack = findViewById<Button>(R.id.start_over)
        goBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            DataSource.restaurants.clear()
            DataSource.swipedRightRestaurants.clear()
        }

        val reset = findViewById<Button>(R.id.reset)
        reset.setOnClickListener{
            list = (DataSource.restaurants).toCollection(mutableListOf())
            DataSource.swipedRightRestaurants.clear()
            DataSource.countSwiped = 0
            finish()
            startActivity(intent)
            Log.d("the second length is", list.size.toString())
        }

    }

//    fun playAudio()  {
//        val audioUrl = "https://www.fesliyanstudios.com/play-mp3/7756"
//        mediaPlayer = MediaPlayer()
//        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
//        try{
//            mediaPlayer!!.setDataSource(audioUrl)
//            mediaPlayer!!.prepare()
////            mediaPlayer!!.start()
//
//        } catch(e: IOException){
//            e.printStackTrace()
//        }
//    }


}
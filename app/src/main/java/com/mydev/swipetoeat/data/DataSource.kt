package com.mydev.swipetoeat.data

import com.mydev.swipetoeat.YelpRestaurant

/**
 * An object to generate a static list of movie reviews from user
 */
object DataSource {
//    var imageResourceBitmap: Bitmap = BitmapFactory.decodeResource(null, R.drawable.casa_de_mariscos_enchiladas);


    var restaurants: MutableList<YelpRestaurant> = mutableListOf()

    val cuisines = listOf<String>("Italian", "Chinese", "Indian", "Mexican", "American", "French", "Greek", "Thai", "Japanese", "Turkish", "Moroccan", "Korean", "German", "South African", "Lebanese", "Portuguese", "Vietnamese", "Belgian", "Pakistani", "Australian", "Brazilian", "Nepalese", "Russian", "Filipino", "Peruvian", "Ethiopian")
    var cuisineAlias = listOf<String>("italian", "chinese", "indpak", "mexican", "tradamerican", "french", "greek", "thai", "japanese", "turkish", "moroccan", "korean", "german", "southafrican", "lebanese", "portuguese", "vietnamese", "belgian", "pakistani", "australian", "brazilian", "himalayan", "russian", "filipino", "peruvian", "ethiopian")

    var swipedRightRestaurants: MutableList<YelpRestaurant> = mutableListOf()
}
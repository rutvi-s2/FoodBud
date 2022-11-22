package com.mydev.swipetoeat.data

import com.mydev.swipetoeat.YelpRestaurant

/**
 * An object to generate a static list of movie reviews from user
 */
object DataSource {
//    var imageResourceBitmap: Bitmap = BitmapFactory.decodeResource(null, R.drawable.casa_de_mariscos_enchiladas);


    var restaurants: MutableList<YelpRestaurant> = mutableListOf()

    val cuisines = listOf("Asian Fusion", "Australian", "Barbeque", "Bistros", "Breakfast & Brunch", "Buffets", "Burgers", "Cafes", "Chinese", "Comfort Food", "Delis", "Diners", "Ethiopian", "Fish & Chips", "Hawaiian", "Fast Food", "Indian", "Italian", "Korean", "Mediterranean", "Mexican", "Mongolian", "Pizza", "Sandwiches", "Seafood", "Sushi Bars", "Tex-Mex", "Thai", "Vegan", "Vegetarian", "Waffles")
    var cuisineAlias = listOf("asianfusion", "australian", "bbq", "bistros", "breakfast_brunch", "buffets", "burgers", "cafes", "chinese", "comfortfood", "delis", "diners", "ethiopian", "fishnchips", "hawaiian", "hotdogs", "indpak", "italian", "korean", "mediterranean", "mexican", "mongolian", "pizza", "sandwiches", "seafood", "sushi", "tex-mex", "thai", "vegan", "vegetarian", "waffles")

    var swipedRightRestaurants: MutableList<YelpRestaurant> = mutableListOf()
}
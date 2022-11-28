package com.mydev.swipetoeat.data

import com.mydev.swipetoeat.YelpRestaurant

/**
 * An object to generate different restaurant variables depending on user input
 */
object DataSource {

    var restaurants: MutableList<YelpRestaurant> = mutableListOf()

    val cuisines = listOf("Italian", "Chinese", "Indian", "Mexican", "American", "French", "Greek", "Thai", "Japanese", "Turkish", "Moroccan", "Korean", "German", "South African", "Lebanese", "Portuguese", "Vietnamese", "Belgian", "Pakistani", "Australian", "Brazilian", "Nepalese", "Russian", "Filipino", "Peruvian", "Ethiopian")
    var cuisineAlias = listOf("italian", "chinese", "indpak", "mexican", "tradamerican", "french", "greek", "thai", "japanese", "turkish", "moroccan", "korean", "german", "southafrican", "lebanese", "portuguese", "vietnamese", "belgian", "pakistani", "australian", "brazilian", "himalayan", "russian", "filipino", "peruvian", "ethiopian")

    var swipedRightRestaurants: MutableList<YelpRestaurant> = mutableListOf()

    var countSwiped = 0
}
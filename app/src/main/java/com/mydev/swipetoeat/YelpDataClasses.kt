package com.mydev.swipetoeat

import com.google.gson.annotations.SerializedName

data class YelpSearchResult(
    @SerializedName("businesses") val restaurants: List<YelpRestaurant>
)

data class YelpSearchResultCuisine(
    @SerializedName("categories") val cuisines: List<YelpCategory>
)

data class YelpRestaurant(
    //no need to specify serialized name if name of parameter in the object exactly matches var name in Kotlin
    val name: String,
    val rating: Double,
    val price : String,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl: String,
    val categories: List<YelpCategory>,
    val location : YelpLocation
) {
    fun displayDistance() : String{
        val milesPerMeter = 0.000621371
        val distanceInMiles = "%.2f".format(distanceInMeters * milesPerMeter)
        return "$distanceInMiles miles"
    }
}

data class YelpCategory(
    val title: String,
    @SerializedName("parent_aliases") val parentAliases: List<String>,
    @SerializedName("alias") val alias: String
)


data class YelpLocation(
    @SerializedName("address1") val address : String,
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("zip_code") val zip_code : String,
    @SerializedName("country") val country : String
){
    fun displayLocation(): String {
        return "$address, $city $state, $zip_code, $country"
    }
}
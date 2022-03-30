package uz.food_delivery.fooddelivery.models

import java.io.Serializable

class MyObject: Serializable {
    var name: String? = null
    var description: String? = null
    var price: Double? = null
    var id: Int? = null
    var images: List<String?>? = null
    var type : String? = null
    var mainSection: Boolean? = null
    var number: Int = 0
    var key: String? = null
    var keySecond: String? = null

    constructor()
}
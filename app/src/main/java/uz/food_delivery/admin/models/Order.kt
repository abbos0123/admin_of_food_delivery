package uz.food_delivery.fooddelivery.models

import java.io.Serializable

class Order: Serializable {
    var list: List<MyObject>? = null
    var user: User? = null
    var price: Double = 0.0
    var id: String? = null
    var time: String? = null
}
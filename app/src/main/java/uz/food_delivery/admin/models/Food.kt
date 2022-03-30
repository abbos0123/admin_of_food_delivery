package uz.food_delivery.fooddelivery.models

class Food {
    var name: String? = null
    var id: Int? = null
    var price: Float? = null
    var isLiked: Boolean? = false

    constructor()
    constructor(name: String?, id: Int?, price: Float?, isLiked: Boolean?) {
        this.name = name
        this.id = id
        this.price = price
        this.isLiked = isLiked
    }
}
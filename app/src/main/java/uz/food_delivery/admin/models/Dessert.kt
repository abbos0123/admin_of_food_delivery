package uz.food_delivery.admin.models

import java.io.Serializable

class Dessert : Serializable {
    var name: String? = null
    var description: String? = null
    var price: Double? = null
    var images: ArrayList<String?>? = null
    var key: String? = null
    var isMainSection: Boolean? = false

    constructor()
    constructor(
        name: String?,
        description: String?,
        price: Double?,
        images: ArrayList<String?>?,
        key: String?, isMainSection: Boolean? = false
    ) {
        this.name = name
        this.description = description
        this.price = price
        this.images = images
        this.key = key
        this.isMainSection = isMainSection
    }


}
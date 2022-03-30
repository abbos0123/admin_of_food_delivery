package uz.food_delivery.fooddelivery.models

class User {
    var username: String? = null
    var fullName: String? = null
    var password: String? = null
    var phone: String? = null
    var id : String? = null

    constructor()

    constructor(username: String?, password: String?, phone: String?, id: String) {
        this.username = username
        this.password = password
        this.phone = phone
        this.id = id
    }
}
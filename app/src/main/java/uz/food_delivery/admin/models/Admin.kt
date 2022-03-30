package uz.food_delivery.admin.models

class Admin {
    var fullName : String? = null
    var login: String? = null
    var id: String? = null
    var password: String? = null

    constructor()

    constructor(fullName: String, login: String, password: String, id: String){
        this.fullName = fullName
        this.login = login
        this.password = password
        this.id = id
    }
}
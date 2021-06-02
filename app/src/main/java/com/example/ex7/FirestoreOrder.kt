package com.example.ex7

data class FirestoreOrder(
    var id:String = "0",
    var comment: String = "",
    var status: String = "",
    var costumer_name: String = "",
    var pickles: Int = 0,
    var hummus: Boolean = true,
    var tahini: Boolean = false
)

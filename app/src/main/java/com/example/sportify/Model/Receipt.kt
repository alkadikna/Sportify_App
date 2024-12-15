package com.example.sportify.Model

data class Receipt(
    val orderId: String = "",
    val username: String = "",
    val orderTime: String = "",
    val totalAmount: Double = 0.0,
    val items: List<ReceiptItem> = listOf()
)

data class ReceiptItem(
    val name: String = "",
    val schedule: String = "",
    val price: Double = 0.0
)

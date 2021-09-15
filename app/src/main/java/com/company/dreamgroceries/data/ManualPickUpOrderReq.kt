package com.company.dreamgroceries.data

import com.company.dreamgroceries.utilities.ORDER_MANUAL_PICKUP
import com.company.dreamgroceries.utilities.PAYMENT_COD

data class ManualPickUpOrderReq(
    val order_type: Int = ORDER_MANUAL_PICKUP,
    val payment_type: Int = PAYMENT_COD,
    val additional_instructions: String = "",
    val pickup_date : String,
    val products : Array<String>
)

package com.fameget.dreamgroceries.data

import com.fameget.dreamgroceries.utilities.ORDER_MANUAL_DELIVERY
import com.fameget.dreamgroceries.utilities.ORDER_MANUAL_PICKUP
import com.fameget.dreamgroceries.utilities.PAYMENT_COD

data class ManualDelOrderReq(
    val order_type: Int = ORDER_MANUAL_DELIVERY,
    val payment_type: Int = PAYMENT_COD,
    val additional_instructions: String = "",
    val address_id : Int,
    val products : Array<String>
)

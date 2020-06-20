package com.fameget.dreamgroceries.data

import com.google.gson.JsonArray

data class ProductsRequest(
    var category_id: IntArray,
    var page: Int = 1,
    var search: String = ""
) {

}
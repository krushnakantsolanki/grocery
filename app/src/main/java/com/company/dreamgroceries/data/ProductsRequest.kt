package com.company.dreamgroceries.data

data class ProductsRequest(
    var category_id: IntArray,
    var page: Int = 1,
    var search: String = ""
) {

}
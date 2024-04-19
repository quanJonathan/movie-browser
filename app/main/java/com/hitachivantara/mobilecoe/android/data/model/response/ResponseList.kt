package com.hitachivantara.mobilecoe.android.data.model.response

import com.hitachivantara.mobilecoe.android.data.model.Cast

class ResponseList<T> {
    var page: Int = 1
    var total_pages: Int = 1
    var total_results: Int = 1
    var results: List<T> = emptyList()
}

class CastResponseList {
    var id: Int = 1
    var cast: List<Cast> = emptyList()
    var crew: List<Cast> = emptyList()
}
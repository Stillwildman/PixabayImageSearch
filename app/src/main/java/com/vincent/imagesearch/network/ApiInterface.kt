package com.vincent.imagesearch.network

import com.vincent.imagesearch.model.ItemImageResult
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Vincent on 2020/11/17.
 */
interface ApiInterface {

    @GET("api")
    fun searchImages(
        @Query("key") key: String,
        @Query("q") keyWords: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int): Observable<Response<ItemImageResult>>

}
package edu.festu.numberfacts.network

import edu.festu.numberfacts.model.Fact
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * API для получения фактов. Получаются в JSON-формате.
 * Может быть расширен для масштабирования приложения.
*/
interface ApiService {
    @Headers("Content-Type: application/json")
    @GET("random/trivia")
    fun getNumberFactAsync(): Call<Fact>
}
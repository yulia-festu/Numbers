package edu.festu.numberfacts.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



/*
* Singleton для работы с сетью. В данном приложении не требуется создавать несколько таких объектов.
* */
    object NetworkObject {
        private lateinit var r:Retrofit
        fun get():Retrofit {
            return if (::r.isInitialized) r
            else {
                r =  Retrofit.Builder().baseUrl("http://numbersapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                r
            }
        }
    }


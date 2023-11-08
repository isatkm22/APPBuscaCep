package com.example.buscacep.data.api

import com.example.buscacep.data.model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("{cep}/json/")
    fun getAddress(@Path("cep") cep: String): Call<Address>
}

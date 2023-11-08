package com.example.buscacep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.buscacep.data.api.ViaCepService
import com.example.buscacep.data.model.Address
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viaCepService: ViaCepService
    private lateinit var editcep: EditText
    private lateinit var buttonbuscar: Button
    private lateinit var buttonlimpar: Button
    private lateinit var textresposta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editcep = findViewById(R.id.editcep)
        buttonbuscar = findViewById(R.id.buttonbuscar)
        buttonlimpar = findViewById(R.id.buttonlimpar)
        textresposta = findViewById(R.id.textresposta)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        viaCepService = retrofit.create(ViaCepService::class.java)


        buttonbuscar.setOnClickListener {
            val cep = editcep.text.toString()
            if (cep.isNotEmpty()) {
                val call = viaCepService.getAddress(cep)
                call.enqueue(object : Callback<Address> {
                    override fun onResponse(call: Call<Address>, response: Response<Address>) {
                        if (response.isSuccessful) {
                            val address = response.body()

                            if (address != null && !address.cep.isNullOrEmpty()) {
                                val resultText = "${address.logradouro}\n" +
                                        "${address.bairro}\n" +
                                        "${address.localidade}\n" +
                                        "${address.uf}"
                                textresposta.text = resultText
                            } else {
                                showToast("CEP inválido ")
                            }
                        } else {
                            Log.e("API Error", "Erro na chamada da API: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Address>, t: Throwable) {
                        Log.e("API Error", "Falha na chamada da API: ${t.message}")
                    }
                })
                //para o teclado sumir quando acionar o botão
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(editcep.windowToken, 0)
            }
        }

        buttonlimpar.setOnClickListener {
            editcep.text.clear()
            textresposta.text = ""

            // Limpar qualquer outra ação adicional que você queira realizar
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

}

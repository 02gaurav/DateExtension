package com.gaurav.dateextension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.LinkedList

fun main() {
    makeApiCall("https://fake-json-api.mock.beeceptor.com/companies")
}
data class Response(val list:List<Companies>)
data class Companies(val id:Int, val name:String, val address:String, val ceoName :String)

private fun makeApiCall(url:String) {
    try {
        val httpUrl = URL(url)
        val connection = httpUrl.openConnection() as HttpURLConnection
        // Set request method
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type","application/json")
        connection.setRequestProperty("Accept","application/json")
        connection.connect()
        val responseCode = connection.responseCode
        println("Response code  =$responseCode")
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.use {
            //println("text === ${it.readText()}")
            it.readText()
        }
        println("Response == ${reader.toString()} === ")
        val gson = Gson()
        val listType = object : TypeToken<List<Companies>>() {}.type
        val data :List<Companies> = gson.fromJson(response,listType)
        println("Here data === $data ==== ${data.size}")

    } catch (e:Exception) {
        println("Exception == $e")
    }

}
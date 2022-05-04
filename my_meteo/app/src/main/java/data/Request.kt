package data

import java.net.URL


class Request(private val url: String) {

    fun run() : String{
        return try {
            URL(url).readText()
        } catch (ex : Exception) {
            ""
        }
    }
}
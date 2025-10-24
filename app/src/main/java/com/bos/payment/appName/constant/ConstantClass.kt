package com.bos.payment.appName.constant

import java.util.Random

object ConstantClass {
    const val BASE_URL_LOGIN = "https://www.aepsmerchant.in/api/"
    const val BASE_URL = "https://api.bos.center/"
   //  const val BASE_URL = "https://api.boscenter.in/api/BOS/"
   //const val BASE_URL = "http://www.aepsmerchant.in/api/"
    const val api_response_default_Message = "Something Went Wrong!"
    val Success = "Success"

    var latdouble = 0.0
    var longdouble = 0.0

    fun generateRandomNumber(): String {
        val random = Random()
        val uniqueNumbers = HashSet<String>()

        while (uniqueNumbers.size < 8) {
            val randomNumber = random.nextInt(90000000) + 10000000
            uniqueNumbers.add(randomNumber.toString())
        }

        return uniqueNumbers.first()
    }


}
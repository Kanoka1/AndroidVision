package com.example.marinab.testapplication

import android.os.Build
import android.support.annotation.RequiresApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient



class HttpRequest {


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun pushPostRquest(filePath: String){
        val serverUrl: String = "http://192.168.222.45:1231/api"
       // val serverUrl: String = "http://192.168.222.45:1231/api/values"
        val url = URL(serverUrl)
        val connection = url.openConnection() as HttpURLConnection
        val charset = "UTF-8"
        val LINE_FEED = "\r\n"
        val boundary: String = "===" + System.currentTimeMillis() + "==="

        connection.requestMethod = "POST"
        //connection.connectTimeout = 300000
        connection.doInput = true
        connection.doOutput = true
        connection.setChunkedStreamingMode(8192)

        //val gson = Gson()
        // val message2 = gson.toJson(message)
        // val postData : ByteArray = message2.toByteArray(StandardCharsets.UTF_8)
        connection.setRequestProperty("charset", "utf-8")
        connection.setRequestProperty("Connection", "Keep-Alive")
        connection.setRequestProperty("Cache-Control", "no-cache")
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary)

        val outputStream: DataOutputStream = DataOutputStream(connection.outputStream)
        val writer = PrintWriter(outputStream, true)

        writer.append("--").append(boundary).append(LINE_FEED)
        writer.append("Content-Disposition: form-data; name=\"File\"")
                .append("filename=\"test.jpg\"").append(LINE_FEED)
        writer.append("Content-Type: \"image/jpg\"").append(LINE_FEED)
        writer.append(LINE_FEED)

        val inputStream = FileInputStream(filePath)
        try {
            var buffer = ByteArray(8192)
            var data = 0
            while (data != -1) {
                data = inputStream.read(buffer)
                outputStream.write(buffer)
            }
        } catch (exception: Exception) {
            val ex: String = exception.message!!
        } finally {
            outputStream.flush()
            writer.flush()
        }
        if (connection.responseCode != HttpURLConnection.HTTP_OK && connection.responseCode != HttpURLConnection.HTTP_CREATED) {
            try {
                val reader: BufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
                val output: String = reader.readLine()

            } catch (exception: Exception) {
                val ex : String = exception.message!!
            }
        }


    }

    fun pushPostRquest2(file: File) {


        val serverUrl: String = "http://192.168.222.45:1231"
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(2000, TimeUnit.SECONDS)
                .writeTimeout(2000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(serverUrl)
                .build()

        val service = retrofit.create(RetrofitInterface::class.java)

                //val ss =  service.uploadUserAvatar(image,"asdf")

        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile)
        val name = RequestBody.create(MediaType.parse("text/plain"), "upload_test")

        val req = service.uploadUserAvatar(body, name)
        req.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {

                if (response != null && response.isSuccessful) {

                } else {

                }


            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {

            }
        })

           }
}
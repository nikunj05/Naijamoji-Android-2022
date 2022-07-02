package com.naijamojiapp.app.webservice

import android.util.Log
import com.google.gson.GsonBuilder
import com.naijamojiapp.app.utils.Preferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class WebService {
    init {
        val httpClient = OkHttpClient.Builder()
        httpClient.interceptors().add(Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                     .header("Accept", "application/json")
                //    .header("Content-Type", "application/x-www-form-urlencoded")
                     .header("Authorization", "Bearer " + Preferences.INSTANCE!!.token)
//                     .header("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjEyMGRhYTI1Y2RmMDNlNzI2MjJjMzliZDI4YzJkMWMxMDhmOTk0NGM5MzI5ZTUyNTBiZTVlNGI3MmNiOTZiODNlM2NjNmVhMmE4NjhmNDI0In0.eyJhdWQiOiIxIiwianRpIjoiMTIwZGFhMjVjZGYwM2U3MjYyMmMzOWJkMjhjMmQxYzEwOGY5OTQ0YzkzMjllNTI1MGJlNWU0YjcyY2I5NmI4M2UzY2M2ZWEyYTg2OGY0MjQiLCJpYXQiOjE1NjY5ODU4OTksIm5iZiI6MTU2Njk4NTg5OSwiZXhwIjoxNTk4NjA4Mjk5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.a7YDJ9fqE5fNTeiD8hQMs5-D9RBdUK5SlzWCxcXNR0HGAVP1y1D6QKaSu7eMV9KkMVi94tRecLcmJ3xb0tHQfif6G3GGzvEwRBUhVexx01wJORnrsYsd3Az8gthCDhgYe9IgmzvtW2OiI6TFJQotLmvZNUbUFe34CAlinkkICIORdsEmYzSrzWtdXCiy4bkW1pRVNytVPDi7CI4TmQN6mycE2_ck2TPTnF9WdB9cB5mHn-W2lhyE2sf0gyToQAVNZSWAxmiOAoBpV8aevDe-qvd7fl_dV2S5X6rK3OqTeyvmRsHtq8uI6lSg0NzxoM9YvNJz8WOmCCMerApZEW6s26jjsOZ0xNApjUYXpgmRd-77pfONrePKPqjHMkFFvzMj6w5WpE3E9Q6rT0wRnONZNzZM8F8COvZ0Q3M9wpGJp9DtuVAbM1vN7O2ASHnxnZ-mqpLvvGBlGgnZxb8OsGduxfoDSRWkPWAFKBp9OS_WqZqn7hxyUeiQxlmw33d-XcBrLlw3inIXWTXJ4iQfEN8roJBburBjE256_ySm40Na6oQuMC8BlPBEPiDBNdkUdnKsCpMbAveKoIVHQz-5OPVR9sp7vnY8qn31A2DhsZ-lrph7n-GcT9AmtYVfpO5QsK2ZE3tIavplcWqrbel5J3_Dg_qHbLIXvxyzXPRFVJDPCSA")
//                     .header("Authorization", "bearer " + Preferences.INSTANCE?.preToken)
                    .method(original.method(), original.body())
                    .build()
            Log.i("tokan", "===>"+Preferences.INSTANCE?.token)
// Customize or return the response
            chain.proceed(request)
        }
        )
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val client = httpClient.build()
    }

    companion object {

        var INSTANCE: WebService? = null

        val instance: WebService
            get() {
                if (INSTANCE == null)
                    INSTANCE = WebService()
                return INSTANCE!!
            }

        // Adding an Network Interceptor for Debugging purpose :
        // Customize the request
        //.header("Authorization", "bearer " + Preferences.getInstance().getToken())
        // Customize or return the response
        val applicationJsonInterceptor: Interceptor
            get() = Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                        .header("Accept", "application/json")
                        //.header("Content-Type", "application/x-www-form-urlencoded")
                        .header("Authorization", "Bearer " + Preferences.INSTANCE!!.token)
//                        .header("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjEyMGRhYTI1Y2RmMDNlNzI2MjJjMzliZDI4YzJkMWMxMDhmOTk0NGM5MzI5ZTUyNTBiZTVlNGI3MmNiOTZiODNlM2NjNmVhMmE4NjhmNDI0In0.eyJhdWQiOiIxIiwianRpIjoiMTIwZGFhMjVjZGYwM2U3MjYyMmMzOWJkMjhjMmQxYzEwOGY5OTQ0YzkzMjllNTI1MGJlNWU0YjcyY2I5NmI4M2UzY2M2ZWEyYTg2OGY0MjQiLCJpYXQiOjE1NjY5ODU4OTksIm5iZiI6MTU2Njk4NTg5OSwiZXhwIjoxNTk4NjA4Mjk5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.a7YDJ9fqE5fNTeiD8hQMs5-D9RBdUK5SlzWCxcXNR0HGAVP1y1D6QKaSu7eMV9KkMVi94tRecLcmJ3xb0tHQfif6G3GGzvEwRBUhVexx01wJORnrsYsd3Az8gthCDhgYe9IgmzvtW2OiI6TFJQotLmvZNUbUFe34CAlinkkICIORdsEmYzSrzWtdXCiy4bkW1pRVNytVPDi7CI4TmQN6mycE2_ck2TPTnF9WdB9cB5mHn-W2lhyE2sf0gyToQAVNZSWAxmiOAoBpV8aevDe-qvd7fl_dV2S5X6rK3OqTeyvmRsHtq8uI6lSg0NzxoM9YvNJz8WOmCCMerApZEW6s26jjsOZ0xNApjUYXpgmRd-77pfONrePKPqjHMkFFvzMj6w5WpE3E9Q6rT0wRnONZNzZM8F8COvZ0Q3M9wpGJp9DtuVAbM1vN7O2ASHnxnZ-mqpLvvGBlGgnZxb8OsGduxfoDSRWkPWAFKBp9OS_WqZqn7hxyUeiQxlmw33d-XcBrLlw3inIXWTXJ4iQfEN8roJBburBjE256_ySm40Na6oQuMC8BlPBEPiDBNdkUdnKsCpMbAveKoIVHQz-5OPVR9sp7vnY8qn31A2DhsZ-lrph7n-GcT9AmtYVfpO5QsK2ZE3tIavplcWqrbel5J3_Dg_qHbLIXvxyzXPRFVJDPCSA")
                        .method(original.method(), original.body())
                        .build()
                Log.i("tokan", "===>"+Preferences.INSTANCE?.token)
                chain.proceed(request)
            }

        val okHttpClient: OkHttpClient
            get() = OkHttpClient().newBuilder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(3000, TimeUnit.SECONDS)
                    .readTimeout(3000, TimeUnit.SECONDS)
                    .writeTimeout(3000, TimeUnit.SECONDS)
                    .addNetworkInterceptor(applicationJsonInterceptor)
                    .build()
    }
}

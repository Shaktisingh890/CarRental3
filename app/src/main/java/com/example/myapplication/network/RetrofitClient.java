package com.example.myapplication.network;


import android.content.Context;

import com.example.myapplication.utils.SharedPreferencesManager;

import okhttp3.OkHttpClient;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    // Static method to return Retrofit instance
    public static Retrofit getRetrofitInstance(Context context) {


        // Create the logging interceptor for logging HTTP request and response bodies
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Log the full body of requests and responses

        // Create OkHttpClient and add the logging interceptor and the auth interceptor
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)  // Connection timeout
                .readTimeout(40, TimeUnit.SECONDS)     // Read timeout
                .writeTimeout(40, TimeUnit.SECONDS)    // Write timeout

                .addInterceptor(loggingInterceptor)  // Add the logging interceptor to OkHttpClient
                .addInterceptor(new AuthInterceptor(context)) // Add the AuthInterceptor for adding authorization header
                .build();

        // Create and return the Retrofit instance
        return new Retrofit.Builder()
                .baseUrl("http://192.168.1.21:3000") // Base URL for the API
                .client(okHttpClient)               // Use the OkHttpClient with logging and auth interceptor
                .addConverterFactory(GsonConverterFactory.create()) // Convert JSON to Java objects using Gson
                .build();
    }

    // AuthInterceptor class to add Authorization header for every request
    public static class AuthInterceptor implements Interceptor {
        private Context context;

        public AuthInterceptor(Context context) {
            this.context = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            // Retrieve the access token from SharedPreferences
            String accessToken = SharedPreferencesManager.getAccessToken(context);

            // If an access token is available, add it to the request header
            if (accessToken != null) {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();
                return chain.proceed(newRequest);
            }

            // If no access token is available, proceed with the original request
            return chain.proceed(chain.request());
        }
    }


}

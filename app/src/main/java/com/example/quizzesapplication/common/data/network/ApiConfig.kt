package com.example.quizzesapplication.common.data.network

/**
 * Base URL is defined using Android Emulator [Network Configuration](https://developer.android.com/studio/run/emulator-networking.html)
 *
 */
object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:8889/api/" // to fetch server running on local machine, hosting both server and emulator
    const val AUTH_BASE_PATH = "auth"
}
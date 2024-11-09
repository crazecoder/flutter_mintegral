package com.crazecoder.flutter.mintegral.utils

import io.flutter.BuildConfig
import io.flutter.Log

class LogUtil {
    companion object {
        fun e(obj: Any, msg: String) {
            if (BuildConfig.DEBUG)
                Log.e(obj::class.simpleName ?: "", msg)
        }

        fun i(obj: Any, msg: String) {
            if (BuildConfig.DEBUG)
                Log.i(obj::class.simpleName ?: "", msg)
        }

        fun d(obj: Any, msg: String) {
            if (BuildConfig.DEBUG)
                Log.d(obj::class.simpleName ?: "", msg)
        }

        fun w(obj: Any, msg: String) {
            if (BuildConfig.DEBUG)
                Log.w(obj::class.simpleName ?: "", msg)
        }

        fun wtf(obj: Any, msg: String) {
            if (BuildConfig.DEBUG)
                Log.wtf(obj::class.simpleName ?: "", msg)
        }

        fun v(obj: Any, msg: String) {
            if (BuildConfig.DEBUG)
                Log.v(obj::class.simpleName ?: "", msg)
        }
    }
}
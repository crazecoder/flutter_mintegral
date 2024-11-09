package com.crazecoder.flutter.mintegral.flutter_mintegral_example

import com.crazecoder.flutter.mintegral.activity.SplashAdAbstractActivity


class SplashActivity : SplashAdAbstractActivity() {
    override fun getAppId(): String? {
        return "118690"
    }

    override fun getAppKey(): String? {
        return "7c22942b749fe6a6e361b675e96b3ee9"
    }

    override fun getAdUnitId(): String? {
        return "209547"
    }

    override val adPlacementId: String
        get() = "173349"

    override fun isBidding(): Boolean? = null

    override val launchBackground: Int? = null
}

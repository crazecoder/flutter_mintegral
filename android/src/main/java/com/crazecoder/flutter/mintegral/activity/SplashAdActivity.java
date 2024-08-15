package com.crazecoder.flutter.mintegral.activity;

public class SplashAdActivity extends SplashAdAbstractActivity {
    @Override
    protected boolean isCheckPermission(){
        return true;
    }

    @Override
    protected String getAppId() {
        return null;
    }

    @Override
    protected String getAppKey() {
        return null;
    }

    @Override
    protected String getAdUnitId() {
        return null;
    }

    @Override
    protected String getAdPlacementId() {
        return null;
    }

    @Override
    protected boolean isProtectGDPR() {
        return false;
    }

    @Override
    protected boolean isProtectCCPA() {
        return false;
    }

    @Override
    protected Integer getLaunchBackground() {
        return null;
    }
}

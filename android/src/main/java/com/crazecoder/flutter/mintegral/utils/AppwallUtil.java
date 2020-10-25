//package com.crazecoder.flutter.mintegral.utils;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.pm.ActivityInfo;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.View;
//
//import androidx.annotation.DrawableRes;
//
//import com.crazecoder.flutter.mintegral.R;
//import com.mintegral.msdk.MIntegralConstans;
//import com.mintegral.msdk.MIntegralSDK;
//import com.mintegral.msdk.out.MIntegralSDKFactory;
//import com.mintegral.msdk.out.MtgWallHandler;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class AppwallUtil {
//    private static final String TAG = "AppwallUtil";
//    private Activity activity;
//    private static AppwallUtil instance;
//    private MtgWallHandler mtgHandler;
//    private Map<String, Object> properties;
//
//
//    private AppwallUtil() {
//    }
//
//    public static AppwallUtil getInstance() {
//        if (instance == null) {
//            instance = new AppwallUtil();
//        }
//        return instance;
//    }
//    public AppwallUtil setActivity(Activity activity){
//        this.activity = activity;
//        return instance;
//    }
//    /**
//            * Choose a way you can
//	 */
//    private void setAppWallTitleLogo(@DrawableRes int logoId) {
//        // 1.use bitmap as the logo
//        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), logoId);
//        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_LOGO, bitmap);
//        // 2.user drawable resId as the logo
//        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_LOGO_ID, logoId);
//    }
//
//    /**
//     * Choose a way you can
//     */
////    private void setAppWallTitleBackgroud() {
////
////        // 1.use bitmap as the appwall title
////        Bitmap titleBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.customer_bg);
////        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_BACKGROUND, titleBitmap);
////        // 2.use color resId as the appwall title
////        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_BACKGROUND_COLOR, R.color.mintegral_green);
////        // 3.use drawable resid as the appwall title
////        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_BACKGROUND_ID, R.drawable.ic_launcher);
////
////    }
//
//    private void setAppWallMainBackgroudColor() {
//        // wall main background must be color
//        properties.put(MIntegralConstans.PROPERTIES_WALL_MAIN_BACKGROUND_ID, R.color.mintegral_bg_main);
//    }
//
//    private void setAppWallTabBackgroud() {
//        // wall tab background must be color
//        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_BACKGROUND_ID, R.color.mintegral_bg_main);
//    }
//
//    private void setAppWallTabIndicateLineBackgroudColor() {
//        // wall tab indicater line must be color
//        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_INDICATE_LINE_BACKGROUND_ID,
//                R.color.mintegral_wall_tab_line);
//    }
//
////    private void setAppWallButtonBackgroudColor() {
////        // wall button color must be drawable
////        properties.put(MIntegralConstans.PROPERTIES_WALL_BUTTON_BACKGROUND_ID, R.drawable.mintegral_shape_btn);
////    }
//
////    private void setAppWallLoaddingResId() {
////        // wall loadding view
////        properties.put(MIntegralConstans.PROPERTIES_WALL_LOAD_ID, R.layout.mintegral_demo_wall_click_loading);
////    }
//
//    private void setAppWallStatusColor() {
//        properties.put(MIntegralConstans.PROPERTIES_WALL_STATUS_COLOR, R.color.mintegral_green);
//    }
//
//    private void setAppWallNavigationColor() {
//        properties.put(MIntegralConstans.PROPERTIES_WALL_NAVIGATION_COLOR, R.color.mintegral_green);
//    }
//
//    private void setAppWallConfigChanes() {
//        properties.put(MIntegralConstans.PROPERTIES_WALL_CONFIGCHANGES, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//    }
//
//    private void setAppWallTabColorByHexColorCode() {
//        // set the wall tab color of selected and unselected text by hex color
//        // codes
//        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_SELECTED_TEXT_COLOR, "#ff7900");
//        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_UNSELECTED_TEXT_COLOR, "#ffaa00");
//
//    }
//
//    @SuppressLint("InflateParams")
//    public void loadHandler() {
//        //properties = MtgWallHandler.getWallProperties(mAppWallUnitId);
//
//        properties = MtgWallHandler.getWallProperties("138784", mAppWallUnitId);
//
////		properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_SHAPE_COLOR,R.color.mintegral_demo_green);
////		properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_SHAPE_HEIGHT,20);
//        // setAppWallTitleLogo();
//        // setAppWallTitleBackgroud();
//        // setAppWallButtonBackgroudColor();
//        // setAppWallLoaddingResId();
//        // setAppWallMainBackgroudColor();
//        // setAppWallTabBackgroud();
//        // setAppWallTabIndicateLineBackgroudColor();
//        // setAppWallStatusColor();
//        // setAppWallNavigationColor();
//        // setAppWallTabColorByHexColorCode();
//        // nat for the click event viewGroup
//        mtgHandler = new MtgWallHandler(properties, this, nat);
//        // customer entry layout begin The part of the code can not
//        View view = activity.getLayoutInflater().inflate(R.layout.customer_entry, null);
//        view.findViewById(R.id.imageview).setTag(MIntegralConstans.WALL_ENTRY_ID_IMAGEVIEW_IMAGE);
//        view.findViewById(R.id.newtip_area).setTag(MIntegralConstans.WALL_ENTRY_ID_VIEWGROUP_NEWTIP);
//        mtgHandler.setHandlerCustomerLayout(view);
//        // customer entry layout end */
//        mtgHandler.load();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_intent:
//                openWall();
//                break;
//
//            case R.id.btn_preappwall:
//                preloadWall();
//                break;
//        }
//    }
//
//    /**
//     * Preloading the appwall can improve the revenue for you.
//     */
//    public void preloadWall() {
//        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
//        Map<String, Object> preloadMap = new HashMap<String, Object>();
//        preloadMap.put(MIntegralConstans.PROPERTIES_LAYOUT_TYPE, MIntegralConstans.LAYOUT_APPWALL);
//        preloadMap.put(MIntegralConstans.PROPERTIES_UNIT_ID, mAppWallUnitId);
//        preloadMap.put(MIntegralConstans.PLACEMENT_ID, "138784");
//        sdk.preload(preloadMap);
//    }
//
//    /**
//     * open the appwall via intent
//     */
//    public void openWall() {
//        try {
//            Map<String, Object> properties = MtgWallHandler.getWallProperties("138784", mAppWallUnitId);
//            MtgWallHandler mtgHandler = new MtgWallHandler(properties, AppwallActivity.this);
//            mtgHandler.startWall();
//        } catch (Exception e) {
//            Log.e("MTGActivity", "", e);
//        }
//    }
//
//}

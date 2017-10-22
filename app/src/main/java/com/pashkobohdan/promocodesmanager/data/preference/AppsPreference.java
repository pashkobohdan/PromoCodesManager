package com.pashkobohdan.promocodesmanager.data.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.pashkobohdan.promocodesmanager.data.schema.AppDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppsPreference {

    private static final String APPS_PREFERENCE_KEY = "AppsPrefenrenceKey";
    private static final String APPS_DTO_SET_KEY = "AppsDtoSetKey";
    private static final String APP_DTO_COUPON_LIST_SET_KEY = "AppDtoCouponListKey";

    private static SharedPreferences sharedPreferences;

    private AppsPreference() {
        //Utility class
    }

    public static List<AppDTO> getAllAppList(@NonNull Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(APPS_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }

        Set<String> appsSet = sharedPreferences.getStringSet(APPS_DTO_SET_KEY, new HashSet<String>());
        List<AppDTO> appDTOList = new ArrayList<>();
        for (String appName : appsSet) {
            appDTOList.add(new AppDTO(appName));
        }

        return appDTOList;
    }

    public static boolean addAppDTO(@NonNull AppDTO appDTO, @NonNull Context context) {
        List<AppDTO> appDTOList = getAllAppList(context);

        if (appDTOList.contains(appDTO)) {
            return false;
        }
        appDTOList.add(appDTO);

        saveAppDTOList(appDTOList, context);
        return true;
    }

    private static void saveAppDTOList(List<AppDTO> list, @NonNull Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(APPS_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }

        Set<String> appNameSet = new HashSet<>();
        for (AppDTO appDTO : list) {
            appNameSet.add(appDTO.getName());
        }

        sharedPreferences.edit().putStringSet(APPS_DTO_SET_KEY, appNameSet).apply();
    }

    public static boolean deleteApp(@NonNull Context context, @NonNull AppDTO appDTO) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(APPS_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }

        List<String> appList = new ArrayList<>(sharedPreferences.getStringSet(APPS_PREFERENCE_KEY, new HashSet<String>()));
        appList.remove(appDTO.getName());

        List<AppDTO> appDTOList = new ArrayList<>();
        for (String appName : appList) {
            appDTOList.add(new AppDTO(appName));
        }
        saveAppDTOList(appDTOList, context);
        return true;
    }

    /**
     * Coupons context
     * @param context
     * @param appName
     * @return
     */

    public static List<String> getAllCouponsOfApp(@NonNull Context context, @NonNull String appName) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(APPS_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }

        Set<String> couponsSet = sharedPreferences.getStringSet(APP_DTO_COUPON_LIST_SET_KEY.concat(appName), new HashSet<String>());
        return new ArrayList<>(couponsSet);
    }

    private static void saveCouponsForApp(@NonNull Context context, @NonNull String appName, List<String> couponsList) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(APPS_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }

        Set<String> couponsSet = new HashSet<>(couponsList);
        sharedPreferences.edit().putStringSet(APP_DTO_COUPON_LIST_SET_KEY.concat(appName), couponsSet).apply();
    }

    public static void addCouponsForApp(@NonNull Context context, @NonNull String appName, List<String> couponsList) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(APPS_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }

        Set<String> couponsSet = new HashSet<>(getAllCouponsOfApp(context, appName));
        for(String newCoupon : couponsList) {
            couponsSet.add(newCoupon);
        }
        sharedPreferences.edit().putStringSet(APP_DTO_COUPON_LIST_SET_KEY.concat(appName), couponsSet).apply();
    }

    public static boolean deleteCouponsForApp(@NonNull Context context, @NonNull String appName, @NonNull String coupon) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(APPS_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }

        List<String> couponsList = new ArrayList<>(sharedPreferences.getStringSet(APP_DTO_COUPON_LIST_SET_KEY.concat(appName), new HashSet<String>()));
        couponsList.remove(coupon);

        saveCouponsForApp(context, appName, couponsList);
        return true;
    }
}

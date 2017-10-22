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
}

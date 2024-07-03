package com.grtteam.laxiba.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by oleh on 22.07.16.
 */
public class GsonHelper {
    private GsonHelper() {
    }

    public static Gson getGsonNewInstance(Class<?>... excludeItems) {
        GsonBuilder bld = new GsonBuilder();
        List<Class<?>> exclude = Arrays.asList(excludeItems);

        return bld
                .serializeNulls()
                .create();
    }

    private static void reg(GsonBuilder builder, List<Class<?>> excludeList, Class<?>
            clazz, Object typeAdapter) {
        if (!excludeList.contains(clazz)) {
            builder.registerTypeAdapter(clazz, typeAdapter);
        }
    }

}
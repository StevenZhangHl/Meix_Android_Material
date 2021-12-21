package com.meix_android_material.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * author steven
 * date 2021/12/20
 * description
 */
public class GsonUtil {
    private static Gson _gson = new Gson();

    public static Gson getGson() {
        return _gson;
    }

    public static String obj2Json(Object obj) {
        try {
            return _gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> T json2Obj(String json, Class<T> c) {
        try {
            if (json == null || TextUtils.isEmpty(json.trim())) {
                return null;
            }
            return _gson.fromJson(json, c);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public <T> ArrayList<T> json2List(String str, final Class<T> c) {
        try {
            if (str == null || TextUtils.isEmpty(str.trim())) {
                return null;
            }
            JsonArray array = new JsonParser().parse(str).getAsJsonArray();
            return json2List(array, c);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public <T> ArrayList<T> json2List(JsonArray array, final Class<T> c) {
        try {
            if (array == null) {
                return null;
            }
            ArrayList<T> list = new ArrayList<T>();
            for (final JsonElement elem : array) {
                list.add(_gson.fromJson(elem, c));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> c, Class clazz) {
        try {
            Type objectType = type(c, clazz);
            return _gson.fromJson(json, objectType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson(Object obj, Class clazz, Class... fieldClazz) {
        try {
            Type objectType = type(clazz, fieldClazz);
            return _gson.toJson(obj, objectType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static public ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}

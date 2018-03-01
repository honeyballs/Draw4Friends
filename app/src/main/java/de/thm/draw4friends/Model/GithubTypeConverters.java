package de.thm.draw4friends.Model;

/**
 * Created by yannikstenzel on 01.03.18.
 */

// https://medium.com/@toddcookevt/android-room-storing-lists-of-objects-766cca57e3f9

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class GithubTypeConverters {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<PaintCommand> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<PaintCommand>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<PaintCommand> someObjects) {
        return gson.toJson(someObjects);
    }
}
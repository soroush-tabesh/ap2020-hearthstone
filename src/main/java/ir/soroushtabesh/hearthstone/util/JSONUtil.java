package ir.soroushtabesh.hearthstone.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {
    private static class AnnotationExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(Exclude.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static final Gson gson = new GsonBuilder()
            .setExclusionStrategies(new AnnotationExclusionStrategy())
            .create();

    public static Gson getGson() {
        return gson;
    }
}

package ir.soroushtabesh.hearthstone.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import ir.soroushtabesh.hearthstone.controllers.CardManager;
import ir.soroushtabesh.hearthstone.controllers.game.GameAction;
import ir.soroushtabesh.hearthstone.controllers.game.viewmodels.GameObject;
import ir.soroushtabesh.hearthstone.models.Card;
import ir.soroushtabesh.hearthstone.models.Hero;
import ir.soroushtabesh.hearthstone.models.Permanent;
import javafx.beans.value.WritableValue;

import java.lang.reflect.Type;

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
//            .registerTypeHierarchyAdapter(Card.class, new PermanentAdapter<Card>())
//            .registerTypeHierarchyAdapter(Hero.class, new PermanentAdapter<Hero>())
//            .registerTypeHierarchyAdapter(InfoPassive.class, new PermanentAdapter<InfoPassive>())
            .registerTypeHierarchyAdapter(GameObject.class, new AbstractAdapter<GameObject>())
            .registerTypeHierarchyAdapter(GameAction.class, new AbstractAdapter<GameObject>())
            .registerTypeHierarchyAdapter(WritableValue.class, new PropertyAdapter<>())
            .enableComplexMapKeySerialization()
            .create();

    private static final Gson gson2 = new GsonBuilder()
            .setExclusionStrategies(new AnnotationExclusionStrategy())
//            .registerTypeHierarchyAdapter(Card.class, new PermanentAdapter<Card>())
//            .registerTypeHierarchyAdapter(Hero.class, new PermanentAdapter<Hero>())
//            .registerTypeHierarchyAdapter(InfoPassive.class, new PermanentAdapter<InfoPassive>())
            .registerTypeHierarchyAdapter(WritableValue.class, new PropertyAdapter<>())
            .enableComplexMapKeySerialization()
            .create();

    public static Gson getGson() {
        return gson;
    }

    static class PermanentAdapter<T extends Permanent> implements JsonSerializer<T>, JsonDeserializer<T> {

        @Override
        public final JsonElement serialize(final T object, final Type interfaceType
                , final JsonSerializationContext context) {
            final JsonObject member = new JsonObject();
            String t;
            if (object instanceof Card)
                t = "c";
            else if (object instanceof Hero)
                t = "h";
            else
                t = "i";
            member.addProperty("type", t);
            member.addProperty("id", object.getId());
            return member;
        }

        @Override
        public final T deserialize(final JsonElement elem, final Type interfaceType
                , final JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject member = (JsonObject) elem;
            String t = get(member, "type").getAsString();
            int id = get(member, "id").getAsInt();
            switch (t) {
                case "c":
                    return (T) CardManager.getInstance().getCardByID(id);
                case "h":
                    return (T) CardManager.getInstance().getHeroByID(id);
                case "i":
                    return (T) CardManager.getInstance().getPassiveByID(id);
                default:
            }
            return null;
        }

        private JsonElement get(final JsonObject wrapper, final String memberName) {
            final JsonElement elem = wrapper.get(memberName);
            if (elem == null) {
                throw new JsonParseException("no '" + memberName + "' member found in json file.");
            }
            return elem;
        }
    }

    static class AbstractAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

        @Override
        public final JsonElement serialize(final T object, final Type interfaceType, final JsonSerializationContext context) {
            final JsonObject member = new JsonObject();

            member.addProperty("type", object.getClass().getName());

            member.add("data", gson2.toJsonTree(object));

            return member;
        }

        @Override
        public final T deserialize(final JsonElement elem, final Type interfaceType, final JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject member = (JsonObject) elem;
            final JsonElement typeString = get(member, "type");
            final JsonElement data = get(member, "data");
            final Type actualType = typeForName(typeString);

            return gson2.fromJson(data, actualType);
        }

        private Type typeForName(final JsonElement typeElem) {
            try {
                return Class.forName(typeElem.getAsString());
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        private JsonElement get(final JsonObject wrapper, final String memberName) {
            final JsonElement elem = wrapper.get(memberName);
            if (elem == null)
                throw new JsonParseException("no '" + memberName + "' member found in json file: " + wrapper.toString());
            return elem;
        }

    }

    static class PropertyAdapter<T extends WritableValue> implements JsonSerializer<T>, JsonDeserializer<T> {

        @Override
        public final JsonElement serialize(final T object, final Type interfaceType, final JsonSerializationContext context) {
            final JsonObject member = new JsonObject();

            member.addProperty("name", "");
            if (object.getValue() != null)
                member.addProperty("type", object.getValue().getClass().getName());
            else
                member.addProperty("type", "");
            member.add("value", context.serialize(object.getValue()));

            return member;
        }

        @Override
        public final T deserialize(final JsonElement elem, final Type interfaceType, final JsonDeserializationContext context)
                throws JsonParseException {
            try {
                final JsonObject member = (JsonObject) elem;
                final JsonElement typeString = get(member, "type");

                WritableValue w = (WritableValue) TypeToken.of(interfaceType).getRawType().newInstance();
                if (typeString.getAsString().equals("")) {
                    return (T) w;
                }
                final JsonElement data = get(member, "value");
                final Type actualType = typeForName(typeString);
                w.setValue(context.deserialize(data, actualType));
                return (T) w;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private Type typeForName(final JsonElement typeElem) {
            try {
                return Class.forName(typeElem.getAsString());
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }
        }

        private JsonElement get(final JsonObject wrapper, final String memberName) {
            final JsonElement elem = wrapper.get(memberName);
            if (elem == null)
                throw new JsonParseException("no '" + memberName + "' member found in json file.");
            return elem;
        }

    }
}

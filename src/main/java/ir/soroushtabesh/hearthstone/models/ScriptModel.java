package ir.soroushtabesh.hearthstone.models;

import com.google.gson.Gson;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import ir.soroushtabesh.hearthstone.util.JSONUtil;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ScriptModel implements Serializable {

    private static final Gson gson;
    private static final long serialVersionUID = -2484337745463110831L;

    static {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Reflections reflections = new Reflections("scripts");
//        Set<Class<? extends GenericScript>> allClasses =
//                reflections.getSubTypesOf(GenericScript.class);
//        gsonBuilder.registerTypeAdapter(GenericScript.class, new InterfaceAdapter<GenericScript>());
//        allClasses.forEach(aClass -> reg(gsonBuilder, aClass));
//        gsonBuilder.registerTypeAdapter(SpellBehavior.class, new InterfaceAdapter<SpellBehavior>());
//        gsonBuilder.registerTypeAdapter(BattleCry.class, new InterfaceAdapter<BattleCry>());
//        gsonBuilder.registerTypeAdapter(RestoreHealthTarget.class, new InterfaceAdapter<RestoreHealthTarget>());
//        gsonBuilder.registerTypeAdapter(Object.class,new InterfaceAdapter<Object>());
        gson = JSONUtil.getGson();
    }
//
//    public static <T> void reg(GsonBuilder gsonBuilder, Class<T> tClass) {
//        gsonBuilder.registerTypeAdapter(tClass, new InterfaceAdapter<GenericScript>());
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String scriptClass;

    @Column(columnDefinition = "text")
    private String scriptData;

    public ScriptModel() {
        this(new GenericScript());
    }

    public ScriptModel(GenericScript genericScript) {
        scriptClass = genericScript.getClass().getName();
        scriptData = gson.toJson(genericScript);
    }

    public ScriptModel(Class<? extends GenericScript> scriptClass, String scriptData) {
        this.scriptClass = scriptClass.getName();
        this.scriptData = scriptData;
    }

    public GenericScript getScript(GameController gameController) {
        if (scriptClass == null || scriptData == null || scriptData.isEmpty())
            return null;
        GenericScript genericScript = null;
        try {
            Class<? extends GenericScript> aClass = (Class<? extends GenericScript>)
                    getClass().getClassLoader().loadClass(scriptClass);
            genericScript = gson.fromJson(scriptData, aClass);
            Class<? extends GenericScript> classOfT = (Class<? extends GenericScript>)
                    getClass().getClassLoader().loadClass(genericScript.getClz());
            genericScript = gson.fromJson(scriptData, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        genericScript.setGameController(gameController);
        return genericScript;
    }

    public String getScriptData() {
        return scriptData;
    }

    public void setScriptData(String scriptData) {
        this.scriptData = scriptData;
    }

    public String getScriptClass() {
        return scriptClass;
    }

    public void setScriptClass(Class<? extends GenericScript> scriptClass) {
        this.scriptClass = scriptClass.getName();
    }

    public void setScriptClass(String scriptClass) {
        this.scriptClass = scriptClass;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Script{" +
                "script_id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScriptModel that = (ScriptModel) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }
//
//    private static final class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
//        private final Gson gson = new GsonBuilder().create();
//
//        public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context) {
//            final JsonObject wrapper = new JsonObject();
//            wrapper.addProperty("type", object.getClass().getName());
////            if (object.getClass() == GenericScript.class)
//            wrapper.add("data", gson.toJsonTree(object));
////            else
////                wrapper.add("data", context.serialize(object));
//            return wrapper;
//        }
//
//        public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
//            final JsonObject wrapper = (JsonObject) elem;
//            final JsonElement typeName = get(wrapper, "type");
//            final JsonElement data = get(wrapper, "data");
//            final Type actualType = typeForName(typeName);
////            return context.deserialize(data, actualType);
//            return gson.fromJson(data, actualType);
//        }
//
//        private Type typeForName(final JsonElement typeElem) {
//            try {
//                return Class.forName(typeElem.getAsString());
//            } catch (ClassNotFoundException e) {
//                throw new JsonParseException(e);
//            }
//        }
//
//        private JsonElement get(final JsonObject wrapper, String memberName) {
//            final JsonElement elem = wrapper.get(memberName);
//            if (elem == null)
//                throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
//            return elem;
//        }
//    }
//
//    final static class StaticTypeAdapterFactory
//            implements TypeAdapterFactory {
//
//        private static final TypeAdapterFactory staticTypeAdapterFactory = new StaticTypeAdapterFactory();
//
//        private StaticTypeAdapterFactory() {
//        }
//
//        static TypeAdapterFactory getStaticTypeAdapterFactory() {
//            return staticTypeAdapterFactory;
//        }
//
//        @Override
//        public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
//            final Type type = typeToken.getType();
//            if (type.equals(Class.class)) {
//                @SuppressWarnings("unchecked") final TypeAdapter<T> castStaticTypeAdapter = (TypeAdapter<T>) StaticTypeAdapter.getStaticTypeAdapter(gson);
//                return castStaticTypeAdapter;
//            }
//            return null;
//        }
//
//    }
//
//    final static class StaticTypeAdapter<T>
//            extends TypeAdapter<Class<T>> {
//
//        private static final String TARGET_CLASS_PROPERTY = "___class";
//
//        private final Gson gson;
//
//        private StaticTypeAdapter(final Gson gson) {
//            this.gson = gson;
//        }
//
//        static <T> TypeAdapter<Class<T>> getStaticTypeAdapter(final Gson gson) {
//            return new StaticTypeAdapter<>(gson);
//        }
//
//        @Override
//        @SuppressWarnings("resource")
//        public void write(final JsonWriter out, final Class<T> value)
//                throws IOException {
//            try {
//                final Iterator<Field> iterator = Stream.of(value.getFields())
//                        .filter(f -> isStatic(f.getModifiers()))
//                        .iterator();
//                out.beginObject();
//                while (iterator.hasNext()) {
//                    final Field field = iterator.next();
//                    out.name(field.getName());
//                    field.setAccessible(true);
//                    final Object fieldValue = field.get(null);
//                    @SuppressWarnings({"unchecked", "rawtypes"}) final TypeAdapter<Object> adapter = (TypeAdapter) gson.getAdapter(field.getType());
//                    adapter.write(out, fieldValue);
//                }
//                out.name(TARGET_CLASS_PROPERTY);
//                out.value(value.getName());
//                out.endObject();
//            } catch (final IllegalAccessException ex) {
//                throw new IOException(ex);
//            }
//        }
//
//        @Override
//        public Class<T> read(final JsonReader in)
//                throws IOException {
//            try {
//                Class<?> type = null;
//                in.beginObject();
//                final Map<String, JsonElement> buffer = new HashMap<>();
//                while (in.peek() != END_OBJECT) {
//                    final String property = in.nextName();
//                    switch (property) {
//                        case TARGET_CLASS_PROPERTY:
//                            type = Class.forName(in.nextString());
//                            break;
//                        default:
//                            // buffer until the target class name is known
//                            if (type == null) {
//                                final TypeAdapter<JsonElement> adapter = gson.getAdapter(JsonElement.class);
//                                final JsonElement jsonElement = adapter.read(in);
//                                buffer.put(property, jsonElement);
//                            } else {
//                                // flush the buffer
//                                if (!buffer.isEmpty()) {
//                                    for (final Map.Entry<String, JsonElement> e : buffer.entrySet()) {
//                                        final Field field = type.getField(e.getKey());
//                                        final Object value = gson.getAdapter(field.getType()).read(in);
//                                        field.set(null, value);
//                                    }
//                                    buffer.clear();
//                                }
//                                final Field field = type.getField(property);
//                                if (isStatic(field.getModifiers())) {
//                                    final TypeAdapter<?> adapter = gson.getAdapter(field.getType());
//                                    final Object value = adapter.read(in);
//                                    field.set(null, value);
//                                }
//                            }
//                            break;
//                    }
//                }
//                in.endObject();
//                // flush the buffer
//                if (type != null && !buffer.isEmpty()) {
//                    for (final Map.Entry<String, JsonElement> e : buffer.entrySet()) {
//                        final Field field = type.getField(e.getKey());
//                        final Object value = gson.fromJson(e.getValue(), field.getType());
//                        field.set(null, value);
//                    }
//                    buffer.clear();
//                }
//                @SuppressWarnings({"unchecked", "rawtypes"}) final Class<T> castType = (Class) type;
//                return castType;
//            } catch (final ClassNotFoundException | NoSuchFieldException | IllegalAccessException ex) {
//                throw new IOException(ex);
//            }
//        }
//
//    }

}

package ir.soroushtabesh.hearthstone.models;

import com.google.gson.*;
import ir.soroushtabesh.hearthstone.controllers.game.GameController;
import ir.soroushtabesh.hearthstone.controllers.game.scripts.GenericScript;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.lang.reflect.Type;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ScriptModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Class<? extends GenericScript> scriptClass;

    @Column(columnDefinition = "text")
    private String scriptData;

    public ScriptModel() {
        this(new GenericScript());
    }

    public ScriptModel(GenericScript genericScript) {
        scriptClass = genericScript.getClass();
        scriptData = new GsonBuilder().create().toJson(genericScript);
    }

    public ScriptModel(Class<? extends GenericScript> scriptClass, String scriptData) {
        this.scriptClass = scriptClass;
        this.scriptData = scriptData;
    }

    public GenericScript getScript(GameController gameController) {
        if (scriptClass == null || scriptData == null || scriptData.isEmpty())
            return null;
        GenericScript genericScript = new GsonBuilder()
                .registerTypeAdapter(GenericScript.class, new JsonDeserializerWithInheritance<GenericScript>())
                .create().fromJson(scriptData, scriptClass);
        genericScript.setGameController(gameController);
        return genericScript;
    }

    public String getScriptData() {
        return scriptData;
    }

    public void setScriptData(String scriptData) {
        this.scriptData = scriptData;
    }

    public Class<? extends GenericScript> getScriptClass() {
        return scriptClass;
    }

    public void setScriptClass(Class<? extends GenericScript> scriptClass) {
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

    private static class JsonDeserializerWithInheritance<T> implements JsonDeserializer<T> {
        @Override
        public T deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get("type");

            String className = classNamePrimitive.getAsString();

            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e.getMessage());
            }
            return context.deserialize(jsonObject, clazz);
        }
    }
}

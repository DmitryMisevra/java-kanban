package server.adapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import helpers.Types;
import module.Subtask;
import java.util.Objects;

public class SubtaskAdapter extends TypeAdapter<Subtask> {

    /* адаптер для корректного чтения и записи Subtask */
    private final Gson gson = new Gson();

    @Override
    public void write(JsonWriter jsonWriter, Subtask subtask) {
        if (Objects.requireNonNull(subtask.getType()) == Types.SUBTASK) {
            gson.toJson(subtask, Subtask.class, jsonWriter);
        } else {
            throw new JsonSyntaxException("Неизвестный тип задачи");
        }
    }

    @Override
    public Subtask read(JsonReader jsonReader){
        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        if (type.equals("SUBTASK")) {
            return gson.fromJson(jsonElement, Subtask.class);
        } else {
            throw new JsonSyntaxException("Тип задачи не соответствует требуемому");
        }
    }
}

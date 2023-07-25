package server.adapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import helpers.Types;
import module.Epic;

import java.io.IOException;
import java.util.Objects;

public class EpicAdapter extends TypeAdapter<Epic> {

    /* адаптер для корректного чтения и записи Epic */
    private final Gson gson = new Gson();


    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        if (Objects.requireNonNull(epic.getType()) == Types.EPIC) {
            gson.toJson(epic, Epic.class, jsonWriter);
        } else {
            throw new JsonSyntaxException("Неизвестный тип задачи");
        }
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        if (type.equals("EPIC")) {
            return gson.fromJson(jsonElement, Epic.class);
        } else {
            throw new JsonSyntaxException("Тип задачи не соответствует требуемому");
        }
    }
}

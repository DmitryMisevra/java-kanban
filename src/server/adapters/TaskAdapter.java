package server.adapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import module.Epic;
import module.Subtask;
import module.Task;

import java.io.IOException;
import java.util.Objects;

import static helpers.Types.TASK;

public class TaskAdapter extends TypeAdapter<Task> {

    /* адаптер для корректного чтения и записи Task */
    private final Gson gson = new Gson();


    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        if (Objects.requireNonNull(task.getType()) == TASK) {
            gson.toJson(task, Task.class, jsonWriter);
        } else {
            throw new JsonSyntaxException("Неизвестный тип задачи");
        }
    }

    /* при чтении Task, которые в списках могут быть и Epic, и Subtask, важно корректно определять тип и возвращать
    корректный экземпляр */
    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        JsonElement jsonElement = JsonParser.parseReader(jsonReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        switch (type) {
            case "TASK":
                return gson.fromJson(jsonElement, Task.class);
            case "EPIC":
                return gson.fromJson(jsonElement, Epic.class);
            case "SUBTASK":
                return gson.fromJson(jsonElement, Subtask.class);
            default:
                throw new JsonSyntaxException("Неизвестный тип задачи");
        }
    }
}

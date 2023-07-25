package helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import module.Epic;
import module.Subtask;
import module.Task;
import server.adapters.EpicAdapter;
import server.adapters.SubtaskAdapter;
import server.adapters.TaskAdapter;
import service.*;

import java.io.IOException;

public class Managers {

    /* Вспомогательный класс, с помощью которого создаются объекты InMemoryTaskManager и InMemoryHistoryManager */

    /* метод getDefault() будет возвращать необходимый субкласс TaskManager. Пока по умолчанию поставил
     InMemoryTaskManager() */
    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static TaskManager getFIleBackedTasksManager(String path) {
        return new FileBackedTasksManager(path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
        gsonBuilder.registerTypeAdapter(Epic.class, new EpicAdapter());
        gsonBuilder.registerTypeAdapter(Subtask.class, new SubtaskAdapter());
        return gsonBuilder.create();
    }
}

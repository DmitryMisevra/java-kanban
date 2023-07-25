package helpers;

import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

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

    public static HistoryManager getDefaultHistory () {
        return new InMemoryHistoryManager();
    }
}

package helpers;

import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Managers {

    /* метод getDefault() будет возвращать необходимый субкласс TaskManager. Пока по умолчанию поставил
     InMemoryTaskManager() */
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory () {
        return new InMemoryHistoryManager();
    }
}

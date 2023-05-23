package service;

import module.Task;

import java.util.List;

public interface HistoryManager {

    /* Вспомогательный метод add() добавляет в список с историей просмотров
     последнюю вызванную задачу */
    void add(Task task);

    /* Вспомогательный метод remove() удаляет задачу из истории просмотра */
    void remove(int id);

    /* метод getHistory() возвращает список с историей просмотров последних 10 задач */
    List<Task> getHistory();
}

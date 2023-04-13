package service;

import module.Task;

import java.util.ArrayList;

public interface HistoryManager {

    /*Вспомогательный метод add() добавляет в список с историей просмотров
     последнюю вызванную задачу*/
    void add(Task task);

    // метод getHistory() возвращает список с историей просмотров последних 10 задач
    ArrayList<Task> getHistory();
}

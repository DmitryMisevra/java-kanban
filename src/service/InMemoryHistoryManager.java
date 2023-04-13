package service;

import module.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    // список history хранит историю 10 последних просмотренных задач
    private static ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > 10) {
            history.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }


}

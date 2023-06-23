package service;

import helpers.Managers;
import module.Epic;
import module.Subtask;
import module.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    Task taskOne;
    Task taskTwo;
    Epic epicOne;
    Epic epicTwo;
    Subtask subtaskOne;
    Subtask subtaskTwo;
    Subtask subtaskThree;


    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();

        taskOne = new Task("Задача №1", "Это простая задача");
        taskOne.setId(1);
        taskTwo = new Task("Задача №2", "Еще одна простая задача");
        taskTwo.setId(2);
        epicOne = new Epic("Эпик №1", "У этого эпика 3 подзадачи");
        epicOne.setId(3);
        epicTwo = new Epic("Эпик №2", "У этого эпика нет подзадач");
        epicTwo.setId(4);
        subtaskOne = new Subtask("Подзадача 1.1", "эта подзадача " +
                "принадлежит эпику №1", 3);
        subtaskOne.setId(5);
        subtaskTwo = new Subtask("Подзадача 1.2", "эта подзадача " +
                "принадлежит эпику №1", 3);
        subtaskTwo.setId(6);
        subtaskThree = new Subtask("Подзадача 1.3", "эта подзадача " +
                "принадлежит эпику №1", 3);
        subtaskThree.setId(7);

        historyManager.add(taskOne);
        historyManager.add(taskOne);
        historyManager.add(taskTwo);
        historyManager.add(taskTwo);
        historyManager.add(epicOne);
        historyManager.add(epicOne);
        historyManager.add(epicTwo);
        historyManager.add(epicTwo);
        historyManager.add(subtaskOne);
        historyManager.add(subtaskOne);
        historyManager.add(subtaskTwo);
        historyManager.add(subtaskTwo);
        historyManager.add(subtaskThree);
        historyManager.add(subtaskThree);

    }

    @Test
    void shouldDeleteDublicatesInHistoryList() {
        List<Task> historyList = historyManager.getHistory();
        assertNotNull(historyList, "история задач не найдена");
        assertEquals(1, Collections.frequency(historyList, taskOne), "есть дубликат задачи 1");
        assertEquals(1, Collections.frequency(historyList, taskTwo), "есть дубликат задачи 2");
    }

    @Test
    void shouldDeleteTasksAtTheBeginningOfHistoryList() {
        historyManager.remove(1);
        historyManager.remove(2);
        List<Task> historyList = historyManager.getHistory();

        assertNotNull(historyList, "история задач не найдена");
        assertFalse(historyList.contains(taskOne), "задача 1 осталась в истории просмотров");
        assertFalse(historyList.contains(taskTwo), "задача 2 осталась в истории просмотров");
        assertEquals(epicOne, historyList.get(0), "Эпик 1 должен стоять в начале истории");
    }

    @Test
    void shouldDeleteTasksInTheMiddleOfHistoryList() {
        historyManager.remove(4);
        historyManager.remove(5);
        List<Task> historyList = historyManager.getHistory();

        assertNotNull(historyList, "история задач не найдена");
        assertFalse(historyList.contains(epicTwo), "эпик 2 остался в истории просмотров");
        assertFalse(historyList.contains(subtaskOne), "подзадача 1 осталась в истории просмотров");
        assertEquals(subtaskTwo, historyList.get(3), "Подзадача 2 должна стоять в начале истории");
    }

    @Test
    void shouldDeleteTasksAtTheEndOfHistoryList() {
        historyManager.remove(6);
        historyManager.remove(7);
        List<Task> historyList = historyManager.getHistory();

        assertNotNull(historyList, "история задач не найдена");
        assertFalse(historyList.contains(subtaskTwo), "подзадача 2 осталась в истории просмотров");
        assertFalse(historyList.contains(subtaskThree), "подзадача 3 осталась в истории просмотров");
    }


    @Test
    void shouldCreateEpicWithSubtasksWithStartTime() {
        historyManager.remove(6);
        historyManager.remove(7);
        List<Task> historyList = historyManager.getHistory();

        assertNotNull(historyList, "история задач не найдена");
        assertFalse(historyList.contains(subtaskTwo), "подзадача 2 осталась в истории просмотров");
        assertFalse(historyList.contains(subtaskThree), "подзадача 3 осталась в истории просмотров");
    }
}
package service;

import client.KVTaskClient;
import com.google.gson.Gson;
import helpers.Managers;
import module.Epic;
import module.Subtask;
import module.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static service.HttpTaskManager.loadFromServer;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    static KVServer kvServer;
    HttpTaskManager managerOne;
    HttpTaskManager managerTwo;

    @BeforeAll
    static void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterAll
    static void afterAll() {
        kvServer.stop();
    }

    @Override
    protected HttpTaskManager createTaskManagerTest() {
        try {
            return new HttpTaskManager("http://localhost:8078");
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка при создании объекта менеджера: " + e.getMessage());
        }
    }

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        managerOne = new HttpTaskManager("http://localhost:8078");
    }

    @Test
    void shouldCreateHttpTaskManagerWithEmptyTasksList() {
        managerOne.clearTaskList();
        managerOne.clearSubTaskList();
        managerOne.clearEpicTaskList();

        managerTwo = loadFromServer("http://localhost:8078");
        assertTrue(managerTwo.tasks.isEmpty(), "список задач не пустой");
        assertTrue(managerTwo.epics.isEmpty(), "список эпиков не пустой");
        assertTrue(managerTwo.subtasks.isEmpty(), "список подзадач не пустой");
    }

    @Test
    void shouldCreateHttpTaskManagerWithEpicWithoutSubtasks() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        managerOne.createEpicTask(epic);
        int epicID = epic.getId();

        managerTwo = loadFromServer("http://localhost:8078");
        assertNotNull(managerTwo.getEpicTaskByID(epicID));
        assertEquals(1, epic.getId(), "id эпика не совпадает");
        assertEquals("Тестовый эпик", epic.getName(), "имя эпика не совпадает");
        assertEquals("Тестовое описание", epic.getDescription(), "описание эпика не совпадает");
        assertTrue(epic.getSubtasksID().isEmpty(), "список подзадач эпика не пустой");
    }

    @Test
    void shouldCreateHttpTaskManagerWithEmptyHistoryList() {
        managerOne.clearTaskList();
        managerOne.clearSubTaskList();
        managerOne.clearEpicTaskList();

        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        managerOne.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        managerOne.createSubtask(subtaskOne);
        managerOne.createSubtask(subtaskTwo);

        managerTwo = loadFromServer("http://localhost:8078");
        assertTrue(managerTwo.getHistory().isEmpty(), "список истории задач не пустой");
    }

    @Test
    void shouldCreateHttpTaskManagerWithFilledHistoryList() throws IOException, InterruptedException {

        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        Gson gson = Managers.getGson();

        Task taskOne = new Task("Задача №1", "Это простая задача");
        Task taskTwo = new Task("Задача №2", "Еще одна простая задача");
        Epic epicOne = new Epic("Эпик №1", "У этого эпика 3 подзадачи");
        Epic epicTwo = new Epic("Эпик №2", "У этого эпика нет подзадач");
        Subtask subtaskOne = new Subtask("Подзадача 1.1", "эта подзадача " +
                "принадлежит эпику №1", 3);
        Subtask subtaskTwo = new Subtask("Подзадача 1.2", "эта подзадача " +
                "принадлежит эпику №1", 3);
        Subtask subtaskThree = new Subtask("Подзадача 1.3", "эта подзадача " +
                "принадлежит эпику №1", 3);

        managerOne.createTask(taskOne);
        managerOne.createTask(taskTwo);
        managerOne.createEpicTask(epicOne);
        managerOne.createEpicTask(epicTwo);
        managerOne.createSubtask(subtaskOne);
        managerOne.createSubtask(subtaskTwo);
        managerOne.createSubtask(subtaskThree);

        managerOne.getTaskByID(1);
        managerOne.getTaskByID(2);
        managerOne.getEpicTaskByID(3);
        managerOne.getEpicTaskByID(4);
        managerOne.getSubtaskByID(5);
        managerOne.getSubtaskByID(6);
        managerOne.getSubtaskByID(7);

        String task = client.load("task");
        String epic = client.load("epic");
        String subtask = client.load("subtask");
        String history = client.load("history");

        System.out.println(task);
        System.out.println(epic);
        System.out.println(subtask);
        System.out.println(history);

        Task[] taskArray = gson.fromJson(client.load("task"), Task[].class);
        Epic[] epicArray = gson.fromJson(client.load("epic"), Epic[].class);
        Subtask[] subtaskArray = gson.fromJson(client.load("subtask"), Subtask[].class);

        managerTwo = loadFromServer("http://localhost:8078");
        List<Task> historyList = managerTwo.getHistory();

        assertFalse(historyList.isEmpty(), "список истории задач не пустой");
        assertEquals(7, historyList.size(), "размер истории просмотров не совпадает");
        assertEquals(taskOne, historyList.get(0), "Задача 1 не на своем месте");
        assertEquals(subtaskThree, historyList.get(6), "Подзадача 3 не на своем месте");
    }
}
package service;

import module.Epic;
import module.Subtask;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static service.FileBackedTasksManager.loadFromFile;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    protected FileBackedTasksManager createTaskManagerTest() {
        return new FileBackedTasksManager("src/data/tasks.csv");
    }

    @Test
    void shouldCreateFileBackedTaskManagerWithEmptyTasksList() {
        String path = "src/data/tasks.csv";
        FileBackedTasksManager managerOne = new FileBackedTasksManager(path);
        managerOne.clearTaskList();
        managerOne.clearSubTaskList();
        managerOne.clearEpicTaskList();

        FileBackedTasksManager managerTwo = loadFromFile(path);
        assertTrue(managerTwo.tasks.isEmpty(), "список задач не пустой");
        assertTrue(managerTwo.epics.isEmpty(), "список эпиков не пустой");
        assertTrue(managerTwo.subtasks.isEmpty(), "список подзадач не пустой");
    }

    @Test
    void shouldCreateFileBackedTaskManagerWithEpicWithoutSubtasks() {
        String path = "src/data/tasks.csv";
        FileBackedTasksManager managerOne = new FileBackedTasksManager(path);


        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        managerOne.createEpicTask(epic);
        int epicID = epic.getId();


        FileBackedTasksManager managerTwo = loadFromFile(path);
        assertNotNull(managerTwo.getEpicTaskByID(epicID));
        assertEquals(1, epic.getId(), "id эпика не совпадает");
        assertEquals("Тестовый эпик", epic.getName(), "имя эпика не совпадает");
        assertEquals("Тестовое описание", epic.getDescription(), "описание эпика не совпадает");
        assertTrue(epic.getSubtasksID().isEmpty(), "список подзадач эпика не пустой");
    }

    @Test
    void shouldCreateFileBackedTaskManagerWithEmptyHistoryList() {
        String path = "src/data/tasks.csv";
        FileBackedTasksManager managerOne = new FileBackedTasksManager(path);
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

        FileBackedTasksManager managerTwo = loadFromFile(path);
        assertTrue(managerTwo.getHistory().isEmpty(), "список истории задач не пустой");
    }
}
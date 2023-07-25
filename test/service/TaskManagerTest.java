package service;

import helpers.Statuses;
import module.Epic;
import module.Subtask;
import module.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {

    protected abstract T createTaskManagerTest();

    private T taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = createTaskManagerTest();
    }

    @Test
    void shouldReturnTasksListWithGetTasksMethod() {
        assertNotNull(taskManager.getTasks(),"Cписок не возвращается");

        Task task = new Task("Тестовая задача", "Тестовое описание");
        taskManager.createTask(task);
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач");
        assertEquals(task, taskManager.getTasks().get(0), "Задачи не совпадают");
    }

    @Test
    void shouldReturnEpicsTaskListWithGetEpicsMethod() {
        assertNotNull(taskManager.getEpics(),"Cписок не возвращается");

        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач");
        assertEquals(epic, taskManager.getEpics().get(0), "Эпики не совпадают");
    }

    @Test
    void shouldReturnSubtasksListWithGetSubtasks() {
        assertNotNull(taskManager.getSubtasks(),"Cписок не возвращается");

        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", epicID);
        taskManager.createSubtask(subtask);

        assertEquals(1, taskManager.getSubtasks().size(), "Неверное количество задач");
        assertEquals(subtask, taskManager.getSubtasks().get(0), "Задачи не совпадают");
    }

    @Test
    void shouldReturnEmptyTasksListWithClearTaskListMethod() {

        Task task = new Task("Тестовая задача", "Тестовое описание");
        taskManager.createTask(task);
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач");

        taskManager.clearTaskList();
        assertEquals(0, taskManager.getTasks().size(), "список не пустой");
    }

    @Test
    void shouldReturnEmptySubtasksListWithClearSubTaskList() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", epicID);
        taskManager.createSubtask(subtask);
        assertEquals(1, taskManager.getSubtasks().size(), "Неверное количество задач");

        taskManager.clearSubTaskList();
        assertEquals(0, taskManager.getSubtasks().size(), "список не пустой");
        assertTrue(epic.getSubtasksID().isEmpty(), "подзадача осталась в списке эпика");


    }

    @Test
    void shouldReturnEmptyEpicsListWithClearEpicTaskList() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач");

        Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", epicID);
        taskManager.createSubtask(subtask);
        assertEquals(1, taskManager.getSubtasks().size(), "Неверное количество задач");

        taskManager.clearEpicTaskList();
        assertEquals(0, taskManager.getEpics().size(), "список эпиков не пустой");
        assertEquals(0, taskManager.getSubtasks().size(), "список подзадач не пустой");
    }

    @Test
    void shouldReturnTaskByIDWithGetTaskByIDMethod() {
        Task task = new Task("Тестовая задача", "Тестовое описание");
        taskManager.createTask(task);
        int taskID = task.getId();

        Task returnedTask = taskManager.getTaskByID(taskID);
        assertEquals(task, returnedTask, "задачи не совпадают");

        List<Task> historyList = taskManager.getHistory();
        assertEquals(task, historyList.get(0), "задача не добавлена в историю просмотров");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfWrongIDInGetTaskByIDMethod() {
        List<Task> tasks = taskManager.getTasks();
        if (tasks.isEmpty()) {
            final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                    () -> taskManager.getTaskByID(1));
            assertEquals("Задачи с таким id нет в списке", exception1.getMessage());
        }
    }

    @Test
    void shouldReturnEpicByIDWithGetEpicTaskByIDMethod() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Epic returnedEpic = (Epic) taskManager.getEpicTaskByID(epicID);
        assertEquals(epic, returnedEpic, "эпики не совпадают");

        List<Task> historyList = taskManager.getHistory();
        assertTrue(historyList.contains(epic), "задача не добавлена в историю просмотров");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfWrongIDInGetEpicsTaskByIDMethod() {
        List<Epic> epics = taskManager.getEpics();
        if (epics.isEmpty()) {
            final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                    () -> taskManager.getEpicTaskByID(1));
            assertEquals("Эпика с таким id нет в списке", exception1.getMessage());
        }
    }

    @Test
    void shouldReturnSubtaskByIDWithGetSubtaskByIDMethod() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", epicID);
        taskManager.createSubtask(subtask);
        int subtaskID = subtask.getId();

        Subtask returnedSubtask = (Subtask) taskManager.getSubtaskByID(subtaskID);
        assertEquals(subtask, returnedSubtask, "эпики не совпадают");

        List<Task> historyList = taskManager.getHistory();
        assertTrue(historyList.contains(subtask), "задача не добавлена в историю просмотров");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfWrongIDInGetSubtaskByIDMethod() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        if (subtasks.isEmpty()) {
            final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                    () -> taskManager.getSubtaskByID(1));
            assertEquals("Подзадачи с таким id нет в списке", exception1.getMessage());
        }
    }

    @Test
    void shouldCreateTaskWithCreateTaskMethod() {
        Task task = new Task("Тестовая задача", "Тестовое описание");
        taskManager.createTask(task);

        int taskID = task.getId();
        Task savedTask = taskManager.getTaskByID(taskID);

        assertEquals(task, savedTask, "задача не найдена");

        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks,"Cписок не возвращается");

        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    void shouldCreateEpicTaskWithcreateEpicTaskMethod() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);

        int epicID = epic.getId();
        Task savedEpic = taskManager.getEpicTaskByID(epicID);

        assertEquals(epic, savedEpic, "Эпик не найден");

        List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics,"Cписок не возвращается");

        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic, epics.get(0), "Эпики не совпадают");
    }

    @Test
    void shouldCreateSubtaskWithcreateSubtaskMethod() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", epicID);
        taskManager.createSubtask(subtask);

        int subtaskID = subtask.getId();
        Task savedSubtask = taskManager.getSubtaskByID(subtaskID);

        assertEquals(subtask, savedSubtask, "Подзадача не найдена");

        List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks,"Cписок не возвращается");

        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWithWrongEpicIDWithCreateSubtaskMethod() {
        Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", 0);
        final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.createSubtask(subtask));
        assertEquals("Невозможно создать подзадачу. Эпика с id 0 нет в списке", exception1.getMessage());
    }

    @Test
    void shouldUpdateStatusNameDescriptionWithUpdateTaskMethod() {
        Task task = new Task("Тестовая задача", "Тестовое описание");
        taskManager.createTask(task);
        int taskID = task.getId();

        task.setStatus(Statuses.IN_PROGRESS);
        task.setName("Обновленное имя");
        task.setDescription("Обновленное описание");
        taskManager.updateTask(task);
        Task updatedTask = taskManager.getTaskByID(taskID);


        assertEquals(task, updatedTask, "Задачи нет в списке");
        assertEquals(Statuses.IN_PROGRESS, updatedTask.getStatus());
        assertEquals("Обновленное описание", updatedTask.getDescription());
        assertEquals("Обновленное имя", updatedTask.getName());
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWithDefaultTaskID() {
        Task task = new Task("Тестовая задача", "Тестовое описание");
        int taskID = task.getId();

        final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getTaskByID(taskID));
        assertEquals("Задачи с таким id нет в списке", exception1.getMessage());


    }

    @Test
    void shouldUpdateStatusNameDescriptionWithEpicTaskMethod() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);

        epic.setName("Обновленное имя");
        epic.setDescription("Обновленное описание");
        subtaskOne.setStatus(Statuses.DONE);
        taskManager.updateSubtask(subtaskOne);
        taskManager.updateEpic(epic);
        Epic updatedEpic = (Epic) taskManager.getEpicTaskByID(epicID);

        assertEquals("Обновленное описание", updatedEpic.getDescription(), "Описание не совпадает");
        assertEquals("Обновленное имя", updatedEpic.getName(), "Имя не совпадает");
        assertEquals(Statuses.IN_PROGRESS, updatedEpic.getStatus(), "Статусы не совпадают");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWithDefaultEpicID() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        int epicID = epic.getId();

        final IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getEpicTaskByID(epicID));
        assertEquals("Эпика с таким id нет в списке", exception3.getMessage());

    }

    @Test
    void shouldUpdateStatusNameDescriptionWithUpdateSubtaskMethod() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", epicID);
        taskManager.createSubtask(subtask);
        int subtaskID = subtask.getId();

        subtask.setStatus(Statuses.IN_PROGRESS);
        subtask.setName("Обновленное имя");
        subtask.setDescription("Обновленное описание");
        taskManager.updateSubtask(subtask);
        Task updatedSubtask = taskManager.getSubtaskByID(subtaskID);

        assertEquals(Statuses.IN_PROGRESS, updatedSubtask.getStatus(), "статусы не совпадают");
        assertEquals("Обновленное описание", updatedSubtask.getDescription(), "Описание не совпадает");
        assertEquals("Обновленное имя", updatedSubtask.getName(), "Имя не совпадает");
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWithDefaultSubtaskID() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
                    taskManager.createEpicTask(epic);
                    int epicID = epic.getId();
                    taskManager.createEpicTask(epic);
                    Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", epicID);
                    taskManager.updateSubtask(subtask);
                });
        assertEquals("такой задачи нет в списке", exception.getMessage());
    }

    @Test
    void shouldRemoveTaskWithTaskByIDMethod() {

        Task task = new Task("Тестовая задача", "Тестовое описание");
        taskManager.createTask(task);
        int taskID = task.getId();

        taskManager.removeTaskByID(taskID);

        final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getTaskByID(taskID));
        assertEquals("Задачи с таким id нет в списке", exception1.getMessage());

        List<Task> historyList = taskManager.getHistory();
        assertFalse(historyList.contains(task), "задача осталась в истории просмотров");
    }
    @Test
    void shouldThrowIllegalArgumentExceptionWithWrongIDWhenRemoveTask() {
        List<Task> tasks = taskManager.getTasks();
        if (tasks.isEmpty()) {
            final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                    () -> taskManager.removeTaskByID(1));
            assertEquals("Невозможно удалить задачу. Задачи с таким id нет в списке",
                    exception1.getMessage());
        }
    }

    @Test
    void shouldRemoveEpicWithEpicTaskByIDMethod() {

        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);
        int subtaskOneID = subtaskOne.getId();
        int subtaskTwoID = subtaskTwo.getId();

        List<Task> historyList = taskManager.getHistory();

        taskManager.removeEpicTaskByID(epicID);

        final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getEpicTaskByID(epicID));
        assertEquals("Эпика с таким id нет в списке", exception1.getMessage());

        final IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getSubtaskByID(subtaskOneID));
        assertEquals("Подзадачи с таким id нет в списке", exception2.getMessage());

        final IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getSubtaskByID(subtaskTwoID));
        assertEquals("Подзадачи с таким id нет в списке", exception3.getMessage());

        assertFalse(historyList.contains(epic), "эпик остался в истории просмотров");
        assertFalse(historyList.contains(subtaskOne), "подзадача 1 осталась в истории просмотров");
        assertFalse(historyList.contains(subtaskTwo), "подзадача 2 осталась в истории просмотров");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionDWhenRemoveWithWrongID() {
        List<Epic> epics = taskManager.getEpics();
        if (epics.isEmpty()) {
            final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                    () -> taskManager.removeEpicTaskByID(1));
            assertEquals("Невозможно удалить эпик. Эпика с таким id нет в списке", exception1.getMessage());
        }
    }


    @Test
    void shouldRemoveSubtasksWithRemoveSubtaskByIDMethod() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);
        int subtaskOneID = subtaskOne.getId();
        int subtaskTwoID = subtaskTwo.getId();

        taskManager.removeSubtaskByID(subtaskOneID);
        taskManager.removeSubtaskByID(subtaskTwoID);

        final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getSubtaskByID(subtaskOneID));
        assertEquals("Подзадачи с таким id нет в списке", exception1.getMessage());

        final IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
                () -> taskManager.getSubtaskByID(subtaskTwoID));
        assertEquals("Подзадачи с таким id нет в списке", exception2.getMessage());

        assertFalse(taskManager.getHistory().contains(subtaskOne), "подзадача осталась в истории просмотров");
        assertFalse(taskManager.getHistory().contains(subtaskTwo), "подзадача осталась в истории просмотров");

        List<Integer> subtasksID = epic.getSubtasksID();

        assertFalse(subtasksID.contains(subtaskOne.getId()), "подзадача осталась в списке эпика");
        assertFalse(subtasksID.contains(subtaskTwo.getId()), "подзадача осталась в в списке эпика");

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWithWrongIDWhenRemoveSubtask() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        if (subtasks.isEmpty()) {
            final IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                    () -> taskManager.removeSubtaskByID(1));
            assertEquals("Невозможно удалить подзадачу. Подзадачи с таким с таким id нет в списке",
                    exception1.getMessage());
        }
    }

    @Test
    void shouldReturnSubtasksListByEpic() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);

        List<Subtask> subtasksListByEpic = taskManager.getSubtaskListByEpic(epicID);

        assertNotNull(subtasksListByEpic, "не найден список подзадач");
        assertTrue(subtasksListByEpic.contains(subtaskOne), "Подзадачи 1 нет в списке");
        assertTrue(subtasksListByEpic.contains(subtaskTwo), "Подзадачи 2 нет в списке");
    }

    @Test
    void shouldReturnEmptyListWithWrongEpicID() {
        List<Epic> epics = taskManager.getEpics();
        if (epics.isEmpty()) {
            assertEquals(Collections.emptyList(), taskManager.getSubtaskListByEpic(1));
        }
    }

    @Test
    void shouldDeleteDublicatesInHistoryList() {

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

        taskManager.createTask(taskOne);
        taskManager.createTask(taskTwo);
        taskManager.createEpicTask(epicOne);
        taskManager.createEpicTask(epicTwo);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);
        taskManager.createSubtask(subtaskThree);

        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        taskManager.getEpicTaskByID(3);
        taskManager.getEpicTaskByID(4);
        taskManager.getSubtaskByID(5);
        taskManager.getSubtaskByID(6);
        taskManager.getSubtaskByID(7);
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        taskManager.getEpicTaskByID(3);
        taskManager.getEpicTaskByID(4);
        taskManager.getSubtaskByID(5);
        taskManager.getSubtaskByID(6);
        taskManager.getSubtaskByID(7);

        List<Task> historyList = taskManager.getHistory();

        assertNotNull(historyList, "список не найден");
        assertEquals(7, historyList.size(), "размер истории просмотров не совпадает");
        assertEquals(1, Collections.frequency(historyList, taskOne), "есть дубликат задачи 1");
        assertEquals(1, Collections.frequency(historyList, taskTwo), "есть дубликат задачи 2");
        assertEquals(1, Collections.frequency(historyList, epicOne), "есть дубликат эпика 1");
        assertEquals(1, Collections.frequency(historyList, epicTwo), "есть дубликат эпика 2");
        assertEquals(1, Collections.frequency(historyList, subtaskOne), "есть дубликат подзадачи 1.1");
        assertEquals(1, Collections.frequency(historyList, subtaskTwo), "есть дубликат подзадачи 1.2");
        assertEquals(taskOne, historyList.get(0), "Задача 1 не на своем месте");
        assertEquals(taskTwo, historyList.get(1), "Задача 2 не на своем месте");
        assertEquals(epicOne, historyList.get(2), "Эпик 1 не на своем месте");
        assertEquals(epicTwo, historyList.get(3), "Эпик 2 не на своем месте");
        assertEquals(subtaskOne, historyList.get(4), "Подзадача 1 не на своем месте");
        assertEquals(subtaskTwo, historyList.get(5), "Подзадача 2 не на своем месте");
        assertEquals(subtaskThree, historyList.get(6), "Подзадача 3 не на своем месте");
    }

    @Test
    void shouldDeleteTasksFromHistoryList() {

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

        taskManager.createTask(taskOne);
        taskManager.createTask(taskTwo);
        taskManager.createEpicTask(epicOne);
        taskManager.createEpicTask(epicTwo);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);
        taskManager.createSubtask(subtaskThree);

        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        taskManager.getEpicTaskByID(3);
        taskManager.getEpicTaskByID(4);
        taskManager.getSubtaskByID(5);
        taskManager.getSubtaskByID(6);
        taskManager.getSubtaskByID(7);

        assertNotNull(taskManager.getHistory(), "список не найден");

        /* удаление задачи в начале */

        taskManager.removeTaskByID(1);
        assertEquals(6, taskManager.getHistory().size(), "размер истории просмотров не совпадает");

        assertEquals(taskTwo, taskManager.getHistory().get(0), "Задача 2 не на своем месте");
        assertEquals(epicOne, taskManager.getHistory().get(1), "Эпик 1 не на своем месте");
        assertEquals(epicTwo, taskManager.getHistory().get(2), "Эпик 2 не на своем месте");
        assertEquals(subtaskOne, taskManager.getHistory().get(3), "Подзадача 1 не на своем месте");
        assertEquals(subtaskTwo, taskManager.getHistory().get(4), "Подзадача 2 не на своем месте");
        assertEquals(subtaskThree, taskManager.getHistory().get(5), "Подзадача 3 не на своем месте");

        /* удаление задачи в конце */
        taskManager.removeSubtaskByID(7);
        assertEquals(5, taskManager.getHistory().size(), "размер истории просмотров не совпадает");
        assertEquals(taskTwo, taskManager.getHistory().get(0), "Задача 2 не на своем месте");
        assertEquals(epicOne, taskManager.getHistory().get(1), "Эпик 1 не на своем месте");
        assertEquals(epicTwo, taskManager.getHistory().get(2), "Эпик 2 не на своем месте");
        assertEquals(subtaskOne, taskManager.getHistory().get(3), "Подзадача 1 не на своем месте");
        assertEquals(subtaskTwo, taskManager.getHistory().get(4), "Подзадача 2 не на своем месте");

        /* удаление задачи в середине */
        taskManager.removeEpicTaskByID(4);
        assertEquals(4, taskManager.getHistory().size(), "размер истории просмотров не совпадает");
        assertEquals(taskTwo, taskManager.getHistory().get(0), "Задача 2 не на своем месте");
        assertEquals(epicOne, taskManager.getHistory().get(1), "Эпик 1 не на своем месте");
        assertEquals(subtaskOne, taskManager.getHistory().get(2), "Подзадача 1 не на своем месте");
        assertEquals(subtaskTwo, taskManager.getHistory().get(3), "Подзадача 2 не на своем месте");

        /* удаление эпика с подзадачами */
        taskManager.removeEpicTaskByID(3);
        assertEquals(1, taskManager.getHistory().size(), "размер истории просмотров не совпадает");
        assertEquals(taskTwo, taskManager.getHistory().get(0), "Задача 2 не на своем месте");
    }

    @Test
    void shouldReturnEmptyHistoryList() {
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    void shouldReturnStatusNEWForEpicWithEmptySubtasksList() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        List <Subtask> epicSubtasks = taskManager.getSubtaskListByEpic(epicID);
        assertTrue(epicSubtasks.isEmpty(), "список подзадач эпика не пустой");
        assertEquals(Statuses.NEW, epic.getStatus(), "статус эпика не NEW");
    }

    @Test
    void shouldReturnStatusNEWForEpicWithAllSubtasksWithStatusNEW() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);

        assertEquals(Statuses.NEW, subtaskOne.getStatus(), "статус подзадачи 1 не NEW");
        assertEquals(Statuses.NEW, subtaskOne.getStatus(), "статус подзадачи 2 не NEW");
        assertEquals(Statuses.NEW, epic.getStatus(), "статус эпика не NEW");
    }

    @Test
    void shouldReturnStatusDONEForEpicWithAllSubtasksWithStatusDONE() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);

        subtaskOne.setStatus(Statuses.DONE);
        subtaskTwo.setStatus(Statuses.DONE);
        taskManager.updateSubtask(subtaskOne);
        taskManager.updateSubtask(subtaskTwo);
        taskManager.updateEpic(epic);
        Epic updatedEpic = (Epic) taskManager.getEpicTaskByID(epicID);


        assertEquals(Statuses.DONE, subtaskOne.getStatus(), "статус подзадачи 1 не DONE");
        assertEquals(Statuses.DONE, subtaskOne.getStatus(), "статус подзадачи 2 не DONE");
        assertEquals(Statuses.DONE, updatedEpic.getStatus(), "статус эпика не DONE");
    }

    @Test
    void shouldReturnStatusINPROGRESSForEpicWithSubtasksWithStatusesNEWAndDone() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);

        subtaskTwo.setStatus(Statuses.DONE);
        taskManager.updateSubtask(subtaskTwo);
        Epic updatedEpic = (Epic) taskManager.getEpicTaskByID(epicID);

        assertEquals(Statuses.NEW, subtaskOne.getStatus(), "статус подзадачи 1 не NEW");
        assertEquals(Statuses.DONE, subtaskTwo.getStatus(), "статус подзадачи 2 не DONE");
        assertEquals(Statuses.IN_PROGRESS, updatedEpic.getStatus(), "статус эпика не IN_PROGRESS");
    }

    @Test
    void shouldReturnStatusINPROGRESSForEpicWithAllSubtasksWithStatusesINPROGRESS() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);

        subtaskOne.setStatus(Statuses.IN_PROGRESS);
        subtaskTwo.setStatus(Statuses.IN_PROGRESS);
        taskManager.updateSubtask(subtaskOne);
        taskManager.updateSubtask(subtaskTwo);

        assertEquals(Statuses.IN_PROGRESS, subtaskOne.getStatus(), "статус подзадачи 1 не IN_PROGRESS");
        assertEquals(Statuses.IN_PROGRESS, subtaskOne.getStatus(), "статус подзадачи 2 не IN_PROGRESS");
        assertEquals(Statuses.IN_PROGRESS, epic.getStatus(), "статус эпика не IN_PROGRESS");
    }

    @Test
    void shouldReturnStartTimeWithDurationAndEndTimeForEpicWithSubtasks() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        subtaskOne.setStartTime(LocalDateTime.of(2023, 6, 23, 12, 0));
        subtaskOne.setDuration(30);
        subtaskTwo.setStartTime(LocalDateTime.of(2023, 6, 23, 13, 0));
        subtaskTwo.setDuration(60);
        taskManager.createSubtask(subtaskOne);
        taskManager.createSubtask(subtaskTwo);

        assertEquals(LocalDateTime.of(2023, 6, 23, 12, 0),
                epic.getStartTime(), "время старта эпика не корректно");
        assertEquals(LocalDateTime.of(2023, 6, 23, 14, 0),
                epic.getEndTime(), "время окончания эпика не корректно");
        assertEquals(120,
                epic.getDuration(), "продолжительность эпика не корректна");
    }


    @Test
    void shouldUpdateEpicWithNoSubtasksWithNullableStartTimeAndEnDitme() {
        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);

        epic.setName("новое имя эпика");
        taskManager.updateEpic(epic);


        assertNull(epic.getStartTime(),"время старта должно быть null");
        assertNull(epic.getEndTime(),"время окончания эпика должно быть null");
        assertEquals(0, epic.getDuration(), "продолжительность эпика должна быть 0");
    }

    @Test
    void ShouldReturnPrioritizedTasksList() {
        Task taskOne = new Task("Задача №1", "Это простая задача");
        taskOne.setStartTime(LocalDateTime.of(2023, 6, 22, 12, 10));
        taskOne.setDuration(120);
        taskManager.createTask(taskOne);

        Task taskTwo = new Task("Задача №2", "Еще одна простая задача");
        taskTwo.setStartTime(LocalDateTime.of(2023, 6, 22, 10, 30));
        taskTwo.setDuration(60);
        taskManager.createTask(taskTwo);

        Epic epic = new Epic("Тестовый эпик", "Тестовое описание");
        taskManager.createEpicTask(epic);
        int epicID = epic.getId();

        Subtask subtaskOne = new Subtask("Тестовая подзадача 1", "Тестовое описание 1", epicID);
        subtaskOne.setStartTime(LocalDateTime.of(2023, 6, 23, 15, 10));
        subtaskOne.setDuration(30);
        taskManager.createSubtask(subtaskOne);

        Subtask subtaskTwo = new Subtask("Тестовая подзадача 2", "Тестовое описание 2", epicID);
        subtaskTwo.setStartTime(LocalDateTime.of(2023, 6, 23, 14, 7));
        subtaskTwo.setDuration(30);
        taskManager.createSubtask(subtaskTwo);

        Subtask subtaskThree = new Subtask("Тестовая подзадача 3", "Тестовое описание 3", epicID);
        taskManager.createSubtask(subtaskThree);

        Subtask subtaskFour = new Subtask("Тестовая подзадача 4", "Тестовое описание 4", epicID);
        taskManager.createSubtask(subtaskFour);


        List<Task> prioritizedTasksList = taskManager.getPrioritizedTasks();
        prioritizedTasksList.forEach(System.out::println);

        assertNotNull(prioritizedTasksList, "список приоритетных задач не найден");
        assertEquals(taskTwo, prioritizedTasksList.get(0), "самая срочная задача не стоит в начале списка");
        assertEquals(subtaskThree, prioritizedTasksList.get(4), "задача без стартового времени должна быть" +
                " в конце списка");
        assertEquals(subtaskFour, prioritizedTasksList.get(5), "задача без стартового времени должна быть" +
                " в конце списка");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTimeIntersectionIsFound() {

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Task taskOne = new Task("Задача №1", "Это простая задача");
                    taskOne.setStartTime(LocalDateTime.of(2023, 6, 22, 12, 12));
                    taskOne.setDuration(120);
                    taskManager.createTask(taskOne);

                    Task taskTwo = new Task("Задача №2", "Еще одна простая задача");
                    taskTwo.setStartTime(LocalDateTime.of(2023, 6, 22, 13, 30));
                    taskTwo.setDuration(60);
                    taskManager.createTask(taskTwo);
                });
        assertEquals("текущее время занято", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStartTimeIsBeforeSetTime() {

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Task taskOne = new Task("Задача №1", "Это простая задача");
                    taskOne.setStartTime(LocalDateTime.of(2023, 6, 21, 23, 59));
                    taskOne.setDuration(120);
                    taskManager.createTask(taskOne);
                });
        assertEquals("время старта задачи должно быть начиная с 22.06.2023 г.", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStartTimeIsAfterSetTime() {

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Task taskOne = new Task("Задача №1", "Это простая задача");
                    taskOne.setStartTime(LocalDateTime.of(2024, 6, 23, 0, 0));
                    taskOne.setDuration(120);
                    taskManager.createTask(taskOne);
                });
        assertEquals("время старта задачи должно быть не позднее 22.06.2024г. 23:59", exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenEndTimeIsAfterSetTime() {

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Task taskOne = new Task("Задача №1", "Это простая задача");
                    taskOne.setStartTime(LocalDateTime.of(2024, 6, 22, 23, 30));
                    taskOne.setDuration(120);
                    taskManager.createTask(taskOne);
                });
        assertEquals("время окончания задачи должно быть до 22.06.2024 г.", exception.getMessage());
    }

    @Test
    void shouldReleaseTimetableWhenTaskIsDeleted() {
        Task taskOne = new Task("Задача №1", "Это простая задача");
        taskOne.setStartTime(LocalDateTime.of(2023, 6, 22, 10, 30));
        taskOne.setDuration(60);
        taskManager.createTask(taskOne);
        taskManager.removeTaskByID(taskOne.getId());

        Task taskTwo = new Task("Задача №2", "Еще одна простая задача");
        taskTwo.setStartTime(LocalDateTime.of(2023, 6, 22, 10, 30));
        taskTwo.setDuration(60);
        taskManager.createTask(taskTwo);

        assertEquals(LocalDateTime.of(2023, 6, 22, 10, 30), taskTwo.getStartTime());
    }

    @Test
    void shouldReleaseTimetableWhenTaskListIsDeleted() {
        Task taskOne = new Task("Задача №1", "Это простая задача");
        taskOne.setStartTime(LocalDateTime.of(2023, 6, 22, 10, 30));
        taskOne.setDuration(60);
        taskManager.createTask(taskOne);

        Task taskTwo = new Task("Задача №2", "Еще одна простая задача");
        taskTwo.setStartTime(LocalDateTime.of(2023, 6, 22, 13, 30));
        taskTwo.setDuration(60);
        taskManager.createTask(taskTwo);

        taskManager.clearTaskList();

        Task taskThree = new Task("Задача №3", "Это простая задача");
        taskThree.setStartTime(LocalDateTime.of(2023, 6, 22, 10, 30));
        taskThree.setDuration(60);
        taskManager.createTask(taskThree);

        Task taskFour = new Task("Задача №4", "Еще одна простая задача");
        taskFour.setStartTime(LocalDateTime.of(2023, 6, 22, 13, 30));
        taskFour.setDuration(60);
        taskManager.createTask(taskFour);


        assertEquals(
                LocalDateTime.of(2023, 6, 22, 10, 30),
                taskThree.getStartTime());

        assertEquals(
                LocalDateTime.of(2023, 6, 22, 13, 30),
                taskFour.getStartTime());
    }
}
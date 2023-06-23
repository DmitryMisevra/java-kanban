package service;

import module.Epic;
import module.Subtask;
import module.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskManager {

    /* getTasks() возвращает список задач простых задач */
    List<Task> getTasks();

    /* getEpics() возвращает список задач эпиков */
    List<Epic> getEpics();

    /* getSubtasks() возвращает список всех подзадач */
    List<Subtask> getSubtasks();

    /* clearTasklist() очищает мапу с простыми задачами */
    void clearTaskList();

    /* clearSubtasklist() очищает мапу с подзадачами */
    /* Также метод обновляет списки подзадач и статусы внутри епиков */
    void clearSubTaskList();

    /* clearEpicTasklist() очищает мапу с эпиками и подзадачами */
    void clearEpicTaskList();

    /* getTaskByID возращает простую задачу по ее id */
    Task getTaskByID(int requestedID);

    /* getEpicTaskByID возвращает эпик по его id */
    Task getEpicTaskByID(int requestedID);

    /* Метод getSubtaskByID возвращает подзадачу по ее id */
    Task getSubtaskByID(int requestedID);

    /* createTask создает новую простую задачу и возвращает ее */
    Task createTask(Task task);

    /* createEpicTask создает новый эпик и возвращает его */
    Epic createEpicTask(Epic epic);

    /* createSubTask создает новую подзадачу и возвращает ее
    Также метод добавляет подзадачу к требуемому эпику в
    список подзадач и обновляет его статус */
    Subtask createSubtask(Subtask subtask);

    /* updateTask обновляет простую задачу */
    void updateTask(Task task);

    /* updateEpic обновляет эпик */
    void updateEpic(Epic epic);

    /* updateSubtask обновляет подзадачу
    Также метод обновляет статус эпика, к которому относится подзадача */
    void updateSubtask(Subtask subtask);

    /* removeTaskByID находит задачу по ее id, удаляет ее и возвращает удаленный объект */
    Task removeTaskByID(int requestedID);

    /* removeEpicTaskByID находит эпик по его id, удаляет его и возвращает удаленный объект
    Также метод удаляет все подзадачи, относящиеся к этому эпику */
    Epic removeEpicTaskByID(int requestedID);

    /* removeSubtaskByID находит подзадачу по ее id, удаляет ее и возвращает удаленный объект
    Также метод удаляет подзадачу из списка подзадач эпика, к которому она принадлежала и
    обновляет статус эпика */
    Subtask removeSubtaskByID(int requestedID);

    /* getSubtaskListByEpic возвращает список подзадач запрашиваемого эпика */
    List<Subtask> getSubtaskListByEpic(int epicID);


    /* метод getHistory() возвращает список с историей просмотров последних 10 задач */
    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}




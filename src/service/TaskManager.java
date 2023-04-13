package service;

import module.Epic;
import module.Subtask;
import module.Task;

import java.util.ArrayList;

public interface TaskManager {

    // getTasks() возвращает список задач простых задач
    ArrayList<Task> getTasks();

    // getEpics() возвращает список задач эпиков
    ArrayList<Epic> getEpics();

    // getSubtasks() возвращает список всех подзадач
    ArrayList<Subtask> getSubtasks();

    // clearTasklist() очищает мапу с простыми задачами
    void clearTaskList();

    // clearSubtasklist() очищает мапу с подзадачами
    // Также метод обновляет списки подзадач и статусы внутри епиков
    void clearSubTaskList();

    // clearEpicTasklist() очищает мапу с эпиками и подзадачами
    void clearEpicTaskList();

    // getTaskByID возращает простую задачу по ее id
    Task getTaskByID(int requestedID);

    // getEpicTaskByID возвращает эпик по его id
    Task getEpicTaskByID(int requestedID);

    // Метод getSubtaskByID возвращает ползадачу по ее id
    Subtask getSubtaskByID(int requestedID);

    // createTask создает новую простую задачу и возращает ее
    Task createTask(Task task);

    // createEpicTask создает новый эпик и возращает его
    Epic createEpicTask(Epic epic);

    // createSubTask создает новую подзадачу и возращает ее
    // Также метод добавляет подзадачу к требуемому эпику в списко подзадач и обновляет его статус
    Subtask createSubtask(Subtask subtask);

    // updateTask обновляет простую задачу
    void updateTask(Task task);

    // updateEpic обновляет эпик
    void updateEpic(Epic epic);

    // updateSubtask обновляет подзадачу
    // Также метод обновляет статус эпика, к которому относится подзадача
    void updateSubtask(Subtask subtask);

    // removeTaskByID находит задачу по ее id, удаляет ее и возращает удаленный объект
    Task removeTaskByID(int requestedID);

    // removeEpicTaskByID находит эпик по его id, удаляет его и возращает удаленный объект
    // Также метод удаляет все подзадачи, относящиеся к этому эпику
    Epic removeEpicTaskByID(int requestedID);

    // removeSubtaskByID находит подзадачу по ее id, удаляет ее и возращает удаленный объект
    /* Также метод удаляет подзадачу из списка подзадач эпика, к коториму она принадлежала и
     обновляет статус эпика.*/
    Subtask removeSubtaskByID(int requestedID);

    // getSubtaskListByEpic вовзращает список подзадач запрашиваемого эпика
    ArrayList<Subtask> getSubtaskListByEpic(int epicID);
}


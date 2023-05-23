package service;

import helpers.Managers;
import helpers.Statuses;
import module.Epic;
import module.Task;
import module.Subtask;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int idCounter = 1; /* счетчик id */
    HistoryManager historyManager = Managers.getDefaultHistory();

    /* для хранения эпиков, задач и подзадач используем HashMap */
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    /* getTasks() возвращает список задач простых задач */
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    /* getEpics() возвращает список задач эпиков */
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    /* getSubtasks() возвращает список всех подзадач */
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return Objects.equals(tasks, that.tasks)
                && Objects.equals(epics, that.epics) && Objects.equals(subtasks, that.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, epics, subtasks);
    }

    /* clearTasklist() очищает мапу с простыми задачами */
    @Override
    public void clearTaskList() {
        tasks.clear();
    }

    /* clearSubtasklist() очищает мапу с подзадачами
    Также метод обновляет списки подзадач и статусы внутри епиков */
    @Override
    public void clearSubTaskList() {
        subtasks.clear();
        for (Integer epicID : epics.keySet()) {
            Epic epic = epics.get(epicID);
            List<Integer> subtasksID = epic.getSubtasksID();
            subtasksID.clear();
            epic.setSubtasksID(subtasksID);
            epic.setStatus(Statuses.NEW);
        }
    }

    /* clearEpicTasklist() очищает мапу с эпиками и подзадачами */
    @Override
    public void clearEpicTaskList() {
        subtasks.clear();
        epics.clear();
    }

    /* getSimpleTaskByID возвращает простую задачу по ее id */
    @Override
    public Task getTaskByID(int requestedID) {
        historyManager.add(tasks.get(requestedID));
        return tasks.get(requestedID);
    }

    /* getEpicTaskByID возвращает эпик по его id */
    @Override
    public Task getEpicTaskByID(int requestedID) {
        historyManager.add(epics.get(requestedID));
        return epics.get(requestedID);
    }

    /* Метод getSubtaskByID возвращает подзадачу по ее id */
    @Override
    public Subtask getSubtaskByID(int requestedID) {
        historyManager.add(subtasks.get(requestedID));
        return subtasks.get(requestedID);
    }

    /* createSimpleTask создает новую простую задачу и возвращает ее */

    @Override
    public Task createTask(Task task) {
        task.setId(idCounter);
        idCounter++;
        tasks.put(task.getId(), task);
        return task;
    }
    /* createEpicTask создает новый эпик и возвращает его */
    @Override
    public Epic createEpicTask(Epic epic) {
        epic.setId(idCounter);
        idCounter++;
        epics.put(epic.getId(), epic);
        return epic;
    }
    /* createSubTask создает новую подзадачу и возвращает ее
    Также метод добавляет подзадачу к требуемому эпику в
    список подзадач и обновляет его статус */
    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(idCounter);
        idCounter++;
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getSubtaskEpicID());
        List<Integer> subtasksID = epic.getSubtasksID();
        subtasksID.add(subtask.getId());
        updateEpicStatus(epic.getId());
        return subtask;
    }

    /* updateSimpleTask обновляет простую задачу */
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /* updateEpic обновляет эпик*/
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /* updateSubtask обновляет подзадачу
    Также метод обновляет статус эпика, к которому относится подзадача */
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getSubtaskEpicID());
    }

    /* removeTaskByID находит задачу по ее id, удаляет ее и возвращает удаленный объект */
    @Override
    public Task removeTaskByID(int requestedID) {
        return tasks.remove(requestedID);
    }

    /* removeEpicTaskByID находит эпик по его id, удаляет его и возвращает удаленный объект
    Также метод удаляет все подзадачи, относящиеся к этому эпику */
    @Override
    public Epic removeEpicTaskByID(int requestedID) {
        Epic removedEpic = epics.remove(requestedID);
        List<Integer> subtasksID = removedEpic.getSubtasksID();
        for (Integer subtaskID : subtasksID) {
            subtasks.remove(subtaskID);
        }
        return removedEpic;
    }

    /* removeSubtaskByID находит подзадачу по ее id, удаляет ее и возвращает удаленный объект
    Также метод удаляет подзадачу из списка подзадач эпика, к которому она принадлежала и
    обновляет статус эпика */
    @Override
    public Subtask removeSubtaskByID(int requestedID) {
        Subtask requestedSubtask = subtasks.get(requestedID);

        Epic epic = epics.get(requestedSubtask.getSubtaskEpicID());
        List<Integer> subtasksID = epic.getSubtasksID();
        subtasksID.remove(Integer.valueOf(requestedSubtask.getId()));
        updateEpicStatus(epic.getId());

        return subtasks.remove(requestedID);
    }

    /* getSubtaskListByEpic возвращает список подзадач запрашиваемого эпика */
    @Override
    public List<Subtask> getSubtaskListByEpic(int epicID) {
        List <Subtask> subtasksListByEpic = new ArrayList<>();

        Epic epic = epics.get(epicID);
        List<Integer> subtasksIDListByEpic = epic.getSubtasksID();

        for (Integer subtaskID : subtasks.keySet()) {
            for (Integer subtaskIDbyEpic : subtasksIDListByEpic) {
                if (subtaskID.equals(subtaskIDbyEpic)) {
                    subtasksListByEpic.add(subtasks.get(subtaskIDbyEpic));
                }
            }
        }
        return subtasksListByEpic;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /* updateEpicStatus находит эпик по его id и обновляет статус
    Используется как вспомогательный метод */
    private void updateEpicStatus(int epicID) {
        Epic epic = epics.get(epicID);
        List<Integer> subtasksID = epic.getSubtasksID();

        if (subtasksID.isEmpty()) {
            epic.setStatus(Statuses.NEW);
            return;
        }

        int statusNew = 0;
        int statusInProgress = 0;
        int statusDone = 0;
        for (Integer subtaskID : subtasksID) {
            Statuses SubtaskStatus = subtasks.get(subtaskID).getStatus();
            switch (SubtaskStatus) {
                case NEW:
                    statusNew++;
                    break;
                case IN_PROGRESS:
                    statusInProgress++;
                    break;
                case DONE:
                    statusDone++;
            }
        }
        if (statusInProgress > 0) {
            epic.setStatus(Statuses.IN_PROGRESS);
        } else if (statusNew == 0 && statusInProgress == 0) {
            epic.setStatus(Statuses.DONE);
        } else if (statusInProgress == 0 && statusDone > 0) {
            epic.setStatus(Statuses.IN_PROGRESS);
        } else if (statusInProgress == 0 && statusDone == 0) {
            epic.setStatus(Statuses.NEW);
        }
    }
}


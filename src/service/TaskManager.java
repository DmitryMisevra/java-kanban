package service;

import module.Epic;
import module.Subtask;
import module.SimpleTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {

    private int IDCounter = 1;

    private HashMap<Integer, SimpleTask> simpleTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    // getSimpleTasks() возвращает список задач простых задач
    public ArrayList<SimpleTask> getSimpleTasks() {
        return new ArrayList<>(simpleTasks.values());
    }

    // getEpics() возвращает список задач эпиков
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // getSubtasks() возвращает список всех подзадач
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskManager that = (TaskManager) o;
        return Objects.equals(simpleTasks, that.simpleTasks) && Objects.equals(epics, that.epics) && Objects.equals(subtasks, that.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleTasks, epics, subtasks);
    }

    public HashMap<Integer, SimpleTask> clearSimpleTaskList() {
        simpleTasks.clear();
        return simpleTasks;
    }

    public HashMap<Integer, Subtask> clearSubTaskList() {
        subtasks.clear();
        for (Integer epicID : epics.keySet()) {
            Epic epic = epics.get(epicID);
            ArrayList<Integer> subtasksID = epic.getSubtasksID();
            subtasksID.clear();
            epic.setSubtasksID(subtasksID);
            epic.setStatus("NEW");
        }
        return subtasks;
    }

    public HashMap<Integer, Epic> clearEpicTaskList() {
        subtasks.clear();
        epics.clear();
        return epics;
    }

    public SimpleTask getSimpleTaskByID(int requestedID) {
        return simpleTasks.get(requestedID);
    }

    public SimpleTask getEpicTaskByID(int requestedID) {
        return epics.get(requestedID);
    }

    public Subtask getSubtaskByID(int requestedID) {
        return subtasks.get(requestedID);
    }

    public SimpleTask createSimpleTask(SimpleTask simpleTask) {
        simpleTask.setId(IDCounter);
        IDCounter++;
        simpleTasks.put(simpleTask.getId(), simpleTask);
        return simpleTask;
    }

    public Epic createEpicTask(Epic epic) {
        epic.setId(IDCounter);
        IDCounter++;
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(IDCounter);
        IDCounter++;
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getSubtaskEpicID());
        ArrayList<Integer> subtasksID = epic.getSubtasksID();
        subtasksID.add(subtask.getId());
        updateEpicStatus(epic.getId());
        return subtask;
    }

    public SimpleTask updateSimpleTask(SimpleTask simpleTask) {
        simpleTasks.put(simpleTask.getId(), simpleTask);
        return simpleTask;
    }

    public Epic updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SimpleTask updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getSubtaskEpicID());
        return subtask;
    }


    public SimpleTask removeSimpleTaskByID(int requestedID) {
        return simpleTasks.remove(requestedID);
    }

    public Epic removeEpicTaskByID(int requestedID) {
        ArrayList<Integer> subtasksID = epics.get(requestedID).getSubtasksID();
        for (Integer subtaskID : subtasksID) {
            subtasks.remove(subtaskID);
        }
        return epics.remove(requestedID);
    }

    public Subtask removeSubtaskByID(int requestedID) {
        Subtask requestedSubtask = subtasks.get(requestedID);

        Epic epic = epics.get(requestedSubtask.getSubtaskEpicID());
        ArrayList<Integer> subtasksID = epic.getSubtasksID();
        subtasksID.remove(Integer.valueOf(requestedSubtask.getId()));
        updateEpicStatus(epic.getId());

        return subtasks.remove(requestedID);
    }

    private void updateEpicStatus(int epicID) {
        Epic epic = epics.get(epicID);
        ArrayList<Integer> subtasksID = getSubtaskListByEpic(epicID);

        if (subtasksID.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }

        int statusNew = 0;
        int statusInProgress = 0;
        int statusDone = 0;
        for (Integer subtaskID : subtasksID) {
            String SubtaskStatus = subtasks.get(subtaskID).getStatus();
            switch (SubtaskStatus) {
                case "NEW":
                    statusNew++;
                    break;
                case "IN_PROGRESS":
                    statusInProgress++;
                    break;
                case "DONE":
                    statusDone++;
            }
        }
        if (statusInProgress > 0) {
            epic.setStatus("IN_PROGRESS");
        } else if (statusNew == 0 && statusInProgress == 0) {
            epic.setStatus("DONE");
        } else if (statusInProgress == 0 && statusDone == 0) {
            epic.setStatus("NEW");
        }
    }

    public ArrayList<Integer> getSubtaskListByEpic(int epicID) {
        Epic epic = epics.get(epicID);
        return epic.getSubtasksID();
    }
}


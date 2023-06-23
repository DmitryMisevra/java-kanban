package service;

import helpers.Types;
import module.Epic;
import module.Subtask;
import module.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private String fileName; /* имя и путь файла */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");

    /* конструктор создает файл вместе с объектом FileBackedTasksManager */
    public FileBackedTasksManager(String fileName) {
        this.fileName = fileName;

        Path path = Paths.get(fileName);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании файла: " + e.getMessage());
        }
    }

    /* метод loadFromFile() создает новый менеджер на основе информации из файла */
    public static FileBackedTasksManager loadFromFile(String path) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
        try {
            String data = Files.readString(Path.of(path));
            String[] tasksFromFile = data.split("\n");
            for (int i = 1; i < tasksFromFile.length; i++) {
                if (tasksFromFile[i].equals("")) {
                    break;
                }
                Task task = fileBackedTasksManager.fromString(tasksFromFile[i]);
                if (task instanceof Subtask) {
                    fileBackedTasksManager.subtasks.put(task.getId(), (Subtask) task);
                } else if (task instanceof Epic) {
                    fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                } else if (task != null) {
                    fileBackedTasksManager.tasks.put(task.getId(), task);
                }
            }
            if (tasksFromFile.length > 3 && tasksFromFile[tasksFromFile.length - 2].equals("")) {
                String historyData = tasksFromFile[tasksFromFile.length - 1];
                List<Integer> historyDataList = historyFromString(historyData);
                for (Integer key : historyDataList) {
                    if (fileBackedTasksManager.tasks.containsKey(key)) {
                        Task task = fileBackedTasksManager.tasks.get(key);
                        fileBackedTasksManager.historyManager.add(task);
                    } else if (fileBackedTasksManager.subtasks.containsKey(key)) {
                        Subtask subtask = fileBackedTasksManager.subtasks.get(key);
                        fileBackedTasksManager.historyManager.add(subtask);
                    } else if (fileBackedTasksManager.epics.containsKey(key)) {
                        Epic epic = fileBackedTasksManager.epics.get(key);
                        fileBackedTasksManager.historyManager.add(epic);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла: " + e.getMessage());
        }
        return fileBackedTasksManager;
    }

    /* во все модифицирующие методы добавлен вызов метода save() */
    @Override
    public Task getTaskByID(int requestedID) {
        Task task = super.getTaskByID(requestedID);
        save();
        return task;
    }

    @Override
    public Task getEpicTaskByID(int requestedID) {
        Task task = super.getEpicTaskByID(requestedID);
        save();
        return task;
    }

    @Override
    public Task getSubtaskByID(int requestedID) {
        Task subtask = super.getSubtaskByID(requestedID);
        save();
        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic createEpicTask(Epic epic) {
        Epic newEpic = super.createEpicTask(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask newSubtask = super.createSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Task removeTaskByID(int requestedID) {
        Task removedTask = super.removeTaskByID(requestedID);
        save();
        return removedTask;
    }

    @Override
    public Epic removeEpicTaskByID(int requestedID) {
        Epic removedEpic = super.removeEpicTaskByID(requestedID);
        save();
        return removedEpic;
    }

    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    @Override
    public void clearSubTaskList() {
        super.clearSubTaskList();
        save();
    }

    @Override
    public void clearEpicTaskList() {
        super.clearEpicTaskList();
        save();
    }

    @Override
    public Subtask removeSubtaskByID(int requestedID) {
        Subtask removedSubtask = super.removeSubtaskByID(requestedID);
        save();
        return removedSubtask;
    }

    /*
    метод historyToString(HistoryManager manager) преобразует данные истории просмотров в строку для последующего
    сохранения в файл
    */
    private static String historyToString(HistoryManager manager) {
        List<String> historyList = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            historyList.add(Integer.toString(task.getId()));
        }
        return String.join(",", historyList);
    }

    /* метод historyFromString() считывает историю просмотров из файла и возвращает ее в список */

    private static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String s : split) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }

    /* метод save() сохраняет текущее состояние менеджера в указанный файл */
    private void save() {
        try (FileWriter out = new FileWriter(fileName)) {
            out.write("id,type,name,status,description,startTime,duration,endTime,epic" + "\n");
            Map<Integer, Task> allTasks = new HashMap<>();
            allTasks.putAll(tasks);
            allTasks.putAll(subtasks);
            allTasks.putAll(epics);
            for (Task value : allTasks.values()) {
                out.write(taskToString(value) + "\n");
            }
            out.write("\n");
            out.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл: " + e.getMessage());
        }
    }

    /* метод toString(Task task) сохраняет параметры задачи в строку для ее дальнейшего сохранения в файл  */
    private String taskToString(Task task) {
        String startTimeAsString = (task.getStartTime() == null) ? "null" : task.getStartTime().format(formatter);
        String endTimeAsString = (task.getEndTime() == null) ? "null" : task.getEndTime().format(formatter);

        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getType(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    startTimeAsString,
                    task.getDuration(),
                    endTimeAsString,
                    subtask.getSubtaskEpicID());
        } else if (task instanceof Epic) {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getType(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    startTimeAsString,
                    task.getDuration(),
                    endTimeAsString);
        } else {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getType(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    startTimeAsString,
                    task.getDuration(),
                    endTimeAsString);
        }
    }

    /* метод fromString() принимает строку с параметрами задачи и на ее базе создает задачу */
    private Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        Types type = Types.valueOf(split[1]);
        String name = split[2];
        String description = split[4];
        LocalDateTime startTime = !split[5].equals("null") ? LocalDateTime.parse(split[5], formatter) : null;
        int duration = !split[6].equals("null") ? Integer.parseInt(split[6]) : 0;

        if (type == Types.SUBTASK) {
            int epicID = Integer.parseInt(split[8]);
            Subtask subtask = new Subtask(name, description, epicID);
            subtask.setId(id);
            if (startTime != null) {
                subtask.setStartTime(startTime);
            }
            if (duration != 0) {
                subtask.setDuration(duration);
            }
            Epic epic = epics.get(subtask.getSubtaskEpicID());
            List<Integer> subtasksID = epic.getSubtasksID();
            subtasksID.add(subtask.getId());
            return subtask;
        } else if (type == Types.TASK) {
            Task task = new Task(name, description);
            task.setId(id);
            if (startTime != null) {
                task.setStartTime(startTime);
            }
            if (duration != 0) {
                task.setDuration(duration);
            }
            return task;
        } else if (type == Types.EPIC) {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            return epic;
        } else {
            return null;
        }
    }
}

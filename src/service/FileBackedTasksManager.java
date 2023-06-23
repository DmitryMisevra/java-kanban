package service;

import helpers.Types;
import module.Epic;
import module.Subtask;
import module.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    private String fileName; /* имя и путь файла */

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
        try  {
            String data = Files.readString(Path.of(path));
            String[] tasksFromFile = data.split("\n");
            for (int i = 1; i < tasksFromFile.length; i++) {
                if(tasksFromFile[i].equals("")) {
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
            out.write("id,type,name,status,description,epic" + "\n");
            Map<Integer, Task> allTasks = new HashMap<>();
            allTasks.putAll(tasks);
            allTasks.putAll(subtasks);
            allTasks.putAll(epics);
            for (int i = 1; i <= allTasks.size(); i++) {
                out.write(taskToString(allTasks.get(i)) + "\n");
            }
            out.write("\n");
            out.write(historyToString(historyManager));
        } catch (IOException e) {
                throw new ManagerSaveException("Ошибка записи в файл: " + e.getMessage());
        }
    }

    /* метод toString(Task task) сохраняет параметры задачи в строку для ее дальнейшего сохранения в файл  */
    private String taskToString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%s,%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getType(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescription(),
                    subtask.getSubtaskEpicID());
        } else {
            return String.format("%s,%s,%s,%s,%s",
                    task.getId(),
                    task.getType(),
                    task.getName(),
                    task.getStatus(),
                    task.getDescription());
        }
    }

    /* метод fromString() принимает строку с параметрами задачи и на ее базе создает задачу */
    private Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        Types type = Types.valueOf(split[1]);
        String name = split[2];
        String description = split[4];

        if (type == Types.SUBTASK) {
            int epicID = Integer.parseInt(split[5]);
            Subtask subtask = new Subtask(name, description, epicID);
            subtask.setId(id);
            Epic epic = epics.get(subtask.getSubtaskEpicID());
            List<Integer> subtasksID = epic.getSubtasksID();
            subtasksID.add(subtask.getId());
            return subtask;
        } else if (type == Types.TASK) {
            Task task = new Task(name, description);
            task.setId(id);
            return task;
        } else if (type == Types.EPIC) {
            Epic epic = new Epic(name, description);
            epic.setId(id);
            return epic;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {

        /* проверка работы */
        /* зададим путь к файлу и создадим задачи */
        String path = "src/data/tasks.csv";
        FileBackedTasksManager managerOne = new FileBackedTasksManager(path);

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

        /* вызовем задачи в произвольном порядке */

        managerOne.getTaskByID(1);
        managerOne.getTaskByID(2);
        managerOne.getEpicTaskByID(3);
        managerOne.getEpicTaskByID(4);
        managerOne.getSubtaskByID(5);
        managerOne.getSubtaskByID(6);
        managerOne.getSubtaskByID(7);
        managerOne.getTaskByID(1);
        managerOne.getTaskByID(2);
        managerOne.getEpicTaskByID(3);
        managerOne.getSubtaskByID(5);
        managerOne.getSubtaskByID(5);
        managerOne.getSubtaskByID(6);
        managerOne.getSubtaskByID(5);
        managerOne.getSubtaskByID(6);
        managerOne.getSubtaskByID(7);
        managerOne.getEpicTaskByID(3);
        managerOne.getEpicTaskByID(4);

        System.out.println();

        /* распечатаем историю просмотра */

        System.out.println("История просмотров первого менеджера");
        for (Task task : managerOne.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        /* создадим второй менеджер и распечатаем его историю просмотра */

        FileBackedTasksManager managerTwo = loadFromFile(path);

        System.out.println("История просмотров второго менеджера");
        for (Task task : managerTwo.getHistory()) {
            System.out.println(task);
        }
        System.out.println();
    }
}

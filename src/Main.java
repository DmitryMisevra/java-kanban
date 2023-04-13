import helpers.Managers;
import helpers.Statuses;
import module.Epic;
import module.Task;
import module.Subtask;
import service.HistoryManager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        // создадим объекты необходимых классов;
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();


        // создадим задачи трех видов и добавим их в соответствующие списки
        Task taskOne = new Task("Задача №1", "Это простая задача");
        Task taskTwo = new Task("Задача №2", "Еще одна простая задача");

        Epic epicOne = new Epic("Эпик №1", "У этого эпика 1 подзадача");
        Epic epicTwo = new Epic("Эпик №2", "У этого эпика 2 подзадачи");

        Subtask subtaskOne = new Subtask("Подзадача 1.1", "эта подзадача " +
                "принадлежит эпику №1", 3);
        Subtask subtaskTwo = new Subtask("Подзадача 2.1", "эта подзадача " +
                "принадлежит эпику №2", 4);
        Subtask subtaskThree = new Subtask("Подзадача 2.2", "эта подзадача " +
                "принадлежит эпику №2", 4);

        taskManager.createTask(taskOne);
        taskManager.createTask(taskTwo);

        taskManager.createEpicTask(epicOne);
        taskManager.createEpicTask(epicTwo);

        taskManager.createSubtask(subtaskOne);

        taskManager.createSubtask(subtaskTwo);
        taskManager.createSubtask(subtaskThree);

        System.out.println();

//         проверим состояние с помощью toString()
        System.out.println("Состояние объектов сразу после создания");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println();

//         напечатаем списки подзадач эпиков
        System.out.println("Печать списков подзадач эпиков");
        System.out.println("Эпик 1:");
        System.out.println(taskManager.getSubtaskListByEpic(3));
        System.out.println("Эпик 2:");
        System.out.println(taskManager.getSubtaskListByEpic(4));
        System.out.println();

        System.out.println("Вызовем несколько раз подряд задачи");
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
        taskManager.getSubtaskByID(5);
        taskManager.getSubtaskByID(5);
        taskManager.getSubtaskByID(6);
        taskManager.getSubtaskByID(5);
        taskManager.getSubtaskByID(6);
        taskManager.getSubtaskByID(7);
        taskManager.getEpicTaskByID(3);
        taskManager.getEpicTaskByID(4);

        System.out.println();

        System.out.println("Распечатаем историю просмотров");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();


        simpleTaskOne.setStatus("IN_PROGRESS");
        taskManager.updateSimpleTask(simpleTaskOne);

        simpleTaskTwo.setStatus("DONE");
        taskManager.updateSimpleTask(simpleTaskTwo);

        subtaskOne.setStatus("DONE");
        taskManager.updateSubtask(subtaskOne);

        subtaskTwo.setStatus("IN_PROGRESS");
        taskManager.updateSubtask(subtaskTwo);
        subtaskThree.setStatus("DONE");
        taskManager.updateSubtask(subtaskThree);


        System.out.println("Состояние объектов после изменения статусов");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println();

        // удалим некоторые задачи
        taskManager.removeTaskByID(1);
        taskManager.removeEpicTaskByID(3);
        taskManager.removeSubtaskByID(6);

        System.out.println("Состояние объектов после удаления части задач");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println();

        // и наконец очистим все списки с задачами
        taskManager.clearTaskList();
        taskManager.clearSubTaskList();
        taskManager.clearEpicTaskList();

        System.out.println("Состояние объектов после очищения списков");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println();
    }
}

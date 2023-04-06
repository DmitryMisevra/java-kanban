import module.Epic;
import module.SimpleTask;
import module.Subtask;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        SimpleTask simpleTaskOne = new SimpleTask("Задача №1", "Это простая задача");
        SimpleTask simpleTaskTwo = new SimpleTask("Задача №2", "Еще одна простая задача");

        Epic epicOne = new Epic("Эпик №1", "У этого эпика 1 подзадача");
        Epic epicTwo = new Epic("Эпик №2", "У этого эпика 2 подзадачи");

        Subtask subtaskOne = new Subtask("Подзадача 1", "эта подзадача " +
                "принадлежит эпику №1", 3);
        Subtask subtaskTwo = new Subtask("Подзадача 1", "эта подзадача " +
                "принадлежит эпику №2", 4);
        Subtask subtaskThree = new Subtask("Подзадача 3", "эта подзадача " +
                "принадлежит эпику №2", 4);

        taskManager.createSimpleTask(simpleTaskOne);
        taskManager.createSimpleTask(simpleTaskTwo);

        taskManager.createEpicTask(epicOne);
        taskManager.createEpicTask(epicTwo);

        taskManager.createSubtask(subtaskOne);

        taskManager.createSubtask(subtaskTwo);
        taskManager.createSubtask(subtaskThree);


        System.out.println("Состояние объектов сразу после создания");
        for (SimpleTask simpleTask : taskManager.getSimpleTasks().values()) {
            System.out.println(simpleTask);
        }
        for (Epic epic : taskManager.getEpics().values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks().values()) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("Печать списков подзадач эпиков");
        System.out.println("Эпик 1:");
        System.out.println(taskManager.getSubtaskListByEpic(3));
        System.out.println("Эпик 2:");
        System.out.println(taskManager.getSubtaskListByEpic(4));

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
        for (SimpleTask simpleTask : taskManager.getSimpleTasks().values()) {
            System.out.println(simpleTask);
        }
        for (Epic epic : taskManager.getEpics().values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks().values()) {
            System.out.println(subtask);
        }
        System.out.println();

        taskManager.removeSimpleTaskByID(1);
        taskManager.removeEpicTaskByID(3);
        taskManager.removeSubtaskByID(6);

        System.out.println("Состояние объектов после удаления части задач");
        for (SimpleTask simpleTask : taskManager.getSimpleTasks().values()) {
            System.out.println(simpleTask);
        }
        for (Epic epic : taskManager.getEpics().values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks().values()) {
            System.out.println(subtask);
        }
        System.out.println();

        taskManager.clearSimpleTaskList();
        taskManager.clearSubTaskList();
        taskManager.clearEpicTaskList();

        System.out.println("Состояние объектов после очищения списков");
        for (SimpleTask simpleTask : taskManager.getSimpleTasks().values()) {
            System.out.println(simpleTask);
        }
        for (Epic epic : taskManager.getEpics().values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : taskManager.getSubtasks().values()) {
            System.out.println(subtask);
        }
        System.out.println();
    }
}

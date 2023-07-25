package service;

import helpers.Managers;
import helpers.Statuses;
import module.Epic;
import module.Task;
import module.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int idCounter = 1; /* счетчик id */
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    /* для хранения эпиков, задач и подзадач используем HashMap */
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();

    /* для хранения отсортированных по дате времени задач используем список tasksSortedByStartTime*/
    protected Set<Task> tasksSortedByStartTime =
            new TreeSet<>(Comparator.comparing(
                    Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId));

    /* для хранения 15 мин интервалов на год вперед используем мапу timetable*/
    protected Map<LocalDateTime, Boolean> timetable = new HashMap<>();

    /* В конструкторе мы сразу заполняем timetable интервалами 15 мин на год вперед */
    public InMemoryTaskManager() {
        LocalDateTime startCounter = LocalDateTime.of(2023, 6, 22, 0, 0);
        LocalDateTime endCounter = startCounter.plusYears(1);
        while (startCounter.isBefore(endCounter)) {
            timetable.put(startCounter, true);
            startCounter = startCounter.plusMinutes(15);
        }
    }

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

    /* clearTasklist() очищает мапу с простыми задачами и освобождает расписание */
    @Override
    public void clearTaskList() {
        getTasks().forEach(tasksSortedByStartTime::remove);
        getTasks().forEach(this::clearIntervalsFromTimetable);
        tasks.clear();
    }

    /* clearSubtasklist() очищает мапу с подзадачами и освобождает расписание
    Также метод обновляет списки подзадач и статусы внутри епиков */
    @Override
    public void clearSubTaskList() {
        getSubtasks().forEach(tasksSortedByStartTime::remove);
        getSubtasks().forEach(this::clearIntervalsFromTimetable);
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
        if (tasks.containsKey(requestedID)) {
            historyManager.add(tasks.get(requestedID));
            return tasks.get(requestedID);
        } else {
            throw new IllegalArgumentException("Задачи с таким id нет в списке");
        }
    }

    /* getEpicTaskByID возвращает эпик по его id */
    @Override
    public Task getEpicTaskByID(int requestedID) {
        if (epics.containsKey(requestedID)) {
            historyManager.add(epics.get(requestedID));
            return epics.get(requestedID);
        } else {
            throw new IllegalArgumentException("Эпика с таким id нет в списке");
        }
    }

    /* Метод getSubtaskByID возвращает подзадачу по ее id */
    @Override
    public Task getSubtaskByID(int requestedID) {
        if (subtasks.containsKey(requestedID)) {
            historyManager.add(subtasks.get(requestedID));
            return subtasks.get(requestedID);
        } else {
            throw new IllegalArgumentException("Подзадачи с таким id нет в списке");
        }
    }

    /* createSimpleTask создает новую простую задачу и возвращает ее */

    @Override
    public Task createTask(Task task) {
        task.setId(idCounter);
        idCounter++;
        tasks.put(task.getId(), task);
        tasksSortedByStartTime.add(task);
        checkTimetable(task);
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
     * Также метод добавляет подзадачу к требуемому эпику в
     * список подзадач и обновляет его статус
     * Отдельно метод добавляет задачу в сортированный по времени список и проверяет расписание*/
    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getSubtaskEpicID())) {
            subtask.setId(idCounter);
            idCounter++;
            subtasks.put(subtask.getId(), subtask);
            tasksSortedByStartTime.add(subtask);
            checkTimetable(subtask);

            Epic epic = epics.get(subtask.getSubtaskEpicID());
            List<Integer> subtasksID = epic.getSubtasksID();
            subtasksID.add(subtask.getId());
            updateEpic(epic);
            return subtask;
        } else {
            throw new IllegalArgumentException("Невозможно создать подзадачу. Эпика с id " + subtask.getSubtaskEpicID()
                    + " нет в списке");
        }
    }

    /* updateSimpleTask обновляет простую задачу
     * Отдельно метод обновляет задачу в сортированном по времени списке и перепроверяет расписание*/
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            tasksSortedByStartTime.remove(task);
            tasksSortedByStartTime.add(task);
            checkTimetable(task);
        } else {
            throw new IllegalArgumentException("такой задачи нет в списке");
        }
    }

    /* updateEpic обновляет эпик*/
    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            updateEpicStatus(epic.getId());
            epic.setStartTime(getEpicStartTime(epic));
            epic.setEndTime(getEpicEndTime(epic));
            epic.setDuration(getEpicDuration(epic));
            epics.put(epic.getId(), epic);
        } else {
            throw new IllegalArgumentException("такого эпика нет в списке");
        }
    }

    /* updateSubtask обновляет подзадачу
     * Также метод обновляет статус эпика, к которому относится подзадача
     * Отдельно метод обновляет задачу в сортированном по времени списке и перепроверяет расписание*/
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            tasksSortedByStartTime.remove(subtask);
            tasksSortedByStartTime.add(subtask);
            checkTimetable(subtask);

            Epic epic = epics.get(subtask.getSubtaskEpicID());
            updateEpic(epic);
        } else {
            throw new IllegalArgumentException("такой задачи нет в списке");
        }
    }

    /* removeTaskByID находит задачу по ее id, удаляет ее и возвращает удаленный объект
     * также метод освобождает расписание и удаляет задачу из списка отсортированного по времени*/
    @Override
    public Task removeTaskByID(int requestedID) {
        if (tasks.containsKey(requestedID)) {
            tasksSortedByStartTime.remove(getTaskByID(requestedID));
            historyManager.remove(requestedID);
            clearIntervalsFromTimetable(tasks.get(requestedID));
            return tasks.remove(requestedID);
        } else {
            throw new IllegalArgumentException("Невозможно удалить задачу. Задачи с таким id нет в списке");
        }
    }

    /* removeEpicTaskByID находит эпик по его id, удаляет его и возвращает удаленный объект
    Также метод удаляет все подзадачи, относящиеся к этому эпику */
    @Override
    public Epic removeEpicTaskByID(int requestedID) {
        if (epics.containsKey(requestedID)) {
            Epic removedEpic = epics.remove(requestedID);
            List<Integer> subtasksID = removedEpic.getSubtasksID();
            for (Integer subtaskID : subtasksID) {
                subtasks.remove(subtaskID);
                historyManager.remove(subtaskID);
            }
            historyManager.remove(requestedID);
            return removedEpic;
        } else {
            throw new IllegalArgumentException("Невозможно удалить эпик. Эпика с таким id нет в списке");
        }
    }

    /* removeSubtaskByID находит подзадачу по ее id, удаляет ее и возвращает удаленный объект
     * Также метод удаляет подзадачу из списка подзадач эпика, к которому она принадлежала и
     * обновляет статус эпика
     * также метод освобождает расписание и удаляет задачу из списка отсортированного по времени*/
    @Override
    public Subtask removeSubtaskByID(int requestedID) {
        if (subtasks.containsKey(requestedID)) {
            Subtask requestedSubtask = subtasks.get(requestedID);
            clearIntervalsFromTimetable(requestedSubtask);
            tasksSortedByStartTime.remove(requestedSubtask);

            Epic epic = epics.get(requestedSubtask.getSubtaskEpicID());
            List<Integer> subtasksID = epic.getSubtasksID();
            subtasksID.remove(Integer.valueOf(requestedSubtask.getId()));
            updateEpic(epic);

            historyManager.remove(requestedID);

            return subtasks.remove(requestedID);
        } else {
            throw new IllegalArgumentException("Невозможно удалить подзадачу. Подзадачи с таким с таким id нет в " +
                    "списке");
        }
    }

    /* getSubtaskListByEpic возвращает список подзадач запрашиваемого эпика */
    @Override
    public List<Subtask> getSubtaskListByEpic(int epicID) {
        if (epics.containsKey(epicID)) {
            List<Subtask> subtasksListByEpic = new ArrayList<>();

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
        } else {
            return Collections.emptyList();
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksSortedByStartTime);
    }

    /* метод возвращает время старта для эпика */
    private LocalDateTime getEpicStartTime(Epic epic) {
        List<Subtask> subtasksEpicList = getSubtaskListByEpic(epic.getId());
        return subtasksEpicList.stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    /* метод возвращает время окончания задачи для эпика */
    private LocalDateTime getEpicEndTime(Epic epic) {
        List<Subtask> subtasksEpicList = getSubtaskListByEpic(epic.getId());
        return subtasksEpicList.stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    /* метод возвращает продолжительность эпика */
    private int getEpicDuration(Epic epic) {
        if (epic.getStartTime() != null && epic.getEndTime() != null) {
            return (int) Duration.between(epic.getStartTime(), epic.getEndTime()).toMinutes();
        }
        return 0;
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

    /* метод проверяет расписание и если оно свободно - заполняет блоки, которые заняты задачей */
    private void checkTimetable(Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null) {
            int minutes = task.getStartTime().getMinute();
            List<LocalDateTime> taskIntervals = new ArrayList<>();

            LocalDateTime checkedInterval = task.getStartTime().minusMinutes(minutes % 15);
            LocalDateTime boundaryInterval = task.getEndTime().plusMinutes(15 - minutes % 15);

            if (checkedInterval.isBefore(LocalDateTime.of(2023, 6, 22, 0, 0))) {
                throw new IllegalArgumentException("время старта задачи должно быть начиная с 22.06.2023 г.");
            }

            if (checkedInterval.isAfter(LocalDateTime.of(2024, 6, 22, 23, 59))) {
                throw new IllegalArgumentException("время старта задачи должно быть не позднее 22.06.2024г. 23:59");
            }

            if (boundaryInterval.isAfter(LocalDateTime.of(2024, 6, 22, 0, 0))) {
                throw new IllegalArgumentException("время окончания задачи должно быть до 22.06.2024 г.");
            }

            while (checkedInterval.isBefore(boundaryInterval)) {
                if (timetable.get(checkedInterval).equals(true)) {
                    timetable.put(checkedInterval, false);
                    taskIntervals.add(LocalDateTime.from(checkedInterval));
                    checkedInterval = checkedInterval.plusMinutes(15);
                } else {
                    throw new IllegalArgumentException("текущее время занято");
                }
            }
            task.setIntervals(taskIntervals);
        }
    }

    /* метод проверяет расписание и освобождает блоки, занятые задачей */
    private void clearIntervalsFromTimetable(Task task) {
        if (!task.getIntervals().isEmpty()) {
            task.getIntervals().forEach(interval -> timetable.put(interval, true));
        }
    }
}


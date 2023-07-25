package module;

import helpers.Statuses;
import helpers.Types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {

    /* Атрибуты класса простой задачи - id, имя, описание, статус */
    protected int id;
    protected String name;
    protected String description;
    protected Statuses status = Statuses.NEW; /* статус по умолчанию при создании новой задачи */
    protected int duration;
    protected LocalDateTime startTime;

    protected List<LocalDateTime> intervals = new ArrayList<>();

    protected Types type;

    public List<LocalDateTime> getIntervals() {
        return intervals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime) && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, duration, startTime, intervals, type);
    }

    public void setIntervals(List<LocalDateTime> intervals) {
        this.intervals = intervals;
    }

    public Types getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        type = Types.TASK;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != 0) {
            return startTime.plus(Duration.ofMinutes(duration));
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }
}

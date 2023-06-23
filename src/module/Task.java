package module;

import helpers.Statuses;
import helpers.Types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Task {

    /* Атрибуты класса простой задачи - id, имя, описание, статус */
    protected int id;
    protected String name;
    protected String description;
    protected Statuses status = Statuses.NEW; /* статус по умолчанию при создании новой задачи */
    protected int duration;
    protected LocalDateTime startTime;

    protected List<LocalDateTime> intervals = new ArrayList<>();

    private final Types type = Types.TASK;

    public List<LocalDateTime> getIntervals() {
        return intervals;
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

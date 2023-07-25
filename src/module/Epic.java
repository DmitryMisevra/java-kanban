package module;

import helpers.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected LocalDateTime endTime;

    private final Types type = Types.EPIC;

    private List<Integer> subtasksID = new ArrayList<>(); /* эпик хранит список подзадач, которые ему
    принадлежат */

    public List<Integer> getSubtasksID() {
        return subtasksID;
    }

    @Override
    public Types getType() {
        return type;
    }

    public void setSubtasksID(List<Integer> subtasksID) {
        this.subtasksID = subtasksID;
    }

    public Epic(String name, String description) {
        super(name, description);
        startTime = getStartTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksID=" + subtasksID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(endTime, epic.endTime) && type == epic.type && Objects.equals(subtasksID, epic.subtasksID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), endTime, type, subtasksID);
    }
}



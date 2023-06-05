package module;

import helpers.Types;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

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
}



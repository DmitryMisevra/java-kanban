package module;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtasksID = new ArrayList<>(); /* эпик хранит списко подзадач, которые ему
    принадлежат */

    public List<Integer> getSubtasksID() {
        return subtasksID;
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



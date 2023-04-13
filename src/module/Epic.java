package module;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksID = new ArrayList<>(); // эпик хранит списко подзадач, которые ему принадлежат

    public ArrayList<Integer> getSubtasksID() {
        return subtasksID;
    }

    public void setSubtasksID(ArrayList<Integer> subtasksID) {
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



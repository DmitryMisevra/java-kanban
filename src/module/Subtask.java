package module;

public class Subtask extends SimpleTask {

    private int subtaskEpicID;

    public int getSubtaskEpicID() {
        return subtaskEpicID;
    }

    public void setSubtaskEpicID(int subtaskEpicID) {
        this.subtaskEpicID = subtaskEpicID;
    }

    public Subtask(String name, String description, int subtaskEpicID) {
        super(name, description);
        this.subtaskEpicID = subtaskEpicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskEpicID=" + subtaskEpicID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

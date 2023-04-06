package module;

public class Subtask extends SimpleTask {
    // класс подзадачи наследует атрибуты и свойства простой задачи

    private int subtaskEpicID; // позадача будет хранить id эпика, которому она принадлежит

    public int getSubtaskEpicID() {
        return subtaskEpicID;
    }

    public void setSubtaskEpicID(int subtaskEpicID) {
        this.subtaskEpicID = subtaskEpicID;
    }

    // в параметр конструктора добавлено поле с id эпика
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

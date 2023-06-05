package module;

import helpers.Types;

public class Subtask extends Task {

    /* класс подзадачи наследует атрибуты и свойства простой задачи */

    private int subtaskEpicID; /* позадача будет хранить id эпика, которому она принадлежит */

    private final Types type = Types.SUBTASK;

    @Override
    public Types getType() {
        return type;
    }

    public int getSubtaskEpicID() {
        return subtaskEpicID;
    }

    public void setSubtaskEpicID(int subtaskEpicID) {
        this.subtaskEpicID = subtaskEpicID;
    }

    /* в параметр конструктора добавлено поле с id эпика */
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

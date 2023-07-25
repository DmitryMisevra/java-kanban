package module;

import helpers.Types;

import java.util.Objects;

public class Subtask extends Task {

    /* класс подзадачи наследует атрибуты и свойства простой задачи */

    private int subtaskEpicID; /* позадача будет хранить id эпика, которому она принадлежит */

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
        type = Types.SUBTASK;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return subtaskEpicID == subtask.subtaskEpicID && type == subtask.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskEpicID, type);
    }
}

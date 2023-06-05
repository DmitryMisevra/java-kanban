package service;

import module.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    /* список history хранит историю 10 последних просмотренных задач */
    private CustomLinkedList<Task> history = new CustomLinkedList<>();

    /* мапа historyMap помогает найти нужную задачу за О(1) */
    private Map<Integer, Node<Task>> historyMap = new HashMap<>();

    /* CustomLinkedList хранит историю просмотров в порядке вызова задач */
    class CustomLinkedList<T extends Task> {

        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        /* метод linkLast добавляет задачу в конец списка и возвращает ноду, созданную на базе этой задачи */
        public Node<T> linkLast(T task) {

            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(task, null, oldTail);
            tail = newNode;

            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;
            return newNode;
        }

        /* метод removeNode удаляет из цепочки узлов требуемую ноду, перезаписывая ссылки у смежных нод */
        public void removeNode(Node<T> node) {
            if (node == head) {
                head = node.next;
                head.prev = null;
            } else if (node == tail) {
                tail = node.prev;
                tail.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            size--;
        }

        /* метод getTasks() формирует ArrayList из задач, хранящихся в CustomLinkedList */
        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();

            /* далее итерируемся по CustomLinkedList, переходя по ссылкам next предыдущей ноды, пока не пройдем по
            всему списку */
            if (head != null) {
                Node<T> currentNode = head;
                do {
                    tasks.add(currentNode.task);
                    currentNode = currentNode.next;
                } while (currentNode != null);
            }
            return tasks;
        }
    }

    /* в методе add() мы сначала проверяем наличие повторов задач и если есть, удаляем их ноды. Затем добавляем
    задачу в конец списка */
    @Override
    public void add(Task task) {
        int id = task.getId();
        if (historyMap.containsKey(id)) {
            history.removeNode(historyMap.get(id));
        }
        historyMap.put(id, history.linkLast(task));
    }

    @Override
    public void remove(int id) {
        history.removeNode(historyMap.get(id));
        historyMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }
}

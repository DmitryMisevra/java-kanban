package service;

import module.Task;

public class Node<T extends Task> {

    public T task;
    public Node<T> next;
    public Node<T> prev;

    public Node(T task, Node<T> next, Node<T> prev) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}

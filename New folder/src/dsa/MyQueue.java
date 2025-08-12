package dsa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * Simple linked-list based queue.
 * - enqueue/dequeue are O(1) for plain FIFO usage.
 * - enqueueWithPriority inserts in sorted order using the provided comparator (O(n)).
 */

public class MyQueue<T> {
    private Node<T> front, rear;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (rear != null) rear.next = newNode;
        rear = newNode;
        if (front == null) front = newNode;
        size++;
    }

    /**
     * Enqueue item in order based on priority comparator
     */
    public void enqueueWithPriority(T data, Comparator<T> comparator) {
        Node<T> newNode = new Node<>(data);

        if (front == null) { // queue empty
            front = rear = newNode;
        } else if (comparator.compare(data, front.data) < 0) {
            // insert at front
            newNode.next = front;
            front = newNode;
        } else {
            // find correct spot
            Node<T> current = front;
            while (current.next != null && comparator.compare(data, current.next.data) >= 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
            if (newNode.next == null) rear = newNode; // update rear if inserted at end
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) return null;
        T data = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return data;
    }

    public T peek() {
        return (isEmpty()) ? null : front.data;
    }

    public boolean isEmpty() { 
        return size == 0; 
    }

    public int size() { 
        return size; 
    }

    /**
     * Converts queue contents into a List for easy iteration
     */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = front;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}

package dsa;

import java.util.ArrayList;
import java.util.List;
/**
 * Simple generic Binary Search Tree.
 * T must be Comparable because we use compareTo() to order nodes.
 *
 * NOTE: This is an *unbalanced* BST. Average operations: O(log n).
 * Worst-case (degenerate tree): O(n).
 */

public class MyBST<T extends Comparable<T>> {
    private Node<T> root;

    private static class Node<T> {
        T data;
        Node<T> left, right;
        Node(T data) { this.data = data; }
    }

    // Insert into BST
    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node<T> insertRec(Node<T> node, T data) {
        if (node == null) return new Node<>(data);
        if (data.compareTo(node.data) < 0) node.left = insertRec(node.left, data);
        else if (data.compareTo(node.data) > 0) node.right = insertRec(node.right, data);
        // duplicates ignored (no change)
        return node;
    }

    // Delete from BST
    public void delete(T data) {
        root = deleteRec(root, data);
    }
    /** Recursive insertion: walk left/right until place found. */
    /**
     * Recursive delete:
     * - If node has no children -> return null
     * - If one child -> return that child
     * - If two children -> replace node.data with inorder successor (min from right subtree),
     *   then delete that successor node recursively.
     */
    private Node<T> deleteRec(Node<T> node, T data) {
        if (node == null) return null;
        if (data.compareTo(node.data) < 0) node.left = deleteRec(node.left, data);
        else if (data.compareTo(node.data) > 0) node.right = deleteRec(node.right, data);
        else {
        	 // found node to delete
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // two children: get inorder successor
            node.data = findMin(node.right);
            node.right = deleteRec(node.right, node.data);
        }
        
        return node;
    }
    /** Find minimum value in subtree (leftmost node) */
    private T findMin(Node<T> node) {
        while (node.left != null) node = node.left;
        return node.data;
    }

    // Return sorted list (in-order traversal)
    public List<T> inOrderList() {
        List<T> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(Node<T> node, List<T> result) {
        if (node != null) {
            inOrderRec(node.left, result);
            result.add(node.data);
            inOrderRec(node.right, result);
        }
    }
}

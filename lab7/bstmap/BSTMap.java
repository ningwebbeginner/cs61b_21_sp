package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left, right;
        private int size;

        Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }

        @Override
        public String toString() {
            return key.toString() + " -> " + value.toString() + " " + size + "\n";
        }
    }

    public BSTMap() {
        root = null;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(key, root);
    }

    private boolean containsKey(K key, Node node) {
        if (node == null) {
            return false;
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            containsKey(key, node.left);
        }
        if (compare > 0) {
            containsKey(key, node.right);
        }
        return true;
    }

    @Override
    public V get(K key) {
        return getKey(key, root);
    }

    private V getKey(K key, Node node) {
        if (node == null) {
            return null;
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            return getKey(key, node.left);
        }
        if (compare > 0) {
            return getKey(key, node.right);
        }
        return node.value;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
    }

    @Override
    public void put(K key, V value) {
        root = put(key, value, root);
    }

    private Node put(K key, V value, Node node) {
        if (node == null) return new Node(key, value, 1);
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            node.left = put(key, value, node.left);
        } else if (compare > 0) {
            node.right = put(key, value, node.right);
        } else {
            node.value = value;
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;

    }


    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }


    public void printInOrder() {
        String result = "";
        printNode(root);
    }

    private String printNode(Node node) {
        if(node == null) {
            return null;
        }
        System.out.println(node);
        printNode(node.left);
        printNode(node.right);
        return null;
        }


}

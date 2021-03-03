package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;
    private class Node {
        private K key;
        private V value;
        private Node left,right;
        private int size;
        Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
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
        return false;
    }

    @Override
    public V get(K key) {
        return null;
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
        if (value == null) {
            remove(key);
        }
        else {
            root = put(key, value, root);
        }

    }

    private Node put(K key, V value, Node node) {
        if (node == null) return new Node(key, value, 1);
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            node.left = put(key, value, node.left);
        }
        else if (compare > 0) {
            node.right = put(key, value, node.right);
        }
        else {
            node.value = value;
            return node;
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;

    }



    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }






}

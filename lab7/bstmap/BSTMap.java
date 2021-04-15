package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
        return getKey(key, root).value;
    }

    private Node getKey(K key, Node node) {
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
        return node;
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
        Set<K> keySet = new TreeSet<>();
        if(root == null) {
            return keySet;
        }
        else {
            return keySet(keySet, root);
        }
    }

    private Set<K> keySet(Set<K> keySet, Node node) {
        if(node == null) {
            return keySet;
        }
        keySet.add(node.key);
        keySet(keySet, node.left);
        keySet(keySet, node.right);
        return keySet;
    }


    @Override
    public V remove(K key) {
         V value = get(key);
         root = remove(key, root);
         return value;

    }

    private Node remove(K key, Node node) {
        if(node == null) {
            return null;
        }
        int compare = key.compareTo(node.key);
        if(compare < 0) {
            node.left = remove(key, node.left);
        }
        else if(compare > 0) {
            node.right = remove(key, node.right);
        }
        else {
            if(node.left == null)     return node.right;
            if(node.right == null)    return node.left;
            Node temp = node;
            node = max(node.left);
            node.left = removeMax(node.left);
            node.right = temp.right;
        }
        node.size = size(node.left) + size(node.right) + 1;
        return node;

    }



    private Node removeMax(Node node) {
        if(node.right == null) {
            return node.left;
        }
        node.right = removeMax(node.right);
        node.size = size(node.left)+ size(node.right) + 1;
        return node;
    }

    private Node max(Node root) {
        if(root.right == null) {
            return root;
        }
        return max(root.right);
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

    private void printNode(Node node) {
        if(node == null) {
            return;
        }
        printNode(node.left);
        System.out.println(node);
        printNode(node.right);
        }


}

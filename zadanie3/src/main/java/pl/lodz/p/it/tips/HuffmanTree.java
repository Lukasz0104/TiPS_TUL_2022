package pl.lodz.p.it.tips;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import lombok.Getter;

public class HuffmanTree {

    /**
     * Builds Huffman Tree for given string.
     *
     * @param text
     * @return Root of the tree.
     */
    public static Node buildTree(String text) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : text.getBytes(StandardCharsets.UTF_8)) {
            if (freq.containsKey(b)) {
                Integer i = freq.get(b);
                freq.put(b, i + 1);
            } else {
                freq.put(b, 1);
            }
        }

        for (Map.Entry<Byte, Integer> entry : freq.entrySet()) {
            queue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();

            Node newNode = new Node(left, right);
            queue.add(newNode);
        }

        return queue.poll();
    }

    public static String decode() {


        return null;
    }

    public static class Node implements Comparable<Node> {
        @Getter
        private final int value;

        @Getter
        private byte character = 0;

        @Getter
        private Node left = null;

        @Getter
        private Node right = null;

        public Node(byte character, int value) {
            this.character = character;
            this.value = value;
        }

        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.value = left.value + right.value;
        }

        @Override
        public int compareTo(Node other) {
            int difference = this.value - other.value;

            if (difference != 0) {
                return difference;
            }

            if (this.character == 0 && other.character == 0) {
                return -1;
            }

            if (this.character == 0) {
                return 1;
            }

            if (other.character == 0) {
                return -1;
            }

            return this.character - other.character;
        }
    }
}

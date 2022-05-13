package pl.lodz.p.it.tips;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Stack;
import lombok.Getter;

public class HuffmanTree {

    /**
     * Builds Huffman Tree for given string.
     *
     * @param text
     * @return Root of the tree.
     */
    public static Node buildTree(String text) {
        // get frequency of each byte
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : text.getBytes(StandardCharsets.UTF_8)) {
            if (freq.containsKey(b)) {
                Integer i = freq.get(b);
                freq.put(b, i + 1);
            } else {
                freq.put(b, 1);
            }
        }

        // build up priority queue
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : freq.entrySet()) {
            queue.add(new Node(entry.getKey(), entry.getValue()));
        }

        // build the tree
        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();

            Node newNode = new Node(left, right);
            queue.add(newNode);
        }
        // return root node
        return queue.poll();
    }

    public static Node readTree(byte[] header) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(header)) {
            int b; // variable used to store byte read from the buffer

            int length = 0; // length of the original text
            for (int i = 0; i < 4; i++) {
                b = bais.read();
                length <<= 1;
                length |= b;
            }

            int numberOfCharacters = 0;
            for (int i = 0; i < 4; i++) {
                b = bais.read();
                numberOfCharacters <<= 1;
                numberOfCharacters |= b;
            }

            Stack<Node> nodes = new Stack<>();

            int nodesRead = 0;
            while (nodesRead < numberOfCharacters || nodes.size() > 1) {
                b = bais.read();
                if (b == 1) {
                    byte character = (byte) bais.read();
                    nodes.push(new Node(character));
                    nodesRead++;
                } else {
                    if (nodes.size() < 2) {
                        throw new RuntimeException("Invalid header format");
                    }
                    Node right = nodes.pop();
                    Node left = nodes.pop();
                    nodes.push(new Node(left, right));
                }
            }
            if (nodes.size() != 1) {
                throw new RuntimeException("Invalid header format");
            }
            return nodes.pop();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static byte[] encode(String text) {
        Node root = HuffmanTree.buildTree(text);
        int length = text.length();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {




            return baos.toByteArray();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

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

        /**
         * Constructor used for creating leaf nodes.
         * @param character character (or byte) to store in a leaf
         * @param value number of occurences
         */
        public Node(byte character, int value) {
            this.character = character;
            this.value = value;
        }

        /**
         * Constructor used for building tree from header.
         * @param character character (or byte) to store in a leaf
         */
        public Node(byte character) {
            this(character, 0);
        }

        /**
         * Constructor used for creating non-leaf nodes.
         * @param left Node containing left leaf or subtree
         * @param right Node containing right leaf or subtree
         */
        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.value = left.value + right.value;
        }

        public boolean isLeafNode() {
            return left == null && right == null && character > 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            if (isLeafNode()) {
                return character == node.character;
            } else {
                return left.equals(node.left) && right.equals(node.right);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, character, left, right);
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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (isLeafNode()) {
                sb.append('1').append((char) character);
            } else {
                sb.append(left.toString()).append(right.toString()).append('0');
            }

            return sb.toString();
        }
    }
}

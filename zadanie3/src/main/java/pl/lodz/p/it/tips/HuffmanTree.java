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
     * @param text String to encode.
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

    public static Node decodeTree(byte[] header, int numberOfCharacters) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(header)) {
            int b; // variable used to store byte read from the buffer

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

    public static String decode(byte[] encoded) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(encoded)) {
            StringBuilder sb = new StringBuilder();
            int textLength = 0;
            for (int i = 0; i < 4; i++) {
                textLength = (textLength << 8) | bais.read();
            }

            int numberOfCharacters = 0;
            for (int i = 0; i < 4; i++) {
                numberOfCharacters = (numberOfCharacters << 8) | bais.read();
            }

            byte[] header = new byte[3 * numberOfCharacters - 1];
            bais.readNBytes(header, 0, 3 * numberOfCharacters - 1);

            Node root = HuffmanTree.decodeTree(header, numberOfCharacters);
            if (root == null) {
                throw new RuntimeException("something is wrong");
            }

            int charactersRead = 0;
            short path1 = (short) bais.read();
            short path2 = (short) bais.read();
            int bitsRead = 0;
            int len;
            while (charactersRead < textLength) {
                byte character = root.getByteForPath((byte) (path1 & 0xff));
                len = (root.getPathForCharacter(character) >> 8);
                bitsRead += len;
                sb.append((char) character);
                charactersRead++;

                path1 = (short) ((path1 << len) & 0xff);
                path1 = (short) (path1 | ((path2 & 0xff) >> (8 - len)));
                path2 = (short) ((path2 << len) & 0xff);

                if (bitsRead > 7) {
                    int zeros = bitsRead - 8;
                    bitsRead -= 8;
                    path2 = (short) bais.read();
                    if (path2 == -1) {
                        path2 = 0;
                    }
                    path1 = (short) (path1 | (((path2 >> (8 - zeros)) & 0xff)));
                    path2 = (short) ((path2 << zeros) & 0xff);
                }
            }

            return sb.toString();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static byte[] encode(String text) {
        Node root = HuffmanTree.buildTree(text);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            int length = text.length();
            for (int i = 0; i < 4; i++) {
                baos.write(length & (0xff << ((3 - i) * 8)));
            }

            int characterCount = root.countLeaves();
            for (int i = 0; i < 4; i++) {
                baos.write(characterCount & (0xff << ((3 - i) * 8)));
            }

            // region encode tree
            // Iterative post-order tree traversal using 2 stacks
            // @see https://www.geeksforgeeks.org/iterative-postorder-traversal/
            Stack<Node> first = new Stack<>();
            Stack<Node> second = new Stack<>();

            first.push(root);

            while (!first.empty()) {
                Node node = first.pop();
                second.push(node);
                if (!node.isLeafNode()) {
                    first.push(node.getLeft());
                    first.push(node.getRight());
                }
            }

            while (!second.empty()) {
                Node n = second.pop();
                if (n.isLeafNode()) {
                    baos.write(1);
                    baos.write(n.getCharacter());
                } else {
                    baos.write(0);
                }
            }
            // endregion

            int bits = 0;
            int buff = 0;
            short pathWithLength;
            for (byte b : text.getBytes(StandardCharsets.UTF_8)) {
                pathWithLength = root.getPathForCharacter(b);
                int len = (pathWithLength & 0xff00) >> 8;
                short path = (short) (pathWithLength & 0xff);

                if (bits + len <= 8) {
                    buff = ((buff << len) | (path >> (8 - len)));
                    bits += len;
                    if (bits == 8) {
                        baos.write(buff);
                        buff = 0;
                        bits = 0;
                    }
                } else {
                    int c = bits + len - 8;
                    for (int i = 0; i < c; i++) {
                        buff = ((buff << 1) | ((path >> (7 - i)) & 1));
                    }
                    baos.write(buff);
                    bits = 0;
                    buff = 0;
                    int remaining = len - c;
                    for (int i = 0; i < remaining; i++) {
                        buff = ((buff << 1) | ((path >> (7 - remaining - i)) & 1));
                        bits++;
                    }
                }
            }
            if (bits < 8) {
                buff = (buff << (8 - bits));
                baos.write(buff);
            }

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
         *
         * @param character character (or byte) to store in a leaf
         * @param value     number of occurrences
         */
        public Node(byte character, int value) {
            this.character = character;
            this.value = value;
        }

        /**
         * Constructor used for building tree from header.
         *
         * @param character character (or byte) to store in a leaf
         */
        public Node(byte character) {
            this(character, 0);
        }

        /**
         * Constructor used for creating non-leaf nodes.
         *
         * @param left  Node containing left leaf or subtree
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

        /**
         * Traverse the tree and return value at specified leaf.
         *
         * @param path Sequence of '1's and '0' determining path to leaf.
         * @return Byte or character located at given leaf.
         */
        public byte getByteForPath(byte path) {
            Node current = this;
            while (!current.isLeafNode()) {
                if ((path & 0x80) == 0) {
                    current = current.getLeft();
                } else {
                    current = current.getRight();
                }
                path <<= 1;
            }
            return current.getCharacter();
        }

        private boolean containsValue(byte ch) {
            if (isLeafNode()) {
                return character == ch;
            }
            return left.containsValue(ch) || right.containsValue(ch);
        }

        /**
         * Finds path to given leaf node.
         *
         * @param ch Character in leaf node we want to find path to.
         * @return Encodes path length in MSB and the actual path in leftmost bits in LSB.
         */
        public short getPathForCharacter(byte ch) {
            short path = 0;

            Node current = this;
            int depth = 7;

            while (current.character != ch) {
                if (current.left.containsValue(ch)) {
                    current = current.left;
                } else {
                    path |= (1 << depth);
                    current = current.right;
                }
                depth--;
            }
            int len = (7 - depth) * 256;
            return (short) (len + path);
        }

        public int countLeaves() {
            if (isLeafNode()) {
                return 1;
            }
            return left.countLeaves() + right.countLeaves();
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

            // first order by node's value
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

            // finally order by ASCII code
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

package pl.lodz.p.it.tips;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.tips.HuffmanTree.Node;

class HuffmanTreeTest {
    @Test
    public void compareToLeafNodesTest() {
        Node node1 = new Node((byte) 'g', 3);
        Node node2 = new Node((byte) 'o', 3);
        Node node3 = new Node((byte) 'a', 2);

        assertTrue(node1.compareTo(node2) < 0);
        assertTrue(node3.compareTo(node1) < 0);
    }

    @Test
    public void compareToNonLeafNodeTest() {
        Node n1 = new Node((byte) 'e', 5);
        Node n2 = new Node((byte) 'a', 1);
        Node n3 = new Node((byte) 'b', 2);
        Node n4 = new Node((byte) 'c', 4);

        Node n5 = new Node(n2, n3); // getValue() => 3
        Node n6 = new Node(n2, n4); // getValue() => 5
        Node n7 = new Node(n3, n4); // getValue() => 6

        assertTrue(n1.compareTo(n5) > 0);
        assertTrue(n6.compareTo(n1) > 0);
        assertTrue(n7.compareTo(n1) > 0);
    }

    @Test
    public void buildTreeFirstTest() {
        Node root = HuffmanTree.buildTree("streets are stone stars are not");

        assertEquals(31, root.getValue());

        assertEquals((byte) 't', root.getLeft().getLeft().getCharacter());

        assertEquals((byte) 'a', root.getLeft().getRight().getLeft().getCharacter());
        assertEquals((byte) 'r', root.getLeft().getRight().getRight().getCharacter());

        assertEquals((byte) 'n', root.getRight().getLeft().getLeft().getLeft().getCharacter());
        assertEquals((byte) 'o', root.getRight().getLeft().getLeft().getRight().getCharacter());

        assertEquals((byte) ' ', root.getRight().getLeft().getRight().getCharacter());

        assertEquals((byte) 'e', root.getRight().getRight().getLeft().getCharacter());
        assertEquals((byte) 's', root.getRight().getRight().getRight().getCharacter());
    }

    @Test
    public void buildTreeSecondTest() {
        Node root = HuffmanTree.buildTree("go go gophers");

        // root node value
        assertEquals(13, root.getValue());

        // left
        assertEquals(6, root.getLeft().getValue());

        assertEquals(3, root.getLeft().getLeft().getValue());
        assertEquals('g', root.getLeft().getLeft().getCharacter());

        assertEquals(3, root.getLeft().getRight().getValue());
        assertEquals('o', root.getLeft().getRight().getCharacter());

        // right
        assertEquals(7, root.getRight().getValue());

        assertEquals(3, root.getRight().getLeft().getValue());

        assertEquals(1, root.getRight().getLeft().getLeft().getValue());
        assertEquals('s', root.getRight().getLeft().getLeft().getCharacter());


        assertEquals(2, root.getRight().getLeft().getRight().getValue());
        assertEquals(' ', root.getRight().getLeft().getRight().getCharacter());

        assertEquals(4, root.getRight().getRight().getValue());

        assertEquals(1, root.getRight().getRight().getLeft().getLeft().getValue());
        assertEquals((byte) 'e', root.getRight().getRight().getLeft().getLeft().getCharacter());

        assertEquals(1, root.getRight().getRight().getLeft().getRight().getValue());
        assertEquals((byte) 'h', root.getRight().getRight().getLeft().getRight().getCharacter());

        assertEquals((byte) 'p', root.getRight().getRight().getRight().getLeft().getCharacter());
        assertEquals((byte) 'r', root.getRight().getRight().getRight().getRight().getCharacter());
    }

    @Test
    public void nodeEqualsTest() {
        Node n1 = new Node((byte) 'a', 1);
        Node n2 = new Node((byte) 'b', 2);

        Node n3 = new Node((byte) 'a', 1);
        Node n4 = new Node((byte) 'b', 2);

        assertEquals(n1, n1);
        assertNotEquals(n1, null);
        assertNotEquals(n1, n2);
        assertEquals(n1, n3);
        assertEquals(n2, n4);
        assertEquals(new Node(n1, n2), new Node(n3, n4));
    }

    @Test
    public void toStringTest() {
        Node root = HuffmanTree.buildTree("go go gophers");
        assertEquals("1g1o01s1 01e1h01p1r0000", root.toString());
    }

    @Test
    public void decodeTreeTest() {
        byte[] header = new byte[]{
                1, 'g', 1, 'o', 0, 1, 's', 1, ' ', 0, 1, 'e', 1, 'h', 0, 1, 'p', 1, 'r', 0, 0, 0, 0
        };

        Node n1 = HuffmanTree.buildTree("go go gophers");
        Node n2 = HuffmanTree.decodeTree(header, 8);

        assertEquals(n1, n2);
    }

    @Test
    public void countLeavesTest() {
        Node root1 = HuffmanTree.buildTree("streets are stone stars are not");
        Node root2 = HuffmanTree.buildTree("go go gophers");

        assertEquals(8, root1.countLeaves());
        assertEquals(8, root2.countLeaves());
    }

    @Test
    public void getByteForPathTest() {
        Node root = HuffmanTree.buildTree("go go gophers");

        assertEquals('g', root.getByteForPath((byte) 0));
        assertEquals('o', root.getByteForPath((byte) 0b01000000));
        assertEquals('s', root.getByteForPath((byte) 0b10000000));
        assertEquals(' ', root.getByteForPath((byte) 0b10100000));
        assertEquals('e', root.getByteForPath((byte) 0b11000000));
        assertEquals('h', root.getByteForPath((byte) 0b11010000));
        assertEquals('p', root.getByteForPath((byte) 0b11100000));
        assertEquals('r', root.getByteForPath((byte) 0b11110000));
    }

    @Test
    public void getPathForCharacterTest() {
        Node root = HuffmanTree.buildTree("go go gophers");

        assertEquals(0x0200, root.getPathForCharacter((byte) 'g'));
        assertEquals(0x04d0, root.getPathForCharacter((byte) 'h'));
        assertEquals(0x03a0, root.getPathForCharacter((byte) ' '));
    }

    @Test
    public void encodeTest() {
        byte[] expected = new byte[]{
                0, 0, 0, 13, // text length
                0, 0, 0, 8,  // number of characters
                1, 'g', 1, 'o', 0, 1, 's', 1, ' ', 0, 1, 'e', 1, 'h', 0, 1, 'p', 1, 'r', 0, 0, 0, 0,
                0b00_01_101_0, 0b0_01_101_00, 0b01_1110_11, 0b01_1100_11, (byte) 0b11_100_000
        };

        byte[] actual = HuffmanTree.encode("go go gophers");

        assertArrayEquals(expected, actual);
    }

    @Test
    public void decodeFirstTest() {
        byte[] encoded = new byte[]{
                0, 0, 0, 13, // text length
                0, 0, 0, 8,  // number of characters
                1, 'g', 1, 'o', 0, 1, 's', 1, ' ', 0, 1, 'e', 1, 'h', 0, 1, 'p', 1, 'r', 0, 0, 0, 0,
                0b00_01_101_0, 0b0_01_101_00, 0b01_1110_11, 0b01_1100_11, (byte) 0b11_100_000
        };

        String decoded = HuffmanTree.decode(encoded);

        assertEquals("go go gophers", decoded);
    }

    @Test
    public void decodeSecondTest() {
        System.out.println(HuffmanTree.buildTree("streets are stone stars are not"));
        byte[] encoded = new byte[]{
                0, 0, 0, 31,
                0, 0, 0, 8,
                1, 't', 1, 'a', 1, 'r', 0, 0, 1, 'n', 1, 'o', 0, 1, ' ', 0, 1, 'e', 1, 's', 0, 0, 0,
                (byte) 0b111_00_011, (byte) 0b110_110_00, (byte) 0b111_101_01, 0b0_011_110_1,
                0b01_111_00_1, 0b001_1000_1, (byte) 0b10_101_111, 0b00_010_011,
                (byte) 0b111_101_01, 0b0_011_110_1, 0b01_1000_10, 0b01_00_0000
        };

        String decoded = HuffmanTree.decode(encoded);

        assertEquals("streets are stone stars are not", decoded);
    }
}
package pl.lodz.p.it.tips;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
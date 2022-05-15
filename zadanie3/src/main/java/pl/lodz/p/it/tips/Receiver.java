package pl.lodz.p.it.tips;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(40321);
             Socket client = server.accept();
             FileWriter out = new FileWriter("out.txt");
             InputStream in = client.getInputStream()) {

            byte[] encoded = in.readAllBytes();
            String decoded = HuffmanTree.decode(encoded);

            if (decoded == null) {
                throw new RuntimeException("something went wrong");
            } else {
                System.out.println(decoded);
                out.write(decoded);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

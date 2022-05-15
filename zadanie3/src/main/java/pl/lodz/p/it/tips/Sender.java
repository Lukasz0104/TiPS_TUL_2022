package pl.lodz.p.it.tips;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Sender {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 40321);
             OutputStream out = socket.getOutputStream()) {

            String fileContent = Files.readString(Path.of("in.txt"));
            System.out.println(fileContent);

            byte[] encoded = HuffmanTree.encode(fileContent);
            if (encoded == null) {
                throw new RuntimeException("something is wrong");
            }
            out.write(encoded);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

package pl.lodz.p.it.tips;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    public static void main(String[] args) {
        System.out.println("hello world from receiver");
        try (ServerSocket server = new ServerSocket(4321);
             Socket client = server.accept();
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

            String line = in.readLine();
            System.out.println(line);
            out.println("i received message from sender");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

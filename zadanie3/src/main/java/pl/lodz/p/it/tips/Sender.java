package pl.lodz.p.it.tips;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4321);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("sending message to server");
            String response = in.readLine();
            System.out.println(response);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

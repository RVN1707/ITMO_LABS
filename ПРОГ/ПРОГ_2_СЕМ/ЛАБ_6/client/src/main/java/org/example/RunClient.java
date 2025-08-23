package org.example;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RunClient {
    public static void main(String[] args) {
        try {
            ClientLogic clientLogic = new ClientLogic(InetAddress.getByName("localhost"), 12727);
            clientLogic.run();
        } catch (UnknownHostException e) {
            System.out.println("Хоста с таким именем не существует");
        }
    }
}
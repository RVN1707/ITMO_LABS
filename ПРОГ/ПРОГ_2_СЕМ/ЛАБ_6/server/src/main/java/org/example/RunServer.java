package org.example;

public class RunServer { ;

    public static void main(String[] args) {
        ServerLogic serverLogic = new ServerLogic(12727, "LAB");
        serverLogic.run();
    }
}
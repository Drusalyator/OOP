package main;

import webServer.Server;

public class Main {
    public static void main(String[] args) {
        new Server("127.0.0.1", 40010).Run();
        System.exit(0);
    }
}

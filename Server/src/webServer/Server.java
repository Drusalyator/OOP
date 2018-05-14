package webServer;

import threadDispatcher.ClientWorker;
import threadDispatcher.ThreadDispatcher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket = null;
    private ThreadDispatcher threadDispatcher = ThreadDispatcher.getInstance();

    public Server(String host, int port) {
        try {
            serverSocket = new ServerSocket(port, 0, InetAddress.getByName(host));
            System.out.println("> Server started");
        } catch (IOException | IllegalArgumentException exception) {
            System.out.println("> Error! Server can not be created with these parameters.");
            System.exit(-1);
        }
    }

    public void Run() {
        try {
            System.out.println("> Waiting for a client...");
            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(String.format("> Client (%s, %s) connected",
                            clientSocket.getInetAddress().toString().substring(1),
                            clientSocket.getPort()));
                    threadDispatcher.Add(new ClientWorker(clientSocket));
                } catch (IOException exception) {
                    System.out.println("> Error : " + exception);
                }
            }
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                    threadDispatcher.Stop();
                    System.out.println("> Server stop");
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}

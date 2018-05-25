package threadDispatcher;

import commandFactory.CommandFactory;
import commands.ICommand;
import dataProvider.FolderProvider;
import javafx.util.Pair;
import packet.IPacket;
import utils.Helper;

import java.io.IOException;
import java.net.Socket;

public class ClientWorker extends ThreadedTask {

    private Socket clientSocket;

    public ClientWorker(Socket clientSocket) {
        name = String.format("Client Worker-%s", clientSocket.getPort());
        this.clientSocket = clientSocket;
    }

    @Override
    public void doWork() {
        try {
            Pair<IPacket, byte[]> request = Helper.getRequest(clientSocket);
            ICommand command = CommandFactory.createCommand(request.getKey());
            Pair<IPacket, byte[]> response = command.execute(new FolderProvider(), request.getValue());
            Helper.sendResponse(clientSocket, response);
            System.out.println(String.format("> Client (%s, %s) disconnected",
                    clientSocket.getInetAddress().toString().substring(1),
                    clientSocket.getPort()));
            clientSocket.close();
        } catch (IOException exception) {
            System.out.println("> Error : " + exception);
        }
    }
}

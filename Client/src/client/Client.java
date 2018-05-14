package client;

import commandFactory.CommandFactory;
import commands.ICommand;
import packet.IPacket;
import parser.CommandParser;
import serialization.Serialization;
import utils.Helper;

import java.net.*;
import java.io.*;
import java.util.Objects;

public class Client {
    private final int serverPort;
    private final String host;

    public Client(String host, int serverPort) {
        this.serverPort = serverPort;
        this.host = host;
    }

    public void run() {
        boolean work = true;

        try {
            System.out.println(getHelp());
            BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            String userName;

            while (true) {
                System.out.print(" > Input your name : ");
                userName = keyboardReader.readLine();
                if (userName != null && !Objects.equals(userName, "")) break;
            }

            String command;

            while (work) {
                try {
                    System.out.print(" > Type your commands: ");
                    IPacket packet;
                    command = keyboardReader.readLine();
                    try {
                        packet = CommandParser.parseCommand(command, userName);
                    } catch (CommandParser.ParseError exception) {
                        System.out.println(" > " + exception.getMessage());
                        continue;
                    }
                    Socket socket = new Socket(host, serverPort);
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                    Helper.sendResponse(socket, packet);
                    /*
                    byte[] data = Serialization.serializeClass(packet);
                    if (data != null) {
                        outputStream.writeInt(data.length);
                        outputStream.write(data);
                        outputStream.flush();
                    } else {
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                        continue;
                    }
                    */

                    IPacket response = Helper.getRequest(socket);

                    /*
                    IPacket response;
                    while (true) {
                        try {
                            int length = inputStream.readInt();
                            if (length > 0) {
                                byte[] message = new byte[length];
                                inputStream.readFully(message);
                                response = (IPacket) Serialization.deserializeClass(message);
                                break;
                            }
                        } catch (IOException exception) {

                        }
                    }
                    */

                    if (response != null) {
                        ICommand responseCommand = CommandFactory.createCommand(response);
                        responseCommand.execute();
                    } else System.out.println(" > Response was not get");

                    inputStream.close();
                    outputStream.close();
                    socket.close();
                } catch (ConnectException exception) {
                    work = false;
                    System.out.println(" > Server not work");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private String getHelp() {
        return " > Welcome to client\n" +
                "   Commands: add\n" +
                "             clone localPath serverPath [flags]\n" +
                "             update\n" +
                "             commit\n" +
                "             revert [version [flag]]\n" +
                "             log\n";
    }
}

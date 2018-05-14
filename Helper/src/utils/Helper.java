package utils;

import packet.FilePacket;
import packet.IPacket;
import serialization.Serialization;

import java.io.*;
import java.net.Socket;

public class Helper {

    public static void sendResponse(Socket socket, IPacket response) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        byte[] data = Serialization.serializeClass(response);
        if (data != null) {
            outputStream.writeInt(data.length);
            outputStream.write(data);
            outputStream.flush();
            //outputStream.close();
        } else throw new NullPointerException();
    }

    public static IPacket getRequest(Socket socket) throws IOException {
        while (true) {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            int length = inputStream.readInt();
            if (length > 0) {
                byte[] message = new byte[length];
                inputStream.readFully(message);
                //inputStream.close();
                return (IPacket) Serialization.deserializeClass(message);
            }
        }
    }

    public static FilePacket[] getFilePacket(File[] files) throws IOException {
        FilePacket[] filePackets = new FilePacket[files.length];
        int i = 0;
        for (File file : files) {
            byte[] data = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(data);
            FilePacket filePacket = new FilePacket(file.getName(), data);
            filePackets[i] = filePacket;
            i++;
        }
        return filePackets;
    }
}
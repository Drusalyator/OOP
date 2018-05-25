package utils;

import fileWorker.Md5Executor;
import javafx.util.Pair;
import packet.IPacket;
import serialization.Serialization;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Helper {

    public static void sendResponse(Socket socket, Pair<IPacket, byte[]> response) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        byte[] data = Serialization.serializeClass(response.getKey());
        if (data != null) {
            outputStream.writeInt(data.length);
            outputStream.write(data);
            if (response.getValue() != null)
                outputStream.write(response.getValue());
        } else throw new NullPointerException();
    }

    public static Pair<IPacket, byte[]> getRequest(Socket socket) throws IOException {
        while (true) {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            int length = inputStream.readInt();
            if (length > 0) {
                byte[] message = new byte[length];
                inputStream.readFully(message);
                IPacket packet = (IPacket) Serialization.deserializeClass(message);
                if (packet.getDataLength() != 0) {
                    byte[] data = new byte[packet.getDataLength()];
                    inputStream.readFully(data);
                    return new Pair<>(packet, data);
                } else return new Pair<>(packet, null);
            }
        }
    }

    public static byte[] makeArchive(File[] files) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        for (File file: files) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);

            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fileInputStream.read(buffer);

            zipOutputStream.write(buffer);
        }

        zipOutputStream.closeEntry();

        return byteArrayOutputStream.toByteArray();
    }

    public static Map<String, byte[]> readArchive(byte[] data) throws IOException {
        Map<String, byte[]> files = new HashMap<>();

        if (data != null) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);

            ZipEntry zipEntry;
            String fileName;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                fileName = zipEntry.getName();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                    byteArrayOutputStream.write(c);
                }

                byte[] file = byteArrayOutputStream.toByteArray();

                files.put(fileName, file);
            }
        }

        return files;
    }
}

package commandFactory;

import commands.*;
import commands.Error;
import packet.IPacket;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CommandFactory {

    public static ICommand createCommand(IPacket packet) {
        switch (packet.getClass().getName()) {
            case "packet.AddPacket":
                return new Add(packet);
            case "packet.ClonePacket":
                return new Clone(packet);
            case "packet.UpdatePacket":
                return new Update(packet);
            case "packet.CommitPacket":
                return new Commit(packet);
            case "packet.LogPacket":
                return new Log(packet);
            case "packet.RevertPacket":
                return new Revert(packet);
            default:
                return new Error(packet);
        }
    }

    public static void storeConfigFile(Properties config, String path) {
        try {
            config.storeToXML(new FileOutputStream(path), null);
        } catch (IOException exception) {
            System.out.println(" > Cannot save current config");
        }
    }

    public static Properties loadConfigFile(String path) throws IOException{
        Properties config = new Properties();
        config.loadFromXML(new FileInputStream(path));
        return config;
    }
}
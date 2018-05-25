package commands;

import commandFactory.CommandFactory;
import packet.IPacket;
import packet.UpdatePacket;
import utils.Helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Update implements ICommand{

    private UpdatePacket updatePacket;

    public Update(IPacket packet) {
        if (packet instanceof UpdatePacket)
            this.updatePacket = (UpdatePacket) packet;
    }

    @Override
    public void execute(byte[] data) {
        try {
            Map<String, byte[]> fileData = Helper.readArchive(data);
            if (fileData.size() == 0)
                System.out.println("No getting file");
            else {
                Properties userConfig;
                Properties repConfig;
                try {
                    userConfig = CommandFactory.loadConfigFile("user.conf");
                    repConfig = CommandFactory.loadConfigFile(userConfig.getProperty("LocalRep") + "//rep.conf");
                } catch (IOException exception) {
                    System.out.println(" > Cannot load config files");
                    return;
                }

                for (Map.Entry<String, byte[]> file : fileData.entrySet()) {
                    try {
                        FileOutputStream fileOutputStream =
                                new FileOutputStream(userConfig.getProperty("LocalRep") + "//" + file.getKey());
                        fileOutputStream.write(file.getValue());
                    } catch (IOException exception) {
                        System.out.println(" > File : '" + file.getKey() + "' was not write");
                    }
                }
                System.out.println(" > Files was update");
            }
        } catch (IOException exception) {
            System.out.println("Cannot execute command");
        }
    }
}

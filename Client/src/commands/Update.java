package commands;

import commandFactory.CommandFactory;
import packet.FilePacket;
import packet.IPacket;
import packet.UpdatePacket;
import parser.CommandParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Update implements ICommand{

    private UpdatePacket updatePacket;

    public Update(IPacket packet) {
        if (packet instanceof UpdatePacket)
            this.updatePacket = (UpdatePacket) packet;
    }

    @Override
    public void execute() {
        FilePacket[] files = updatePacket.getFiles();
        if (files == null)
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

            for (FilePacket file : files) {
                if (repConfig.getProperty(file.getFileName()) == null ||
                        file.getHash() == null ||
                        !Objects.equals(repConfig.getProperty(file.getFileName()), file.getHash())) {
                    try {
                        FileOutputStream fileOutputStream =
                                new FileOutputStream(userConfig.getProperty("LocalRep") + "//" + file.getFileName());
                        fileOutputStream.write(file.getFileData());
                    } catch (IOException exception) {
                        System.out.println(" > File : '" + file.getFileName() + "' was not write");
                    }
                }
            }
            System.out.println(" > Files was update");
        }
    }
}

package commands;

import commandFactory.CommandFactory;
import packet.FilePacket;
import packet.IPacket;
import packet.RevertPacket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

public class Revert implements ICommand{

    private RevertPacket revertPacket;

    public Revert(IPacket packet) {
        if (packet instanceof RevertPacket)
            this.revertPacket = (RevertPacket) packet;
    }

    @Override
    public void execute() {
        ArrayList<FilePacket> needToRevertFiles = new ArrayList<>();
        Properties userConfig;
        try {
            userConfig = CommandFactory.loadConfigFile("user.conf");
        } catch (IOException exception) {
            System.out.println(" > Cannot load config files");
            return;
        }
        File localRep = new File(userConfig.getProperty("LocalRep"));
        if (revertPacket.getFlag() != null) {
            switch (revertPacket.getFlag()) {
                case "-hard":
                    needToRevertFiles.addAll(Arrays.asList(revertPacket.getFiles()));
                    break;
                default: {
                    System.out.println(" > Unsupported flag");
                    return;
                }
            }
        } else {
            String[] fileInRepo = localRep.list((dir, name) -> !Objects.equals(name, "rep.conf"));
            if (fileInRepo == null) {
                System.out.println(" > No files in repository");
                return;
            }
            for (FilePacket filePacket: revertPacket.getFiles()) {
                boolean found = false;
                for (String fileName: fileInRepo) {
                    if (Objects.equals(filePacket.getFileName(), fileName)) {
                        found = true;
                        break;
                    }
                }
                if (found) needToRevertFiles.add(filePacket);
            }
        }
        for (FilePacket filePacket: needToRevertFiles) {
            try {
                FileOutputStream fileOutputStream =
                        new FileOutputStream(userConfig.getProperty("LocalRep") + "//" + filePacket.getFileName());
                fileOutputStream.write(filePacket.getFileData());
            } catch (IOException exception) {
                System.out.println(" > File : '" + filePacket.getFileName() + "' was not write");
            }
        }
        System.out.println(" > Revert to version: " + revertPacket.getVersion() + " with flag: " + revertPacket.getFlag());

    }

}

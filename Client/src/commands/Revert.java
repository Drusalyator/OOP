package commands;

import commandFactory.CommandFactory;
import fileWorker.Md5Executor;
import packet.IPacket;
import packet.RevertPacket;
import utils.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Revert implements ICommand {

    private RevertPacket revertPacket;

    public Revert(IPacket packet) {
        if (packet instanceof RevertPacket)
            this.revertPacket = (RevertPacket) packet;
    }

    @Override
    public void execute(byte[] data) {
        try {
            Map<String, byte[]> filesData = Helper.readArchive(data);
            ArrayList<Map.Entry<String, byte[]>> needToRevertFiles = new ArrayList<>();
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
                        needToRevertFiles.addAll(filesData.entrySet());
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
                for (Map.Entry<String, byte[]> file : filesData.entrySet()) {
                    boolean found = false;
                    for (String fileName : fileInRepo) {
                        if (Objects.equals(file.getKey(), fileName)) {
                            found = true;
                            break;
                        }
                    }
                    if (found) needToRevertFiles.add(file);
                }
            }

            Md5Executor md5Executor = new Md5Executor();
            Properties repConfig = CommandFactory.loadConfigFile(localRep + "//rep.conf");

            for (Map.Entry<String, byte[]> file : needToRevertFiles) {
                try {
                    String path = userConfig.getProperty("LocalRep") + "//" + file.getKey();
                    FileOutputStream fileOutputStream = new FileOutputStream(path);
                    fileOutputStream.write(file.getValue());
                    String hash = md5Executor.process(new File(path));
                    repConfig.setProperty(file.getKey(), hash);
                } catch (IOException exception) {
                    System.out.println(" > File : '" + file.getKey() + "' was not write");
                }
            }
            CommandFactory.storeConfigFile(repConfig, localRep + "//rep.conf");
            System.out.println(" > Revert to version: " + revertPacket.getVersion() + " with flag: " + revertPacket.getFlag());

        } catch (IOException exception) {
            System.out.println("Cannot execute command");
        }
    }

}

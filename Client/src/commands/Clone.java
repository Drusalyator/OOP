package commands;

import packet.ClonePacket;
import packet.FilePacket;
import packet.IPacket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Clone implements ICommand{

    private ClonePacket clonePacket;

    public Clone(IPacket packet) {
        if (packet instanceof ClonePacket)
            this.clonePacket = (ClonePacket) packet;
    }

    @Override
    public void execute() {
        String pathToRepositories = clonePacket.getPath();
        File repositories = new File(pathToRepositories);
        if (!repositories.isDirectory()) repositories.mkdir();
        Properties userConfig = new Properties();
        userConfig.setProperty("ServerRep", clonePacket.getRepoName());
        if (clonePacket.getFlag() != null) {
            switch (clonePacket.getFlag()) {
                case ".":
                    String pathToRepo = pathToRepositories + "//";
                    File[] filesInRepo = new File(pathToRepositories).listFiles();
                    if (filesInRepo != null)
                        for (File file : filesInRepo) file.delete();
                    for (FilePacket filePacket: clonePacket.getFiles())
                        try {
                            writeFile(pathToRepo, filePacket);
                        } catch (IOException exception) {
                            System.out.println(" > File " + filePacket.getFileName() + " was not write");
                        }
                    userConfig.setProperty("LocalRep", pathToRepositories);
                    break;
                default: System.out.println(" > Unsupported flag");
            }
        } else {
            String pathToNewRepo = pathToRepositories + "//" + clonePacket.getRepoName();
            if (!new File(pathToNewRepo).mkdir()) {
                File[] filesInRepo = new File(pathToNewRepo).listFiles();
                if (filesInRepo != null)
                    for (File file : filesInRepo) file.delete();
            }
            for (FilePacket filePacket: clonePacket.getFiles()) {
                try {
                    writeFile(pathToNewRepo + "//", filePacket);
                } catch (IOException exception) {
                    System.out.println(" > File " + filePacket.getFileName() + " was not write");
                }
            }
            userConfig.setProperty("LocalRep", pathToRepositories + "//" + clonePacket.getRepoName());
        }
        try {
            userConfig.storeToXML(new FileOutputStream("user.conf"), null);
        } catch (IOException exception) {
            System.out.println(" > Cannot save current repository");
        }
    }

    private void writeFile(String path, FilePacket file) throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream(path + file.getFileName());
        fileOutputStream.write(file.getFileData());
    }

}

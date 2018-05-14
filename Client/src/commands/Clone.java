package commands;

import commandFactory.CommandFactory;
import fileWorker.Md5Executor;
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
        String pathToFile;
        if (clonePacket.getFlag() != null)
            switch (clonePacket.getFlag()) {
                case ".":
                    pathToFile = pathToRepositories;
                    File[] filesInRepo = new File(pathToRepositories).listFiles();
                    if (filesInRepo != null)
                        for (File file : filesInRepo) file.delete();
                    userConfig.setProperty("LocalRep", pathToRepositories);
                    break;
                default:
                    System.out.println(" > Unsupported flag");
                    return;
            }
        else {
            String pathToNewRepo = pathToRepositories + "//" + clonePacket.getRepoName();
            if (!new File(pathToNewRepo).mkdir()) {
                File[] filesInRepo = new File(pathToNewRepo).listFiles();
                if (filesInRepo != null)
                    for (File file : filesInRepo) file.delete();
            }
            pathToFile = pathToNewRepo;
            userConfig.setProperty("LocalRep", pathToRepositories + "//" + clonePacket.getRepoName());
        }
        Properties repConfig = new Properties();
        for (FilePacket filePacket: clonePacket.getFiles())
            try {
                writeFile(pathToFile, filePacket);
                Md5Executor md5Executor = new Md5Executor();
                String currentPath = pathToFile + "//" + filePacket.getFileName();
                String hash = md5Executor.process(new File(currentPath));
                repConfig.setProperty(filePacket.getFileName(), hash);
            } catch (IOException exception) {
                System.out.println(" > File " + filePacket.getFileName() + " was not write");
            }
        CommandFactory.storeConfigFile(repConfig, pathToFile + "//rep.conf");
        CommandFactory.storeConfigFile(userConfig, "user.conf");
    }

    private void writeFile(String path, FilePacket file) throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream(path + "//" + file.getFileName());
        fileOutputStream.write(file.getFileData());
    }
}

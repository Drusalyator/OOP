package commands;

import commandFactory.CommandFactory;
import fileWorker.Md5Executor;
import packet.ClonePacket;
import packet.IPacket;
import utils.Helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class Clone implements ICommand{

    private ClonePacket clonePacket;

    public Clone(IPacket packet) {
        if (packet instanceof ClonePacket)
            this.clonePacket = (ClonePacket) packet;
    }

    @Override
    public void execute(byte[] data) {
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
        try {
            Map<String, byte[]> fileData = Helper.readArchive(data);
            for (Map.Entry<String, byte[]> file : fileData.entrySet())
                try {
                    writeFile(pathToFile, file);
                    Md5Executor md5Executor = new Md5Executor();
                    String currentPath = pathToFile + "//" + file.getKey();
                    String hash = md5Executor.process(new File(currentPath));
                    repConfig.setProperty(file.getKey(), hash);
                } catch (IOException exception) {
                    System.out.println(" > File " + file.getKey() + " was not write");
                }
            CommandFactory.storeConfigFile(repConfig, pathToFile + "//rep.conf");
            CommandFactory.storeConfigFile(userConfig, "user.conf");
            System.out.println(" > Clone was success");
        } catch (IOException exception) {
            System.out.println("Cannot execute command");
        }
    }

    private void writeFile(String path, Map.Entry<String, byte[]> file) throws IOException{
        FileOutputStream fileOutputStream = new FileOutputStream(path + "//" + file.getKey());
        fileOutputStream.write(file.getValue());
    }
}

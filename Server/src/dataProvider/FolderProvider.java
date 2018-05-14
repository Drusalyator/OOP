package dataProvider;

import packet.FilePacket;
import serialization.Serialization;
import versionSystem.IVersionSystem;

import java.io.*;
import java.util.*;

public class FolderProvider implements IDataProvider {

    public FolderProvider() {
        String pathToRepositories = "repositories";
        File repositories = new File(pathToRepositories);
        if (!repositories.isDirectory()) repositories.mkdir();
    }

    public void createRepository(String repositoryName, IVersionSystem versionSystem) throws ProviderException {
        String path = "repositories//" + repositoryName;
        File repository = new File(path);
        if (repository.mkdir()) {
            try {
                RepConfig repConfig = new RepConfig(repositoryName, versionSystem);
                writeConfig(repConfig, path + String.format("//%s.conf", repositoryName));
            } catch (ProviderException exception) {
                repository.delete();
                throw new ProviderException(exception.getMessage());
            }
        } else throw new ProviderException("Could not created repository");
    }

    public void addNewVersion(String repName, FilePacket[] files, String[] actualFiles) throws ProviderException {
        String pathToRepConfig = "repositories//" + repName + String.format("//%s.conf", repName);
        RepConfig repConfig = (RepConfig) readConfig(pathToRepConfig);
        double nextVersion = repConfig.getVersionSystem().getNextVersion(repConfig.actualVersion);

        String pathToVersion = "repositories//" + repName + "//" + nextVersion;
        File newVersion = new File(pathToVersion);

        if (newVersion.mkdir()) {
            try {
                for (FilePacket file : files) {
                    FileOutputStream fileOutputStream =
                            new FileOutputStream(pathToVersion + "//" + file.getFileName());
                    fileOutputStream.write(file.getFileData());
                    fileOutputStream.close();
                }

                repConfig.actualVersion = nextVersion;
                VerConfig verConfig = new VerConfig(actualFiles);

                pathToVersion = pathToVersion + "//version.conf";
                writeConfig(verConfig, pathToVersion);
                writeConfig(repConfig, pathToRepConfig);
            } catch (IOException | NullPointerException exception) {
                new File(pathToVersion).delete();
                throw new ProviderException("Could not add some file: " + exception.getMessage());
            }
        } else throw new ProviderException("Could not created new version");
    }

    public File[] getActualVersion(String repName) throws ProviderException {
        String pathToRep = "repositories//" + repName;
        String pathToRepConfig = pathToRep + String.format("//%s.conf", repName);

        RepConfig repConfig = (RepConfig) readConfig(pathToRepConfig);
        double actualVersion = repConfig.actualVersion;

        if (actualVersion != 0.0) {
            String pathToVerConfig = pathToRep + "//" + actualVersion + "//version.conf";
            VerConfig verConfig = (VerConfig) readConfig(pathToVerConfig);
            String[] actualFiles = verConfig.actualFiles;

            ArrayList<File> result = new ArrayList<>();
            File[] listOfVersion = getListOfVersion(repName);

            for (int i = listOfVersion.length - 1; i >= 0; i--) {
                addNeededFile(result, repName, listOfVersion[i].getName(), actualFiles);
                if (actualFiles.length == result.size()) break;
            }

            return result.toArray(new File[result.size()]);
        } else return new File[0];
    }

    public File[] getNeededVersion(String repName, String versionName) throws ProviderException {
        String pathToRep = "repositories//" + repName;

        String pathToVerConfig = pathToRep + "//" + versionName + "//version.conf";
        VerConfig verConfig = (VerConfig) readConfig(pathToVerConfig);
        String[] actualFiles = verConfig.actualFiles;

        ArrayList<File> result = new ArrayList<>();
        File[] listOfVersion = getListOfVersion(repName);

        for (int i = listOfVersion.length - 1; i >= 0; i--) {
            if (Double.parseDouble(listOfVersion[i].getName()) <= Double.parseDouble(versionName))
                addNeededFile(result, repName, listOfVersion[i].getName(), actualFiles);
            if (actualFiles.length == result.size()) break;
        }

        return result.toArray(new File[result.size()]);
    }

    private File[] getListOfVersion(String repName) throws ProviderException {
        File repository = new File("repositories//" + repName);
        ArrayList<File> result = new ArrayList<>();
        if (repository.isDirectory()) {
            File[] listOfVersion = repository.listFiles();
            if (listOfVersion != null) {
                for (File file : listOfVersion) {
                    if (file.isDirectory()) result.add(file);
                }
                return result.toArray(new File[result.size()]);
            } else return new File[0];
        } else throw new ProviderException("It's not repository");
    }

    private void addNeededFile(ArrayList<File> result, String repName, String versionName, String[] actualFiles) throws ProviderException {
        File[] files;
        File versionDirectory = new File("repositories//" + repName + "//" + versionName);
        if (versionDirectory.isDirectory())
            files = versionDirectory.listFiles();
        else throw new ProviderException(String.format("There is no such repository: %s", repName));
        if (files != null)
            for (File file : files) {
                boolean found = false;
                for (String fileName : actualFiles)
                    if (Objects.equals(fileName, file.getName())) {
                        found = true;
                        break;
                    }
                if (found) {
                    boolean exist = false;
                    for (File resultFiles : result)
                        if (Objects.equals(resultFiles.getName(), file.getName())) {
                            exist = true;
                            break;
                        }
                    if (!exist) result.add(file);
                }
            }
        else throw new ProviderException("Version is empty");
    }

    private void writeConfig(IConfig config, String path) throws ProviderException {
        Properties confFile = Serialization.classToProperties(config);
        if (confFile != null)
            try {
                confFile.storeToXML(new FileOutputStream(path), null);
            } catch (IOException exception) {
                throw new ProviderException("RepConfig file was not wrote: " + exception.getMessage());
            }
        else throw new ProviderException("Could not created config file");
    }

    private IConfig readConfig(String path) throws ProviderException {
        Properties confFile = new Properties();
        try {
            confFile.loadFromXML(new FileInputStream(path));
            return (IConfig) Serialization.classFromProperties(confFile);
        } catch (IOException exception) {
            throw new ProviderException("Could not load config file: " + exception.getMessage());
        }
    }
}
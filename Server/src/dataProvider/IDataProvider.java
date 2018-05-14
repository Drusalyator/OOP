package dataProvider;

import packet.FilePacket;
import versionSystem.IVersionSystem;

import java.io.File;


public interface IDataProvider {

    void createRepository(String repositoryName, IVersionSystem versionSystem) throws ProviderException;

    void addNewVersion(String repName, FilePacket[] files, String[] actualFiles) throws ProviderException;

    File[] getActualVersion(String repName) throws ProviderException;

    File[] getNeededVersion(String repName, String versionName) throws ProviderException;

    class ProviderException extends Exception {
        public ProviderException(String message) {
            super(message);
        }
    }
}

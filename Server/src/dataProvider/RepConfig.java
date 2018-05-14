package dataProvider;

import versionSystem.IVersionSystem;

public class RepConfig implements IConfig {

    private String repositoryName;
    double actualVersion = 0.0;
    private IVersionSystem versionSystem;

    public RepConfig() {
    }

    RepConfig(String repositoryName, IVersionSystem versionSystem) {
        this.repositoryName = repositoryName;
        this.versionSystem = versionSystem;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    IVersionSystem getVersionSystem() {
        return versionSystem;
    }
}

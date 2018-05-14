package dataProvider;

import versionSystem.IVersionSystem;

public class RepConfig implements IConfig {

    private String repositoryName;
    public double actualVersion = 0.0;
    private IVersionSystem versionSystem;

    public RepConfig() {
    }

    public RepConfig(String repositoryName, IVersionSystem versionSystem) {
        this.repositoryName = repositoryName;
        this.versionSystem = versionSystem;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public IVersionSystem getVersionSystem() {
        return versionSystem;
    }
}

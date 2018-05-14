package dataProvider;

public class VerConfig implements IConfig {

    public String[] actualFiles;

    public VerConfig() {
    }

    public VerConfig(String[] actualFiles) {
        this.actualFiles = actualFiles;
    }
}

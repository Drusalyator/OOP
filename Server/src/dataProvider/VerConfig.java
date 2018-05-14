package dataProvider;

public class VerConfig implements IConfig {

    String[] actualFiles;

    public VerConfig() {
    }

    VerConfig(String[] actualFiles) {
        this.actualFiles = actualFiles;
    }
}

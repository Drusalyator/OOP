package versionSystem;


public class StandardVersion implements IVersionSystem {

    public StandardVersion() {
    }

    @Override
    public double getNextVersion(double currentVersion) {
        if (currentVersion == 0.0) return 1;
        else return (currentVersion * 10 + 1) / 10;
    }
}

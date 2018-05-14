package commandFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserConfig {

    private Properties userRepository;

    private static volatile UserConfig instance;

    public static UserConfig getInstance() {
        if (instance == null)
            synchronized (UserConfig.class) {
                if (instance == null)
                    instance = new UserConfig();
            }
        return instance;
    }

    private UserConfig() {
        userRepository = new Properties();
        try {
            userRepository.loadFromXML(new FileInputStream("users.conf"));
        } catch (IOException exception) {
            System.out.println("Cannot load users configuration");
        }
    }

    public void addNewUsers(String userName, String repName) {
        userRepository.setProperty(userName, repName);
        try {
            userRepository.storeToXML(new FileOutputStream("users.conf"), null);
        } catch (IOException exception) {
            System.out.println("Cannot save users configuration");
        }
    }

    public String getUserRepository(String userName) {
        return userRepository.getProperty(userName);
    }
}

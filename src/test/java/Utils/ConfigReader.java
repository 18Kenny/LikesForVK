package Utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class ConfigReader {
    static private final String pathForConfig = "./src/test/resources/config.properties";
    public static HashMap<Object, String> configPropertyMap;
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    public static HashMap<Object, String> getConfigPropertyMap() {
        if (configPropertyMap == null) {
            configPropertyMap = getValueFromConfig();
        }
        return configPropertyMap;
    }

    public static HashMap<Object, String> getValueFromConfig() {
        configPropertyMap = new HashMap<>();
        FileInputStream propertyStream;
        Properties property = new Properties();
        try {
            propertyStream = new FileInputStream(pathForConfig);
            property.load(propertyStream);
            property.forEach((propertyName, value) -> {
                configPropertyMap.put(propertyName, value.toString());
                logger.info("got from config file property " + propertyName + " with value = " + value);
            });
            return configPropertyMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("File " + pathForConfig + "didnt found");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("u tried put wrong data at property loader");
        }
        return null;
    }
}

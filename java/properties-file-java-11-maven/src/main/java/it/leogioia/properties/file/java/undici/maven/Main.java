package it.leogioia.properties.file.java.undici.maven;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

@Slf4j
public class Main {

    public static void main(String[] args) throws ConfigurationException {
        String configFilePath = System.getProperty("config.dir") + System.getProperty("file.separator") + "application.properties";

        log.info("Loading properties from {}", configFilePath);

        Configurations configs = new Configurations();
        Configuration config = configs.properties(new File(configFilePath));

        log.info("Value for key1: {}", config.getString("key1"));
        log.info("Value for key2: {}", config.getString("key2"));
    }
}

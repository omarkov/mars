package server;

import java.io.*;
import java.util.*;


public class Config {
    private static Properties properties = new Properties();

    private static void loadResource(String resources, Properties props) throws Exception {
        InputStream in = new FileInputStream(resources);

        if (in == null) {
            throw new Exception("Unable to locate resources: " + resources);
        } else {
            try {
                props.load(in);
            } catch (java.io.IOException e) {
                System.err.println("Caught IOException loading resources: "
                                   + resources);
                System.err.println("Using default resources.");
            }
        }
    }

    public static void load(String file) throws Exception {
	loadResource(file, properties);
    }

    public static String get(String key) {
	return properties.getProperty(key);
    }
}

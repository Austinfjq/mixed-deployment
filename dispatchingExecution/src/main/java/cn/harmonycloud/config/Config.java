package cn.harmonycloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hc on 19-1-15.
 */
public class Config {
    private final static Logger LOGGER = LoggerFactory.getLogger(Config.class);
    //database server
    public static String DB_SERVER="10.10.101.115";
    public static String filename = "config.yaml";

    public void readConfigFile(){
        String path =this.getClass().getResource("/").getPath();
        Yaml yaml = new Yaml();
        File f = new File(path + "/" +filename);
        Map<String,String> result = new HashMap<>();
        try {
            result = yaml.load(new FileInputStream(f));
            LOGGER.info("Read config file from ["+ f.getAbsolutePath()+"]");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if ( result != null && result.get("dbserver")!= null ){
            DB_SERVER = result.get("dbserver");
        }
    }

    public static void main(String[] args){
        Config config = new Config();
        config.readConfigFile();
//        Config.readConfigFile();
        System.out.println(DB_SERVER);
    }

}

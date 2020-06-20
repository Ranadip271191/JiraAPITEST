package StepDefinations;
import Resources.library;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks extends library {
    private static Logger logger = LogManager.getLogger(Hooks.class);

    StepDefination01 stepdefination01 = new StepDefination01();

    @Before("@CreateIssue")
    public void GetProjectKey() throws Throwable {
        /*
            1. First check the default project key is exist or not
            2. If exists use the key
            3. If not create a new key and use it and update the key to global.properties file
         */
        logger.info("executing hooks.");
        StepDefination01.key = library.GlobalValue("defaultProjectKey");
        stepdefination01.user_get_the_key("createIssue");
        stepdefination01.user_calls_http_request("projectAPI","getProject","GET");
        int status_code =stepdefination01.response.getStatusCode();
        logger.info("Status code is "+status_code);
        if(status_code==404){
            logger.info("Global  project key already exists");
            PropertiesConfiguration p;
            p = new PropertiesConfiguration("src/test/java/Resources/global.properties");
            p.setProperty("projectKey",StepDefination01.key);
            p.save();
            StepDefination01.key=null;
            stepdefination01.user_get_the_key("createIssue");
        }else if(status_code == 200){
            logger.info("Default project key exists. Using default project key..");
        }else {
           // library.writevalue();
            StepDefination01.key=null;
            stepdefination01.user_get_the_key("createIssue");
            String key = StepDefination01.key;
            PropertiesConfiguration p;
            p = new PropertiesConfiguration("src/test/java/Resources/global.properties");
            p.setProperty("defaultProjectKey",key);
            p.save();
            logger.info("Default project key does not exists. Create a new Key and make it default");
        }
    }
    @After("@DeleteProject")
    public void deleteKey(){
        StepDefination01.key = null;
    }
}
//make a default project key from property file.




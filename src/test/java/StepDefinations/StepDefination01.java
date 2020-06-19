package StepDefinations;
import Resources.APIRecources;
import Resources.TestDataBuild;
import Resources.library;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class StepDefination01 extends library {
    private static Logger logger = LogManager.getLogger(StepDefination01.class);
    RequestSpecification req;
    public Response response;
    public static String key;
    TestDataBuild t =new TestDataBuild();

    public static RequestSpecification session_start(String APIName) throws IOException {
        library.requestspecification();
        logger.info("Session started succesfully for " + APIName);
        return library.requestspecification();
    }

    @Given("User send {string} payload")
    public void user_send_payload(String APIName) throws IOException, InvocationTargetException, IllegalAccessException {
        Method[] m = t.getClass().getDeclaredMethods();
        for (Method methodName : m) {
            if (methodName.getName().equalsIgnoreCase(APIName)) {
                req = given().spec(session_start(APIName))
                        .body(methodName.invoke(t, null));
                logger.info("Succesfully got "+APIName+" payload");
            }
        }

    }

    @When("user calls {string} to {string} with {string} http request")
    public void user_calls_http_request(String APIName, String resources, String method) throws InterruptedException, ConfigurationException, IOException {
        APIRecources apiRecources = APIRecources.valueOf(APIName);
        switch (resources) {
            case "createProject":
            case "createIssue":
                response = req.when().post(apiRecources.getResources());
                break;
            case "getProject":
                response = req.pathParam("pKey", key).when().get(apiRecources.getResources() + "{pKey}");
                break;
            case "updateProject":
                response = req.pathParam("pKey", key).body(t.updateProject()).when().put(apiRecources.getResources() + "{pKey}");
                break;
            case "deleteProject":
                response = req.pathParam("pKey", key).when().delete(apiRecources.getResources() + "{pKey}");
                break;
        }
        logger.info(resources + " API called with " + method + " method");
    }

    @Then("{string} API call got success with status code {string}")
    public void api_call_got_success_with_status_code(String ProjectAPI, String StatusCode) {
        if (ProjectAPI.equalsIgnoreCase("createProject")) {
            String res = response.asString();
            JsonPath js = new JsonPath(res);
            key = js.get("key");
            logger.info("Key is generated-> " + key);
        }
        assertEquals(Integer.parseInt(StatusCode), response.statusCode());
        logger.info(ProjectAPI + " asserted succesfully");
    }

    @Given("User get the {string} key")
    public void user_get_the_key(String APIName) throws Throwable {
        if (key == null) {
            logger.info("Key is null -> Create_Project is called to get the key");
            user_send_payload("createProject");
            user_calls_http_request("projectAPI", "createProject", "POST");
            api_call_got_success_with_status_code("createProject", "201");

        } else {
            req = given().spec(session_start(APIName));
        }


    }
}



package Resources;

import StepDefinations.Hooks;
import StepDefinations.StepDefination01;
import org.apache.commons.configuration.ConfigurationException;
import pojo.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestDataBuild {
    ArrayList<String> data;

    public Create_Project createProject() throws IOException, ConfigurationException {
        data = library.DataDriven("Project", "Craete_Porject");
        Create_Project cp = new Create_Project();
        library.writevalue();
        cp.setKey(library.GlobalValue("projectKey"));
        cp.setName(library.GlobalValue("projectname"));
        cp.setProjectTypeKey(data.get(2));
        cp.setProjectTemplateKey(data.get(3));
        cp.setLead(data.get(4));
        cp.setDescription(data.get(5));
        cp.setAvatarId(data.get(6));
        cp.setUrl(data.get(7));
        cp.setAssigneeType(data.get(8));
        return cp;
    }

    public update_project updateProject() throws IOException, ConfigurationException, InterruptedException {
        data = library.DataDriven("Project", "Update_Project");
        update_project up = new update_project();
        library.writevalue();
        Thread.sleep(2000);
        up.setKey(library.GlobalValue("projectKey"));
        up.setName(library.GlobalValue("projectname"));
        up.setProjectTypeKey(data.get(2));
        up.setProjectTemplateKey(data.get(3));
        up.setLead(data.get(4));
        up.setDescription(data.get(5));
        up.setAvatarId(data.get(6));
        up.setUrl(data.get(7));
        up.setAssigneeType(data.get(8));
        return up;

    }

    public HashMap<String, Object> createIssue() throws Throwable {
        /*
           1. Use Utils.DataDriven to get the data from excel sheet
           2. Create a HashMap Object "map" to add feilds
           3. Create a HashMap Object "fields" to add subsequent nested JSON
         */
        data = library.DataDriven("Issue", "Craete_Issue");
        String projectKey = StepDefination01.key;

        //Project key
        HashMap<String, Object> key = new HashMap<>();
        key.put("key", projectKey);

        //Assignee name
        HashMap<String, Object> assignee = new HashMap<>();
        assignee.put("name", data.get(2));

        //Repoter name
        HashMap<String, Object> reporter = new HashMap<>();
        reporter.put("name", data.get(3));

        //Issue Priority
        HashMap<String, Object> priority = new HashMap<>();
        priority.put("id", data.get(4));

        //Issue type
        HashMap<String, Object> issuetype = new HashMap<>();
        issuetype.put("name", data.get(5));

        //Labels
        ArrayList<String> lebels = new ArrayList<String>();
        lebels.add(data.get(6));
        lebels.add(data.get(7));

        HashMap<String, Object> fields = new HashMap<>();
        fields.put("project", key);
        fields.put("summary", data.get(8));
        fields.put("assignee", assignee);
        fields.put("reporter", reporter);
        fields.put("priority", priority);
        fields.put("description", data.get(9));
        fields.put("environment", data.get(10));
        fields.put("issuetype", issuetype);

        fields.put("labels", lebels);

        HashMap<String, Object> map = new HashMap<>();
        map.put("fields", fields);
        return map;

    }

}

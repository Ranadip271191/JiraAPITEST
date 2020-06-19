package Resources;

public enum APIRecources {

    projectAPI("/rest/api/2/project/"),
    issueAPI("/rest/api/2/issue/");

    private String resources;

    APIRecources(String resources) {
        this.resources = resources;
    }

    public String getResources() {
        return resources;
    }


}

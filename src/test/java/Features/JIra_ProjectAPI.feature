Feature: Jira Project API Validation
  @CreateProject
  Scenario: Create Project
    Given User send "createProject" payload
    When user calls "projectAPI" to "createProject" with "POST" http request
    Then "createProject" API call got success with status code "201"

  @GetProject
  Scenario: Get Project
    Given User get the "getProject" key
    When user calls "projectAPI" to "getProject" with "GET" http request
    Then "getProject" API call got success with status code "200"

  @UpdateProject
  Scenario: Update Project
    Given User get the "updateProject" key
    When user calls "projectAPI" to "updateProject" with "PUT" http request
    Then "updateProject" API call got success with status code "200"

  @DeleteProject
  Scenario: Delete Project
    Given User get the "deleteProject" key
    When user calls "projectAPI" to "deleteProject" with "Delete" http request
    Then "deleteProject" API call got success with status code "204"


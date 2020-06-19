Feature: Jira Issue API Validation
  @CreateIssue
  Scenario: Create Issue
    Given User send "createIssue" payload
    When user calls "issueAPI" to "createIssue" with "POST" http request
    Then "createIssue" API call got success with status code "201"


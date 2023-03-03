package com.exiger.stepDefs;

import com.exiger.utilities.BookitUtil;
import com.exiger.utilities.ConfigReader;
import com.exiger.utilities.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.serenitybdd.junit5.SerenityTest;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

import static io.restassured.RestAssured.given;

@SerenityTest
public class ApiTest {
    String token;
    String globalEmail;
    Response response;
    @Given("I logged Bookit api as a {string}")
    public void i_logged_bookit_api_as_a(String role) {
        token = BookitUtil.generateTokenByRole(role);
        System.out.println("token = " + token);

        Map<String, String> credentials = BookitUtil.credentials(role);
        globalEmail = credentials.get("email");
    }

    @When("I sent get request to {string} endpoint")
    public void i_sent_get_request_to_endpoint(String endpoint) {
        response = given().accept(ContentType.JSON).
                header("Authorization", token).
                when().get(ConfigReader.getProperty("base.url") + endpoint).prettyPeek();

    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer expectedStatusCode) {
        System.out.println("response.statusCode() = " + response.statusCode());
        Assertions.assertEquals(expectedStatusCode, response.statusCode());


    }

    @Then("content type is {string}")
    public void content_type_is(String expectedContentType) {
        System.out.println("response.contentType() = " + response.contentType());
        Assertions.assertEquals(expectedContentType, response.contentType());
    }

    @Then("role is {string}")
    public void role_is(String role) {
        System.out.println("response.path(\"role\") = " + response.path("role"));
        System.out.println("response.jsonPath().getString(\"role\") = " + response.jsonPath().getString("role"));

        Assertions.assertEquals(role, response.path("role"));
    }


    @Then("the information about current user from api and database should match")
    public void theInformationAboutCurrentUserFromApiAndDatabaseShouldMatch() {
/*
{
    "id": 17536,
    "firstName": "Lissie",
    "lastName": "Finnes",
    "role": "student-team-leader"
}
 */
        JsonPath jsonPath = response.jsonPath();
        String actualFirstName = jsonPath.getString("firstName");
        String actualLastName = jsonPath.getString("lastName");
        String actualRole = jsonPath.getString("role");

        //get the data from DB

        String query="select firstName,lastName,role from users where email='"+globalEmail+"'";

        DB_Util.runQuery(query);

        Map<String, String> dbMap = DB_Util.getRowMap(1);
        System.out.println("dbMap = " + dbMap);

        String expectedFirstName = dbMap.get("firstname");
        System.out.println("expectedFirstName = " + expectedFirstName);
        String expectedLastName = dbMap.get("lastname");
        System.out.println("expectedLastName = " + expectedLastName);
        String expectedRole = dbMap.get("role");
        System.out.println("expectedRole = " + expectedRole);

        Assertions.assertEquals(expectedFirstName, actualFirstName);
        Assertions.assertEquals(expectedLastName, actualLastName);
        Assertions.assertEquals(expectedRole, actualRole);

    }
}

package com.exiger.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookitUtil {

    public static String generateToken(String email, String password) {

        Response response = given().accept(ContentType.JSON)
                .queryParam("email", email)
                .and()
                .queryParam("password", password)
                .when().get(ConfigReader.getProperty("base.url") + "/sign");

        String token = "Bearer " + response.path("accessToken");

        return token;
    }

    public static String generateTokenByRole(String role) {
        Object accessToken = given().queryParams(credentials(role))
                .when().get(ConfigReader.getProperty("base.url") + "/sign").path("accessToken");

        return "Bearer " + accessToken;
    }

    public static Map<String, String> credentials(String role) {

        String email = "";
        String password = "";

        switch (role) {

            case "teacher":
                email = ConfigReader.getProperty("teacher.email");
                password = ConfigReader.getProperty("teacher.password");
                break;


            case "team-member":
                email = ConfigReader.getProperty("team.member.email");
                password = ConfigReader.getProperty("team.member.password");
                break;


            case "team-leader":
                email = ConfigReader.getProperty("team.leader.email");
                password = ConfigReader.getProperty("team.leader.password");
                break;

            default:

                throw new RuntimeException("Invalid Role Entry");
        }

        Map<String, String> email_pass = new HashMap<>();
        email_pass.put("email", email);
        email_pass.put("password", password);

        return email_pass;
    }


}

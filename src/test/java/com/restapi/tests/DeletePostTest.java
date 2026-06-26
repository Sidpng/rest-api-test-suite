package com.restapi.tests;

import com.restapi.utils.ApiConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Feature("Posts API")
public class DeletePostTest {

    @BeforeClass
    public void setup() {
        io.restassured.RestAssured.requestSpecification = ApiConfig.buildSpec();
    }

    @Test
    @Story("DELETE post")
    @Description("DELETE /posts/1 returns 200 with empty body")
    public void deletePost_returns200() {
        given()
            .when()
                .delete("/posts/1")
            .then()
                .statusCode(200);
    }

    @Test
    @Story("DELETE post — response body")
    @Description("DELETE /posts/1 returns an empty JSON object {}")
    public void deletePost_returnsEmptyBody() {
        given()
            .when()
                .delete("/posts/1")
            .then()
                .statusCode(200)
                .body("isEmpty()", org.hamcrest.Matchers.is(true));
    }
}

package com.restapi.tests;

import com.restapi.models.Post;
import com.restapi.utils.ApiConfig;
import com.restapi.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

@Feature("Posts API")
public class UpdatePostTest {

    @BeforeClass
    public void setup() {
        io.restassured.RestAssured.requestSpecification = ApiConfig.buildSpec();
    }

    @Test
    @Story("PUT full update")
    @Description("PUT /posts/1 replaces the entire resource and returns 200")
    public void putPost_returns200WithUpdatedData() {
        Post payload = TestDataFactory.createRandomPost();

        Post updated = given()
                .body(payload)
            .when()
                .put("/posts/1")
            .then()
                .statusCode(200)
                .extract().as(Post.class);

        assertEquals(updated.getTitle(), payload.getTitle());
        assertEquals(updated.getBody(), payload.getBody());
    }

    @Test
    @Story("PATCH partial update")
    @Description("PATCH /posts/1 updates only the title field and returns 200")
    public void patchPost_titleOnly_returns200() {
        String patchBody = "{\"title\": \"Patched Title Only\"}";

        Post patched = given()
                .body(patchBody)
            .when()
                .patch("/posts/1")
            .then()
                .statusCode(200)
                .extract().as(Post.class);

        assertEquals(patched.getTitle(), "Patched Title Only");
        // id is preserved
        assertEquals(patched.getId(), 1);
    }
}

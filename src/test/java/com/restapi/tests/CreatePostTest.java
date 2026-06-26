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
public class CreatePostTest {

    @BeforeClass
    public void setup() {
        io.restassured.RestAssured.requestSpecification = ApiConfig.buildSpec();
    }

    @Test
    @Story("POST create post")
    @Description("POST /posts returns 201 with the created resource echoed back")
    public void createPost_returns201WithCreatedBody() {
        Post payload = TestDataFactory.createRandomPost();

        Post created = given()
                .body(payload)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .extract().as(Post.class);

        assertTrue(created.getId() > 0, "Created post should have a server-assigned id");
        assertEquals(created.getTitle(), payload.getTitle());
        assertEquals(created.getBody(), payload.getBody());
        assertEquals(created.getUserId(), payload.getUserId());
    }

    @Test
    @Story("POST create post — validate response time")
    @Description("POST /posts responds within 5 seconds")
    public void createPost_respondsWithinAcceptableTime() {
        Post payload = TestDataFactory.createRandomPost();

        given()
                .body(payload)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .time(org.hamcrest.Matchers.lessThan(5000L));
    }

    @Test
    @Story("POST — different user IDs")
    @Description("POST /posts with userId=5 returns the correct userId in response")
    public void createPost_differentUserId_returnsCorrectUserId() {
        Post payload = TestDataFactory.createPostWithUserId(5);

        Post created = given()
                .body(payload)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .extract().as(Post.class);

        assertEquals(created.getUserId(), 5);
    }
}

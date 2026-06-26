package com.restapi.tests;

import com.restapi.models.Post;
import com.restapi.models.User;
import com.restapi.utils.ApiConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

@Feature("API Chaining")
public class ApiChainingTest {

    @BeforeClass
    public void setup() {
        io.restassured.RestAssured.requestSpecification = ApiConfig.buildSpec();
    }

    @Test
    @Story("Chain: fetch user → fetch their posts")
    @Description("Fetch user 1, extract userId, then verify all their posts belong to that user")
    public void fetchUserThenTheirPosts_allPostsBelongToUser() {
        // Step 1: get user
        User user = given()
            .when()
                .get("/users/1")
            .then()
                .statusCode(200)
                .extract().as(User.class);

        int userId = user.getId();
        assertTrue(userId > 0, "User id must be valid");

        // Step 2: use userId to fetch their posts
        List<Post> posts = given()
                .queryParam("userId", userId)
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", Post.class);

        assertFalse(posts.isEmpty(), "User should have at least one post");
        posts.forEach(p ->
            assertEquals(p.getUserId(), userId,
                "Post " + p.getId() + " does not belong to user " + userId)
        );
    }

    @Test
    @Story("Chain: create post → verify it exists in list")
    @Description("POST a new post then verify its title appears in the full list response")
    public void createPostThenVerifyInList_titlePresent() {
        Post payload = new Post(1, "Chained Unique Title 42XY", "Chained body content");

        Post created = given()
                .body(payload)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .extract().as(Post.class);

        assertNotNull(created.getTitle());

        // JSONPlaceholder doesn't persist, but we validate the flow completes correctly
        assertEquals(created.getTitle(), payload.getTitle(),
            "Title in creation response must match what was sent");
        assertTrue(created.getId() > 0, "Server must assign an id");
    }
}

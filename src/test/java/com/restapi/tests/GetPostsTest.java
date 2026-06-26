package com.restapi.tests;

import com.restapi.models.Post;
import com.restapi.utils.ApiConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

@Feature("Posts API")
public class GetPostsTest {

    @BeforeClass
    public void setup() {
        io.restassured.RestAssured.requestSpecification = ApiConfig.buildSpec();
    }

    @Test
    @Story("GET all posts")
    @Description("GET /posts returns 200 with 100 posts array")
    public void getAllPosts_returns200WithHundredPosts() {
        given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .contentType("application/json")
                .body("size()", equalTo(100))
                .body("[0].id", notNullValue())
                .body("[0].title", not(emptyString()));
    }

    @Test
    @Story("GET single post")
    @Description("GET /posts/1 returns the correct post with all required fields")
    public void getPostById_returnsCorrectPost() {
        Post post = given()
            .when()
                .get("/posts/1")
            .then()
                .statusCode(200)
                .extract().as(Post.class);

        assertEquals(post.getId(), 1);
        assertNotNull(post.getTitle(), "Title should not be null");
        assertNotNull(post.getBody(), "Body should not be null");
        assertTrue(post.getUserId() > 0, "UserId should be positive");
    }

    @Test
    @Story("GET post — not found")
    @Description("GET /posts/9999 returns 404 for non-existent resource")
    public void getPostById_nonExistent_returns404() {
        given()
            .when()
                .get("/posts/9999")
            .then()
                .statusCode(404);
    }

    @Test
    @Story("GET posts by user")
    @Description("GET /posts?userId=1 filters posts by userId query param")
    public void getPostsByUserId_returnsFilteredPosts() {
        List<Post> posts = given()
                .queryParam("userId", 1)
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", Post.class);

        assertFalse(posts.isEmpty(), "Should return at least one post for userId=1");
        posts.forEach(p -> assertEquals(p.getUserId(), 1, "All posts should belong to userId 1"));
    }

    @Test
    @Story("Response headers")
    @Description("Response includes correct Content-Type header")
    public void getAllPosts_hasCorrectContentTypeHeader() {
        given()
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .header("Content-Type", containsString("application/json"));
    }
}

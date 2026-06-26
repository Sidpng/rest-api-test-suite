package com.restapi.utils;

import com.restapi.models.Post;

import java.util.UUID;

public class TestDataFactory {

    public static Post createRandomPost() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return new Post(
                1,
                "Test Title - " + uniqueSuffix,
                "Auto-generated body for API test run " + uniqueSuffix
        );
    }

    public static Post createPostWithUserId(int userId) {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return new Post(
                userId,
                "Post by user " + userId + " - " + uniqueSuffix,
                "Body content for user " + userId
        );
    }
}

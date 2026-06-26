# REST API Test Suite

[![REST API Tests](https://github.com/Sidpng/rest-api-test-suite/actions/workflows/ci.yml/badge.svg)](https://github.com/Sidpng/rest-api-test-suite/actions/workflows/ci.yml)

A production-style REST API test suite using **Java + RestAssured + TestNG** against [JSONPlaceholder](https://jsonplaceholder.typicode.com) — covering the full CRUD lifecycle with API chaining, dynamic test data, and Allure reporting.

## What's covered

| Method | Endpoint | Tests |
|--------|----------|-------|
| GET | `/posts` | 200, count, headers, query filter |
| GET | `/posts/{id}` | field validation, 404 for missing |
| POST | `/posts` | 201, echoed body, response time |
| PUT | `/posts/{id}` | full resource replacement, 200 |
| PATCH | `/posts/{id}` | partial update (title only) |
| DELETE | `/posts/{id}` | 200, empty body |
| Chain | `/users/{id}` → `/posts?userId=` | cross-resource validation |

## Stack

- **RestAssured 5.4** — fluent HTTP DSL
- **TestNG 7.9** — parallel execution via `testng.xml`
- **Jackson** — POJO serialization/deserialization
- **Allure** — rich HTML test reports
- **GitHub Actions** — CI on push + nightly schedule

## Run locally

```bash
# Prerequisites: Java 11+, Maven 3.8+
mvn test
```

Results land in `target/surefire-reports`. For Allure HTML:

```bash
mvn allure:serve
```

## Project structure

```
src/test/java/com/restapi/
├── models/          # POJOs (Post, User)
├── tests/           # Test classes per HTTP method
└── utils/           # ApiConfig, TestDataFactory
```

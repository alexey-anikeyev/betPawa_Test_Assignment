package ee.betPawa.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITests {

    private RequestSpecification requestSpecification = RestAssured.with().baseUri("https://api.todoist.com");

    private Headers headers() {
        Header contentType = new Header("Content-Type", "application/json");
        Header requestId = new Header("X-Request-Id", UUID.randomUUID().toString());
        Header authorization = new Header("Authorization", "Bearer 9faff56976fdee154f328ecf09cf50288ad1a02a");
        return Headers.headers(contentType, requestId, authorization);
    }

    private JSONObject createNewObject() {
        JSONObject newJsonObject = new JSONObject();
        newJsonObject.put("content", "Buy Milk");
        newJsonObject.put("due_string", "tomorrow at 12:00");
        newJsonObject.put("due_lang", "en");
        newJsonObject.put("priority", 4);
        return newJsonObject;
    }

    private JSONObject updateObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", "Buy Coffee");
        return jsonObject;
    }

    @Test
    public void createTheNewTask() {
        ExtractableResponse extractableResponse = newExtractableResponse();

        String contentActual = actualValue(extractableResponse, "content");
        String contentExpected = expectedValue("content");
        assertEquals(contentExpected, contentActual, "Incorrect Subject");

        String priorityActual = actualValue(extractableResponse, "priority");
        String priorityExpected = expectedValue("priority");
        assertEquals(priorityExpected, priorityActual, "Incorrect Priority");

        String dateActual = actualNestedValue(extractableResponse, "due", "date");
        assertEquals(nextDate(), dateActual, "Incorrect Date");

        String whenActual = actualNestedValue(extractableResponse, "due", "string");;
        String whenExpected = expectedValue("due_string");
        assertEquals(whenExpected, whenActual, "Incorrect Date String, Should Be 'tomorrow at 12:00'");
    }

    @Test
    public void updateTheTask() {
        ExtractableResponse extractableResponseAdd = newExtractableResponse();

        updateExtractableResponse(extractableResponseAdd);

        Response response = response(extractableResponseAdd);

        String contentActual = actualValue(response, "content");
        String contentExpected = expectedValueUpdate("content");
        assertEquals(contentExpected, contentActual, "Incorrect Subject");

        String priorityActual = actualValue(response, "priority");
        String priorityExpected = expectedValue("priority");
        assertEquals(priorityExpected, priorityActual, "Incorrect Priority");

        String dateActual = actualNestedValue(response, "due", "date");
        assertEquals(nextDate(), dateActual, "Incorrect Date");

        String whenActual = actualNestedValue(response, "due", "string");;
        String whenExpected = expectedValue("due_string");
        assertEquals(whenExpected, whenActual, "Incorrect Date String, Should Be 'tomorrow at 12:00'");
    }

    @Test
    public void deleteTheTask() {
        ExtractableResponse extractableResponseAdd = newExtractableResponse();

        deleteExtractableResponse(extractableResponseAdd);
    }

    private ExtractableResponse newExtractableResponse() {
        return RestAssured.given().spec(requestSpecification)
                .headers(headers())
                .basePath("/rest/v1/tasks")
                .contentType(ContentType.JSON)
                .body(createNewObject().toString())
                .post().then().assertThat().statusCode(200).extract();
    }

    private void updateExtractableResponse(ExtractableResponse extractableResponseAdd) {
        RestAssured.given().spec(requestSpecification)
                .headers(headers())
                .basePath("/rest/v1/tasks")
                .contentType(ContentType.JSON)
                .body(updateObject().toString())
                .post("/" + id(extractableResponseAdd))
                .then().assertThat().statusCode(204).extract();
    }

    private void deleteExtractableResponse(ExtractableResponse extractableResponseAdd) {
        RestAssured.given().spec(requestSpecification)
                .headers(headers())
                .basePath("/rest/v1/tasks")
                .delete("/" + id(extractableResponseAdd))
                .then().assertThat().statusCode(204)
                .body(Matchers.blankOrNullString()).extract();
    }

    private Response response(ExtractableResponse extractableResponseAdd) {
        return RestAssured.given().spec(requestSpecification)
                .headers(headers())
                .basePath("/rest/v1/tasks")
                .get("/" + id(extractableResponseAdd));
    }

    private String actualValue(ExtractableResponse extractableResponse, String jsonObjectKey) {
        return new JSONObject(extractableResponse.body().asString()).get(jsonObjectKey).toString();
    }

    private String actualValue(Response response, String jsonObjectKey) {
        return new JSONObject(response.getBody().asString()).get(jsonObjectKey).toString();
    }

    private String actualNestedValue(ExtractableResponse extractableResponse, String jsonObjectKey, String jsonObjectNestedKey) {
        return new JSONObject(new JSONObject(extractableResponse.body().asString()).get(jsonObjectKey).toString()).get(jsonObjectNestedKey).toString();
    }

    private String actualNestedValue(Response response, String jsonObjectKey, String jsonObjectNestedKey) {
        return new JSONObject(new JSONObject(response.getBody().asString()).get(jsonObjectKey).toString()).get(jsonObjectNestedKey).toString();
    }

    private String expectedValue(String object) {
        return createNewObject().get(object).toString();
    }

    private String expectedValueUpdate(String object) {
        return updateObject().get(object).toString();
    }

    private String id(ExtractableResponse extractableResponse) {
        return new JSONObject(extractableResponse.body().asString()).get("id").toString();
    }

    public String nextDate() {
        return LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

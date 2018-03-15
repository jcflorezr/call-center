package net.learningpath.callcenter.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.learningpath.callcenter.config.EmbeddedTomcatConfig;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.failed.badrequest.BadRequestErrorResponse;
import net.learningpath.callcenter.employee.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedTomcatConfig.class})
@WebAppConfiguration
public class ControllerIT {

    @Value("${current-host:#{null}}")
    private Optional<String> currentHost;

    @Value("${tomcat-server-port:#{null}}")
    private Optional<Integer> serverPort;

    @Value("${endpoint-url}")
    private String endpointUrl;

    // happy path
    @Test
    public void callIsAttendedSuccessfully() {
        RestAssured.baseURI = currentHost.orElse(RestAssured.DEFAULT_URI);
        RestAssured.port = serverPort.orElse(RestAssured.DEFAULT_PORT);
        String clientName = "any client";
        Call call = new Call(clientName);
        Response actualResponse =
                given()
                    .contentType(ContentType.JSON)
                    .body(call)
                    .when()
                    .post(endpointUrl)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .and()
                    .contentType(ContentType.JSON)
                    .extract().response().as(Response.class);
        assertNotNull(actualResponse);
        assertTrue(actualResponse.isSuccess());
        assertThat(actualResponse.getMessage(), is(equalTo("Call was attended successfully !!!")));
        Call attendedCall = actualResponse.getCall();
        assertNotNull(attendedCall);
        assertThat(attendedCall.getClientName(), is(equalTo(clientName)));
        Employee attendedBy = attendedCall.getAttendedBy();
        assertNotNull(attendedBy);
    }

    @Test
    public void callInfoIsEmpty() {
        RestAssured.baseURI = currentHost.orElse(RestAssured.DEFAULT_URI);
        RestAssured.port = serverPort.orElse(RestAssured.DEFAULT_PORT);
        String requestBody = "{}";
        BadRequestErrorResponse actualResponse = (BadRequestErrorResponse)
                given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post(endpointUrl)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .and()
                    .contentType(ContentType.JSON)
                    .extract().response().as(Response.class);
        assertNotNull(actualResponse);
        assertFalse(actualResponse.isSuccess());
        assertThat(actualResponse.getMessage(), is(equalTo("Call info cannot be empty")));
        List<String> hints = actualResponse.getHints();
        assertNotNull(hints);
        assertThat(hints.size(), is(1));
        assertThat(hints.get(0), is(equalTo("Please add the call info with the following structure: {'clientName': 'client name'}")));
        assertNull(actualResponse.getCall());
    }

    @Test
    public void clientNameIsNull() {
        RestAssured.baseURI = currentHost.orElse(RestAssured.DEFAULT_URI);
        RestAssured.port = serverPort.orElse(RestAssured.DEFAULT_PORT);
        Call call = new Call(null);
        BadRequestErrorResponse actualResponse = (BadRequestErrorResponse)
                given()
                        .contentType(ContentType.JSON)
                        .body(call)
                        .when()
                        .post(endpointUrl)
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .and()
                        .contentType(ContentType.JSON)
                        .extract().response().as(Response.class);
        assertNotNull(actualResponse);
        assertFalse(actualResponse.isSuccess());
        assertThat(actualResponse.getMessage(), is(equalTo("Call info cannot be empty")));
        List<String> hints = actualResponse.getHints();
        assertNotNull(hints);
        assertThat(hints.size(), is(1));
        assertThat(hints.get(0), is(equalTo("Please add the call info with the following structure: {'clientName': 'client name'}")));
        assertNull(actualResponse.getCall());
    }

}

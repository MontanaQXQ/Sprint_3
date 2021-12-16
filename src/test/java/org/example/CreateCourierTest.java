package org.example;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {

    ScooterRegisterCourier generatorOfCouriers = new ScooterRegisterCourier();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

    }
/*
    @After
    public void tearDown() {
        File json = new File("src/test/resources/loginCourier.json");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().extract().body().path("id");
        int curierId = response.then().extract().body().path("id");
        System.out.println(curierId);
        given()
                .delete("/api/v1/courier/{curierId}", curierId )
                .then().assertThat().statusCode(200);

    }
*/

    @Test
    public void testCourierCanCreate() {
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(201);
    }



    @Test
    public void testCantCreateSameCourier() {
        ScooterRegisterCourier createCourier = new ScooterRegisterCourier();
        createCourier.registerNewCourierAndReturnLoginPassword();
        System.out.println(createCourier.registerNewCourierAndReturnLoginPassword());
    }


}



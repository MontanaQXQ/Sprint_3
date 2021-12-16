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



    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

    }

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
        System.out.println("Получил Айди курьера = " + curierId);
        given()
                .delete("/api/v1/courier/{curierId}", curierId )
                .then().assertThat().statusCode(200);
        System.out.println("Удалил курьера id = " +  curierId);
        System.out.println("----------------------------------------------------------------");

    }


    @Test
    public void testCourierCanCreate() {
        System.out.println("Проверяю что курьера можно создать");
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
        System.out.println(response.getStatusCode());

    }

    @Test
    public void testCantCreateSameCourier() {
        System.out.println("проверяю что нельзя содать одного и того же курьера");
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
        System.out.println( response.getBody().asString() + " Создал первого курьера");
        System.out.println(response.getStatusCode());


        Response responseTwo =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        responseTwo.then().assertThat()
                .statusCode(409);
        System.out.println(responseTwo.getBody().asString() + "Попытка создать второго курьера с тем же логином");
        System.out.println(responseTwo.getStatusCode());

    }

}






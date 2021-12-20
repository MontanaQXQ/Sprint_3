package org.example;


import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import org.junit.Assert;

import java.util.ArrayList;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginCourier {

    ScooterRegisterCourier createCourier = new ScooterRegisterCourier();
    private int courierId;
    String loginCourier = "/api/v1/courier/login";
    String deleteCourier = "/api/v1/courier/{curierId}";



    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

    }

    @After
    public void tearDown() {
        if (courierId > 0) {
            Response response =
                    given()
                            .delete(deleteCourier, courierId);
            System.out.println("Удалил курьера(After метод) id = " + courierId);
            System.out.println(response.getStatusCode());
            System.out.println("----------------------------------------------------------------");

        }
    }

    @Test
    public void testCourierCanCreate() {
         courierId =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(getAuth())
                        .when()
                        .post(loginCourier)
                        .then().assertThat().statusCode(200)
                        .and().extract().body().path("id");

        }

    @Test
    public void testCourierCanCreateTwo() {
        courierId =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(getAuth())
                        .when()
                        .post(loginCourier)
                        .then().assertThat().statusCode(200)
                        .and().extract().body().path("id");

    }





    private AuthCourier getAuth() {
        ArrayList<String> authCourier = createCourier.registerNewCourierAndReturnLoginPassword();
        String myLoginCourier = authCourier.get(0);
        String myPasswordCourier = authCourier.get(1);
        AuthCourier auth = new AuthCourier(myLoginCourier, myPasswordCourier);
        return auth;
    }
}



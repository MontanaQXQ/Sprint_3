package org.example;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

public class СreateCourierTest {

    ScooterRegisterCourier generatorOfCouriers = new ScooterRegisterCourier();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";

    }


    @Test
    public void testCourierCanCreate() {
        generatorOfCouriers.registerNewCourierAndReturnLoginPassword();


    }

}


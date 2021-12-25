package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.util.List;
import io.restassured.RestAssured;
import java.util.ArrayList;


@RunWith(Parameterized.class)
public class CreateOrderParamTest {



    String makeOrder = "/api/v1/orders";

    @Before
    public void setUp() {
     //  orderClient = new OrderClient();
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    private final List<String> color;
    private final Matcher<Object> expected;

    public CreateOrderParamTest(List<String> color, Matcher<Object> expected) {
        this.color = color;
        this.expected = expected;

    }


    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {List.of("BLACK", "GREY"), notNullValue()},
                {List.of("BLACK"), notNullValue()},
                {List.of("GREY"), notNullValue()},
                {null, notNullValue()}

        };
    }

   // OrderClient orderClient;

    @Test
    @DisplayName("Создание заказа. Позитивный сценарий.")
    @Description("Проверяем,что заказ создается в разными комбинациями color.")
    public void createOrder() {
        Order order = new Order(color);


        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post(makeOrder);
        response.then().assertThat().statusCode(201).and()
             .body("track", expected);
        System.out.println(response.getBody().asString());
        System.out.println(response.getStatusCode());
        /*
        orderClient.createOrder(order);
        response.then().assertThat().statusCode(201)
               .and()
               .body("track", expected);

         */
    }


}

package org.example;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class GetOrderListTest {

    String getOrderList = "/api/v1/orders?limit=10&page=0";

    @Test
    public void testGetOrderList() {
        System.out.println("Кейс: Получаю список доступных заказов и  проверяю,что в тело ответа возвращается список заказов.");
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        Response response =given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(getOrderList);
        response.then().assertThat().statusCode(200)
                .and().body(notNullValue());
        System.out.println(response.getBody().asString());
        System.out.println(response.getStatusCode());
    }

}
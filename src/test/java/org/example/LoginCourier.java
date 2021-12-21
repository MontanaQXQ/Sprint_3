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
import static io.restassured.RestAssured.responseSpecification;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class LoginCourier {

    ScooterRegisterCourier createCourier = new ScooterRegisterCourier();
    private AuthCourier currentCourier;
    String loginCourier = "/api/v1/courier/login";
    String deleteCourier = "/api/v1/courier/{curierId}";



    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
       currentCourier=getCorrectAuth();

    }

    @After
    public void tearDown() {
        if (currentCourier.id > 0) {
            Response response =
                    given()
                            .delete(deleteCourier, currentCourier.id);

            System.out.println("Удалил курьера(After метод) id = " + currentCourier.id);
            System.out.println(response.getStatusCode());
            System.out.println("----------------------------------------------------------------");
            currentCourier=null;

        }
    }

    @Test
    public void testCourierLogin() {
        System.out.println("Кейс: Курьер может авторизоваться");

                int id=given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(currentCourier)
                        .when()
                        .post(loginCourier)
                        .then().assertThat().statusCode(200)
                        .and().extract().body().path("id");
                assertEquals(id,currentCourier.id);
        System.out.println("Курьер авторизовался." + "\n" + "Айди курьера = " + currentCourier.id);

        }

    @Test
    public void testCourierAuthWithRequiredFields() {
        System.out.println("Кейс: для авторизации нужно передать все обязательные поля");

                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(currentCourier)
                        .when()
                        .post(loginCourier)
                        .then().assertThat().statusCode(200)
                        .and().extract().body().path("id");
        System.out.println("Курьер авторизовался." + "\n" + "Айди курьера = " + currentCourier.id);
    }

    @Test
    public void testSystemErrorWithIncorrectPassword() {
        System.out.println("Кейс: система вернёт ошибку, если неправильно указать логин или пароль");
        System.out.println("Указываем некорректный пароль");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(new AuthCourier(currentCourier.login,"****WrongPassword****"))
                        .when()
                        .post(loginCourier);
                        response.then().assertThat().statusCode(404)
                        .and().body("message", is("Учетная запись не найдена"));
        System.out.println(response.getBody().asString() + " Авторизация курьера");
        System.out.println(response.getStatusCode());

    }

    @Test
    public void testSystemErrorWithIncorrectLogin() {
        System.out.println("Кейс: система вернёт ошибку, если неправильно указать логин или пароль");
        System.out.println("Указываем некорректный Логин");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(new AuthCourier("****WrongLogin****",currentCourier.password))
                        .when()
                        .post(loginCourier);
        response.then().assertThat().statusCode(404)
                .and().body("message", is("Учетная запись не найдена"));
        System.out.println(response.getBody().asString() + " Авторизация курьера");
        System.out.println(response.getStatusCode());

    }

    @Test
    public void testAuthWithNullField() {
        System.out.println("Кейс: если какого-то поля нет, запрос возвращает ошибку");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(new AuthCourier("",currentCourier.password))
                        .when()
                        .post(loginCourier);
        response.then().assertThat().statusCode(400)
                .and().body("message", is("Недостаточно данных для входа"));
        System.out.println(response.getBody().asString() + " Авторизация курьера");
        System.out.println(response.getStatusCode());

    }

    @Test
    public void testNonExistUser() {
        System.out.println("Кейс:если авторизоваться под несуществующим пользователем, запрос возвращает ошибку");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(new AuthCourier("NonExsistUser3000","NonExsistPassword3000"))
                        .when()
                        .post(loginCourier);
        response.then().assertThat().statusCode(404)
                .and().body("message", is("Учетная запись не найдена"));
        System.out.println(response.getBody().asString() + " Авторизация курьера");
        System.out.println(response.getStatusCode());

    }

    @Test
    public void testLoginCourierreturnId() {
        System.out.println("Успешный запрос возвращает id");

        int id=given()
                .header("Content-type", "application/json")
                .and()
                .body(currentCourier)
                .when()
                .post(loginCourier)
                .then().assertThat().statusCode(200)
                .and().extract().body().path("id");
        assert id != 0;
        assertEquals(id,currentCourier.id);
        System.out.println("Курьер авторизовался." + "\n" + "Айди курьера = " + currentCourier.id);

    }





    private AuthCourier getCorrectAuth() {
        ArrayList<String> authCourier = createCourier.registerNewCourierAndReturnLoginPassword();
        String myLoginCourier = authCourier.get(0);
        String myPasswordCourier = authCourier.get(1);
        AuthCourier auth = new AuthCourier(myLoginCourier, myPasswordCourier);
        saveId(auth);
        System.out.println(auth.id);
        return auth;
    }
    private void saveId(AuthCourier courier){
        int id= given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(loginCourier)
                .then().assertThat().statusCode(200)
                .and().extract().body().path("id");
        courier.id=id;
    }

}



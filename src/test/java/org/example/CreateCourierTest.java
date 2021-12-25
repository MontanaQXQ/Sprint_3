package org.example;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;



import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {

    String loginCourier = "/api/v1/courier/login";
    String createCourier = "/api/v1/courier";
    String deleteCourier = "/api/v1/courier/{curierId}";


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
                        .post(loginCourier);

        if (response.then().extract().statusCode() ==200) {
            Object courierId = response.then().extract().body().path("id");
            System.out.println("Получил Айди курьера = " + courierId);
            given()
                    .delete(deleteCourier, courierId)
                    .then().assertThat().statusCode(200)
                    .and().body("ok", is(true));
            System.out.println("Удалил курьера(After метод) id = " + courierId);
            System.out.println(response.getStatusCode());
            System.out.println("----------------------------------------------------------------");

        } else {

            System.out.println("Айди курьера не был получен  " );
        }

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
                        .post(createCourier);
        response.then().assertThat()
                .statusCode(201)
                .and().body("ok", is(true));
        System.out.println(response.getStatusCode());


    }




    @Test
    public void testCantCreateSameCourier() {
        System.out.println("нельзя создать двух одинаковых курьеров");
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(createCourier);
        response.then().assertThat()
                .statusCode(201)
                .and().body("ok", is(true));
        System.out.println(response.getBody().asString() + " Создал первого курьера");
        System.out.println(response.getStatusCode());

        File jsonTwo = new File("src/test/resources/newCourier.json");
        Response responseTwo =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(jsonTwo)
                        .when()
                        .post(createCourier);
        responseTwo.then().assertThat()
                .statusCode(409)
                .and().body(notNullValue());
        System.out.println(responseTwo.getBody().asString() + "Попытка создать второго курьера с теми же данными");
        System.out.println(responseTwo.getStatusCode());

    }

    @Test
    public void testCourierCreatingWithRequiredFields() {
        System.out.println("Кейс: Чтобы создать курьера, нужно передать в ручку все обязательные поля");
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(createCourier);
        response.then().assertThat()
                .statusCode(201)
                .and().body("ok", is(true));
        System.out.println(response.getStatusCode());

    }

    @Test
    public void testCourierCreateReturnCorrectSatusCode() {
        System.out.println("Кейс: Запрос возвращает правильный код ответа");
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(createCourier);
        response.then().assertThat()
                .statusCode(201)
                .and().body("ok", is(true));
        System.out.println(response.getStatusCode());


    }

    @Test
    public void testCourierCreatSuccessBodyOkTrue() {
        System.out.println("Кейс: успешный запрос возвращает ok: true");
        File json = new File("src/test/resources/newCourier.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(createCourier);
        response.then().assertThat()
                .statusCode(201)
                .and().body("ok", is(true));
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody().asString());

    }

    @Test
    public void testCourierCreatReturnErrorIfNoOneField() {
        System.out.println("Кейс: Если одного из полей нет, запрос возвращает ошибку");
        File json = new File("src/test/resources/createCourierWithoutField.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(createCourier);
         response.then().assertThat()
                .statusCode(400)
                .and().body("message", is("Недостаточно данных для создания учетной записи"));
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody().asString());

    }


        @Test
        public void testCourierCreatReturnErrorIfLoginIsCreate() {
            System.out.println("Кейс: если создать пользователя с логином, который уже есть, возвращается ошибка.");
            File json = new File("src/test/resources/newCourier.json");
            Response response =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body(json)
                            .when()
                            .post(createCourier);
            response.then().assertThat()
                    .statusCode(201)
                    .and().body("ok", is(true));
            System.out.println(response.getBody().asString() + " Создал первого курьера");
            System.out.println(response.getStatusCode());

            File jsonTwo = new File("src/test/resources/sameLoginCourier.json");
            Response responseTwo =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body(jsonTwo)
                            .when()
                            .post(createCourier);
            responseTwo.then().assertThat()
                    .statusCode(409)
                    .and().body("message", is("Этот логин уже используется"));
            System.out.println(responseTwo.getBody().asString() + "Попытка создать второго курьера с тем же Логином");
            System.out.println(responseTwo.getStatusCode());

        }

}






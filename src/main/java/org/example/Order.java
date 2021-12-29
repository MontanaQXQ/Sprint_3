package org.example;



import java.time.LocalDateTime;
import java.util.List;

public class Order {


    public final String firstName;
    public final String lastName;
    public final String address;
    public final String metroStation;
    public final String phone;
    public final int rentTime;
    public final String deliveryDat;
    public final String comment;
    public final List<String> color;


    public Order(List<String>color) {
        this.firstName = "Ивэн";
        this.lastName = "Ивэнэв";
        this.address = "ул.нижняя первомайская,33";
        this.metroStation = "Первомайская";
        this.phone = "89651678964";
        this.rentTime = 4;
        this.deliveryDat = LocalDateTime.now().toString();
        this.comment = "Комментарий для курьера";
        this.color = color;
    }



}




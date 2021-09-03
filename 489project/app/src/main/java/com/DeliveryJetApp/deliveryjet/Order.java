package com.DeliveryJetApp.deliveryjet;

public class Order {
    String key = "";
    String orderId="";
    String email="";
    String details="";
    String weight = "";
    String customerName = "";
    String customerEmail = "";
    String phone ="";
    String address = "";
    String date = "";
    String status="";
    String code="";

    public Order(String key,String orderId,String email,String details,String weight,String customerName,String customerEmail,String phone,String address,String date,String status,String code){
        this.key = key;
        this.orderId=orderId;
        this.email=email;
        this.details = details;
        this.weight = weight;
        this.customerName = customerName;
        this.customerEmail=customerEmail;
        this.phone = phone;
        this.address = address;
        this.date = date;
        this.status=status;
        this.code=code;
    }
}
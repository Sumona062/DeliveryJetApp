package com.example.deliveryjet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.text.InputFilter;

public class CreateOrder extends Activity {
    private EditText orderIdTF,customerNameTF,addressTF,phoneTF,weightTF,dateTF,detailsTF,codeTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        orderIdTF=findViewById(R.id.eOrderId);
        customerNameTF=findViewById(R.id.eCustomerName);
        phoneTF=findViewById(R.id.eCustomerNo);
        weightTF=findViewById(R.id.eWeight);
        addressTF=findViewById(R.id.eCustomerAddress);
        setEditTextMaxLength(phoneTF,11);
        dateTF=findViewById(R.id.eDueDate);
        detailsTF=findViewById(R.id.eOrderDetails);
        codeTF=findViewById(R.id.eCode);
        setEditTextMaxLength(codeTF,9);


        Intent i= getIntent();
        String key=i.getStringExtra("ORDERID");
        Intent i1= getIntent();
        String email=i1.getStringExtra("email");


        initializeFormWithExistingData(key);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveEventData(email);

            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CreateOrder.this,CompanyFeed.class);
                i.putExtra("ComEmail",email);
                startActivity(i);

                CreateOrder.this.finish();

            }
        });

    }
    private  void saveEventData(String email){
        String orderId = orderIdTF.getText().toString();
        String customerName = customerNameTF.getText().toString();
        String phone = phoneTF.getText().toString();
        String weight = weightTF.getText().toString();
        String address = addressTF.getText().toString();
        String date = dateTF.getText().toString();
        String details=detailsTF.getText().toString();
        String code=codeTF.getText().toString();

        String errorMSG="";

        if(orderId.isEmpty()) {
            errorMSG = "Order Id can not be empty.\n";
        }
        if(customerName.isEmpty()){
            errorMSG +="Customer Name can not be empty.\n";
        }
        if(weight.isEmpty()){
            errorMSG +="Weight can not be empty.\n";
        }
        if(address.isEmpty()){
            errorMSG +="Address can not be empty.\n";
        }
        if(date.isEmpty()){
            date += "No Due Date";
        }
        if(details.isEmpty()){
            details += "No Details Available";
        }

        if(phone.isEmpty()){
            errorMSG +="Customer contact no: can not be empty.\n";
        }
        if(code.isEmpty()||code.length()<5){
            errorMSG +="Order code length must be at least 5.\n";
        }


        if(errorMSG.isEmpty()){
            String value=orderId+";"+email+";"+details+";"+weight+";"+customerName+";"+phone+";"+address+";"+date+";"+"Due"+";"+code;
            String key= orderId+";"+email;

            System.out.print("key: "+key);
            System.out.print("value: "+value);

            Util.getInstance().setKeyValue(CreateOrder.this,key,value);
            showDialog("Successfully Saved the Order","Info!","OK",false,email);
        }
        else{
            showDialog(errorMSG,"Error in Data!!","Back",true,email);
        }

    }
    private void initializeFormWithExistingData(String key){
        String value =Util.getInstance().getValueByKey(this,key);
        System.out.println("Value: "+value);


        if(value!= null) {

            String[] fieldValues = value.split(";");
            String orderId=fieldValues[0];
            String email=fieldValues[1];
            String details=fieldValues[2];
            String weight = fieldValues[3];
            String customerName = fieldValues[4];
            String phone = fieldValues[5];
            String address = fieldValues[6];
            String date = fieldValues[7];
            String code = fieldValues[9];

            detailsTF.setText(details);
            orderIdTF.setText(orderId);
            weightTF.setText(weight);
            customerNameTF.setText(customerName);
            phoneTF.setText(phone);
            addressTF.setText(address);
            dateTF.setText(date);
            codeTF.setText(code);

        }
        else{
            System.out.println("Data not found");
        }
    }


    public void setEditTextMaxLength(EditText et, int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }
    private void showDialog(String message,String title,String buttonLabel,boolean closeDialog,String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(true)
                .setNegativeButton(buttonLabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(closeDialog){
                            dialog.cancel();
                        }
                        else{
                            Intent i = new Intent(CreateOrder.this, CompanyFeed.class);
                            i.putExtra("ComEmail",email);
                            startActivity(i);
                            finish();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

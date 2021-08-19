package com.example.deliveryjet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class OrderDetails extends Activity {
    private TextView orderIdTF,customerNameTF,addressTF,phoneTF,weightTF,dateTF,detailsTF,comNameTF,comEmailTF,comAddressTF,comPhoneTF;
    private EditText codeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderIdTF=findViewById(R.id.eOrderId);
        customerNameTF=findViewById(R.id.eCustomerName);
        phoneTF=findViewById(R.id.eCustomerNo);
        weightTF=findViewById(R.id.eWeight);
        addressTF=findViewById(R.id.eCustomerAddress);
        dateTF=findViewById(R.id.eDueDate);
        detailsTF=findViewById(R.id.eOrderDetails);
        codeET=findViewById(R.id.eCode);
        comNameTF=findViewById(R.id.eCompanyName);
        comEmailTF=findViewById(R.id.eCompanyEmail);
        comAddressTF=findViewById(R.id.eCompanyAddress);
        comPhoneTF=findViewById(R.id.eCompanyPhone);


        Intent i= getIntent();
        String key=i.getStringExtra("ORDERID");
        Intent i1= getIntent();
        String email=i1.getStringExtra("DelEmail");



        initializeWithExistingData(key);

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveCode(key,email);

            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OrderDetails.this,DeliveryManFeed.class);
                i.putExtra("DelEmail",email);
                startActivity(i);

                OrderDetails.this.finish();

            }
        });

    }
    private  void saveCode(String key,String delEmail){
        String val =Util.getInstance().getValueByKey(this,key);
        System.out.println("Value: "+val);


        if(val!= null) {

            String[] fieldValues = val.split(";");
            String orderId = fieldValues[0];
            String email = fieldValues[1];
            String details = fieldValues[2];
            String weight = fieldValues[3];
            String customerName = fieldValues[4];
            String phone = fieldValues[5];
            String address = fieldValues[6];
            String date = fieldValues[7];
            String code = fieldValues[9];

            String delCode = codeET.getText().toString();

            String errorMSG = "";

            if (delCode.isEmpty()) {
                errorMSG = "You must enter the correct order code to complete this delivery, Please re-try with the correct code!!\n";
            }

            if (errorMSG.isEmpty() && code.equals(delCode)) {
                String value = orderId + ";" + email + ";" + details + ";" + weight + ";" + customerName + ";" + phone + ";" + address + ";" + date + ";" + "Delivered"+";"+code;

                System.out.print("key: " + key);
                System.out.print("value: " + value);

                Util.getInstance().setKeyValue(OrderDetails.this, key, value);
                showDialog("Successfully Delivered the Order", "Info!", "OK", false, delEmail,key);
            } else {
                errorMSG = "You must enter the correct order code to complete this delivery, Please re-try with the correct code!!\n";
                showDialog(errorMSG,"Error!!" , "Back", true, delEmail,key);
            }
        }

    }
    private void initializeWithExistingData(String key){
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


            String ComValue =Util.getInstance().getValueByKey(this,email);
            if(ComValue!=null){
                String[] fValues = ComValue.split(";");
                String comName=fValues[1];
                String ComAddress = "";
                String comPhone = fValues[3];
                if (fValues.length > 5) {
                    ComAddress = fValues[6];
                }
                comPhoneTF.setText(comPhone);
                comNameTF.setText(comName);
                comAddressTF.setText(ComAddress);
                comEmailTF.setText(email);

            }

            detailsTF.setText(details);
            orderIdTF.setText(orderId);
            weightTF.setText(weight);
            customerNameTF.setText(customerName);
            phoneTF.setText(phone);
            addressTF.setText(address);
            dateTF.setText(date);


        }
        else{
            System.out.println("Data not found");
        }
    }

    private void showDialog(String message,String title,String buttonLabel,boolean closeDialog,String email,String key){
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
                            Intent i = new Intent(OrderDetails.this, DeliveryManFeed.class);
                            i.putExtra("DelEmail",email);
                            startActivity(i);
                            finish();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

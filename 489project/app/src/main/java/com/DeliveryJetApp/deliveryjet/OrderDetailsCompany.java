package com.DeliveryJetApp.deliveryjet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OrderDetailsCompany extends Activity {
    private TextView orderIdTF,customerNameTF,customerEmailTF,addressTF,phoneTF,weightTF,dateTF,detailsTF,statusTF,codeTF,delNameTF,delEmailTF,delAddressTF,delPhoneTF,delDocTypeTF,delDocNoTF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_company);

        orderIdTF=findViewById(R.id.eOrderId);
        customerNameTF=findViewById(R.id.eCustomerName);
        customerEmailTF=findViewById(R.id.eCustomerEmail);
        phoneTF=findViewById(R.id.eCustomerNo);
        weightTF=findViewById(R.id.eWeight);
        addressTF=findViewById(R.id.eCustomerAddress);
        dateTF=findViewById(R.id.eDueDate);
        detailsTF=findViewById(R.id.eOrderDetails);
        statusTF=findViewById(R.id.eStatus);
        codeTF=findViewById(R.id.eCode);
        delNameTF=findViewById(R.id.eDelManName);
        delEmailTF=findViewById(R.id.eDelManEmail);
        delAddressTF=findViewById(R.id.eDelManAddress);
        delPhoneTF=findViewById(R.id.eDelManPhone);
        delDocTypeTF=findViewById(R.id.eDelManDocType);
        delDocNoTF=findViewById(R.id.eDelManDocNum);


        Intent i= getIntent();
        String key=i.getStringExtra("ORDERID");
        Intent i2=getIntent();
        String email=i2.getStringExtra("email");



        initializeWithExistingData(key);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OrderDetailsCompany.this,CompanyFeed.class);
                i.putExtra("ComEmail",email);
                startActivity(i);
                OrderDetailsCompany.this.finish();

            }
        });
        findViewById(R.id.btnLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(OrderDetailsCompany.this,LoginActivity.class);
                startActivity(i);
                OrderDetailsCompany.this.finish();

            }
        });

    }

    private void initializeWithExistingData(String key){
        String value =Util.getInstance().getValueByKey(this,key);
        System.out.println("Value: "+value);


        if(value!= null) {

            String[] fieldValues = value.split(";");
            String orderId=fieldValues[0];
            String delEmail=fieldValues[11];
            String details=fieldValues[2];
            String weight = fieldValues[3];
            String customerName = fieldValues[4];
            String customerEmail = fieldValues[5];
            String phone = fieldValues[6];
            String address = fieldValues[7];
            String date = fieldValues[8];
            String status = fieldValues[9];
            String code = fieldValues[10];


            String delValue =Util.getInstance().getValueByKey(this,delEmail);
            if(delValue!=null){
                String[] fValues = delValue.split(";");
                String delName=fValues[1];
                String delAddress = "";
                String DocType = "";
                String DocNum = "";
                String delPhone = fValues[3];
                if (fValues.length > 5) {
                    DocType = fValues[5];
                    DocNum = fValues[6];
                    delAddress = fValues[7];
                }
                delPhoneTF.setText(delPhone);
                delNameTF.setText(delName);
                delAddressTF.setText(delAddress);
                delEmailTF.setText(delEmail);
                delDocTypeTF.setText(DocType);
                delDocNoTF.setText(DocNum);

            }

            detailsTF.setText(details);
            orderIdTF.setText(orderId);
            weightTF.setText(weight);
            customerNameTF.setText(customerName);
            customerEmailTF.setText(customerEmail);
            phoneTF.setText(phone);
            addressTF.setText(address);
            dateTF.setText(date);
            statusTF.setText(status);
            codeTF.setText(code);


        }
        else{
            System.out.println("Data not found");
        }
    }



}

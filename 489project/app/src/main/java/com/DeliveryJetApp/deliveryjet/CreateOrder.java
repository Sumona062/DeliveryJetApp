package com.DeliveryJetApp.deliveryjet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;


import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class CreateOrder extends Activity {
    private EditText orderIdTF,customerNameTF,customerEmailTF,addressTF,phoneTF,weightTF,dateTF,detailsTF,codeTF;

    final String MY_API_KEY = "AIzaSyCjkI-TzN9giph0DnS1fFF1liDo9HbyQU0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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
        customerEmailTF=findViewById((R.id.eCustomerEmail));


        Intent i= getIntent();
        String key=i.getStringExtra("ORDERID");
        Intent i1= getIntent();
        String email=i1.getStringExtra("email");


        initializeFormWithExistingData(key);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    saveEventData(email,"Unassigned");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        findViewById(R.id.btnAssign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    saveEventData(email,"Assigned");
                } catch (Exception e) {
                    e.printStackTrace();
                }


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
    private int calculateDistance(String address1,String address2) throws Exception {

        String sourcePlace = address1.replace(" ","");
        String destinationPlace = address2.replace(" ","");
        System.out.println(sourcePlace);
        System.out.println(destinationPlace);

        String URL_str = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + sourcePlace + "&destinations=" + destinationPlace + "&key=" + MY_API_KEY;
        URL url = new URL(URL_str);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        String line, outputString = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        while ((line = reader.readLine()) != null) {
            outputString += line;
        }
        System.out.println(outputString);

        String value = null;
        JSONObject json = (JSONObject) new JSONParser().parse(outputString);


        JSONArray rows = ((JSONArray) json.get("rows"));


        for(int i=0;i<rows.size();i++){

            JSONObject second =(JSONObject) rows.get(i);
            JSONArray elements = (JSONArray) second.get("elements");

            for(int j=0;j<elements.size();j++){
                //System.out.println(elements.get(j));
                Map element = (Map) elements.get(j);
                //System.out.println(element);
                Iterator<Map.Entry> itr1 = element.entrySet().iterator();
                while (itr1.hasNext()) {

                    Map.Entry pair = itr1.next();

                    if(pair.getKey().toString().equals("distance"))
                        value = pair.getValue().toString();
                }

            }
        }

        //System.out.println(value);
        String v1 = value.replace("{", "");
        String v2 = v1.replace("}", "");

        int distance = 0;
        String[] pairs = v2.split(",");
        for (int i=0;i<pairs.length;i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");

            if(keyValue[0].equals("\"value\""))
                distance = Integer.parseInt(keyValue[1]);

        }

        //System.out.println(distance);

        return distance;

    }



    private String findDeliveryMan(Hashtable<String,Integer> distance){
        String minKey = null;
        int minValue = Integer.MAX_VALUE;

        Enumeration enu = distance.keys();

        while (enu.hasMoreElements()) {
            String key=enu.nextElement().toString();
            if(minValue>distance.get(key)){
                minValue=distance.get(key);
                minKey=key;
            }

        }
        return minKey;
    }
    private  void saveEventData(String email,String status) throws Exception {
        String orderId = orderIdTF.getText().toString();
        String customerName = customerNameTF.getText().toString();
        String phone = phoneTF.getText().toString();
        String weight = weightTF.getText().toString();
        String address = addressTF.getText().toString();
        String date = dateTF.getText().toString();
        String details=detailsTF.getText().toString();
        String code=codeTF.getText().toString();
        String cusEmail=customerEmailTF.getText().toString();

        String errorMSG="";

        if(orderId.isEmpty()) {
            errorMSG = "Order Id can not be empty.\n";
        }
        if(customerName.isEmpty()){
            errorMSG +="Customer Name can not be empty.\n";
        }
        if(cusEmail.isEmpty()){
            errorMSG +="Customer Email Address can not be empty.\n";
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
            String value=orderId+";"+email+";"+details+";"+weight+";"+customerName+";"+cusEmail+";"+phone+";"+address+";"+date+";"+status+";"+code;
            String key= orderId+";"+email;


            Util.getInstance().setKeyValue(CreateOrder.this,key,value);
            if(status.equals("Assigned")){
                Boolean flag=selectDeliveryMan(email,key);
                if(flag==true){
                    showDialog("Order assigned successfully placed!!","Info!","OK",false,email);
                }else{
                    showDialog("Order could not assigned!!","Error!","Back",true,email);
                }


            }else{
                showDialog("Order successfully placed!!","Info!","OK",false,email);
            }

        }
        else{
            showDialog(errorMSG,"Error in order information!!","Back",true,email);
        }

    }

    private boolean selectDeliveryMan(String email,String key) throws Exception{


        String value =Util.getInstance().getValueByKey(this,key);
        String message="";



        if(value!= null) {
            String[] fieldValues = value.split(";");
            if (fieldValues[9].equals("Assigned")) {
                String cusName = fieldValues[4];
                String customerEmail = fieldValues[5];
                String cusAddress = fieldValues[7];
                //System.out.println(cusAddress);

                String comValue = Util.getInstance().getValueByKey(this, email);
                String[] comValues = comValue.split(";");
                String comName = comValues[1];
                if (comValues.length > 5) {
                    String comAddress = comValues[6];
                    //System.out.println(comAddress);
                    Hashtable<String, Integer> distance = new Hashtable<String, Integer>();

                    KeyValueDB db = new KeyValueDB(this);
                    Cursor Rows = db.execute("SELECT * FROM key_value_pairs");
                    if (Rows.getCount() == 0) {
                        return false;
                    }
                    String deliveryMan = "";
                    String delName = "";
                    String delPhone = "";
                    while (Rows.moveToNext()) {

                        String key1 = Rows.getString(0);
                        String Data = Rows.getString(1);

                        if (!key1.contains(";")) {
                            String[] delValues = Data.split(";");

                            delPhone = delValues[3];
                            if (delValues.length > 5 && delValues[4].equals("DELIVERYMAN")) {
                                String delAddress = delValues[7];
                                //System.out.println(key1);
                                //System.out.println(delAddress);
                                int dist = calculateDistance(cusAddress, delAddress) + calculateDistance(comAddress, delAddress);
                                distance.put(key1, dist);
                                //System.out.println(distance);
                                deliveryMan = findDeliveryMan(distance);
                                delName = delValues[1];


                            }

                        }
                    }
                    if (deliveryMan.equals("")) {
                        return false;
                    } else {
                        value = value + ";" + deliveryMan;
                        Util.getInstance().setKeyValue(CreateOrder.this, key, value);
                        String newValue = Util.getInstance().getValueByKey(this, key);
                        //System.out.println(newValue);

                        String subject="Order information";
                        message="Hello "+cusName+"!, \n Your Order from '"+comName+"', \nOrder Id-"+fieldValues[0]+"\n Order code-"+fieldValues[10]+
                                "\n\n Will be delivered by-\nName-"+delName+"\nEmail-"+deliveryMan+"\nContact no-"+delPhone+"\n\n Enjoy you day!";
                        System.out.println(customerEmail);
                        System.out.println(subject);
                        System.out.println(message);
                        sendMail(customerEmail,subject,message);


                        return true;
                    }

                }else{
                    return false;
                }
            }else{
                return  false;
            }
        }
        else{
            return  false;
        }

    }

    private void sendMail(String customerEmail, String subject, String message) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,customerEmail);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));
    }


    private void initializeFormWithExistingData(String key)
    {
        String value =Util.getInstance().getValueByKey(this,key);
        //System.out.println("Value: "+value);


        if(value!= null) {

            String[] fieldValues = value.split(";");
            String orderId=fieldValues[0];
            String details=fieldValues[2];
            String weight = fieldValues[3];
            String customerName = fieldValues[4];
            String customerEmail = fieldValues[5];
            String phone = fieldValues[6];
            String address = fieldValues[7];
            String date = fieldValues[8];
            String code = fieldValues[10];

            detailsTF.setText(details);
            orderIdTF.setText(orderId);
            weightTF.setText(weight);
            customerNameTF.setText(customerName);
            customerEmailTF.setText(customerEmail);
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

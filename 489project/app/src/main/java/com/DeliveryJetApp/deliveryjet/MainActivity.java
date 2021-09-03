package com.DeliveryJetApp.deliveryjet;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.text.InputFilter;
import android.view.View;

public class MainActivity extends Activity {
    private EditText nameTF, phoneTF, pass1TF, pass2TF, emailTF;
    private RadioButton companyRB, deliverymanRB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTF=findViewById(R.id.eName);
        emailTF=findViewById(R.id.eEmail);
        pass1TF=findViewById(R.id.ePass1);
        pass2TF=findViewById(R.id.ePass2);
        phoneTF=findViewById(R.id.ePhone);
        setEditTextMaxLength(phoneTF,11);

        companyRB=findViewById(R.id.rbCompany);
        deliverymanRB=findViewById(R.id.rbDeliveryman);

        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventData();

            }
        });
        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                MainActivity.this.finish();
            }
        });


    }
    public void setEditTextMaxLength(EditText et, int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }


    private  void saveEventData(){
        String name = nameTF.getText().toString();
        String pass1 = pass1TF.getText().toString();
        String pass2 = pass2TF.getText().toString();
        String email = emailTF.getText().toString();
        String phone = phoneTF.getText().toString();



        String radioButtonValue="";
        if(companyRB.isChecked()){
            radioButtonValue="COMPANY";
        }
        else if(deliverymanRB.isChecked()){
            radioButtonValue="DELIVERYMAN";
        }

        String errorMSG="";

        if(name.isEmpty()||name.length()<5) {
            errorMSG = "Full name should have at least five character.\n";
        }
        if(pass1.isEmpty()||pass1.length()<8){
            errorMSG +="Password should have at least 8 character.\n";
        }
        if(pass2.isEmpty()||pass2.equals(pass1)==false){
            errorMSG +="Passwords are not matching.\n";
        }
        if(email.isEmpty()){
            errorMSG +="Email can not be empty.\n";
        }

        if(phone.isEmpty()){
            errorMSG +="Contact no: can not be empty.\n";
        }
        if(radioButtonValue.equals("")){
            errorMSG +="Please select any User type.\n";
        }


        if(errorMSG.isEmpty()){

            String value=email+";"+name+";"+pass1+";"+phone+";"+radioButtonValue;
            String key= email;

            System.out.print("\nkey: "+key);
            System.out.print("\nvalue: "+value);

            Util.getInstance().setKeyValue(MainActivity.this,key,value);
            showDialog("Registration Successful!!","Info!","Sign In",false);
        }
        else{
            showDialog(errorMSG,"Error in Data!!","Back",true);
        }
        System.out.println("\nName: "+ name);
        System.out.println("Email: "+ email);
        System.out.println("Pass1: "+ pass1);
        System.out.println("Pass2: "+ pass2);
        System.out.println("Phone: "+ phone);
        System.out.println("Type: "+ radioButtonValue);

    }

    private void showDialog(String message,String title,String buttonLabel,boolean closeDialog){
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
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }
                });

        AlertDialog alert = builder.create();

        alert.show();
    }


}




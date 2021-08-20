package com.example.deliveryjet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class EditCompanyProfile extends Activity {
    private EditText nameTF, pass1TF,pass2TF,addressTF, phoneTF, websiteTF;
    private TextView emailTV;
    private RadioButton nidRB, passportRB, drivingLicenceRB;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences sharedPreferences;
    String rememberVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile);
        sharedPreferences=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        prefsEditor=sharedPreferences.edit();
        rememberVal=sharedPreferences.getString("remember",null);

        nameTF=findViewById(R.id.eName);
        pass1TF=findViewById(R.id.ePass1);
        pass2TF=findViewById(R.id.ePass2);
        phoneTF=findViewById(R.id.ePhone);
        addressTF=findViewById(R.id.eAddress);
        websiteTF=findViewById(R.id.eWebsite);
        setEditTextMaxLength(phoneTF,11);
        emailTV=findViewById(R.id.labelEmail);


        Intent i= getIntent();
        String key=i.getStringExtra("email");
        System.out.println(key);

        initializeFormWithExistingData(key);
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EditCompanyProfile.this,CompanyFeed.class);
                i.putExtra("ComEmail",key);
                startActivity(i);
                EditCompanyProfile.this.finish();
            }
        });
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventData(key);

            }
        });
    }
    public void setEditTextMaxLength(EditText et, int length) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }

    private  void saveEventData(String email){
        String name = nameTF.getText().toString();
        String pass1 = pass1TF.getText().toString();
        String pass2 = pass2TF.getText().toString();
        String phone = phoneTF.getText().toString();
        String website = websiteTF.getText().toString();
        String address = addressTF.getText().toString();


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

        if(phone.isEmpty()){
            errorMSG +="Contact no: can not be empty.\n";
        }

        if(website.isEmpty()){
            errorMSG +="Website can not be empty.\n";
        }
        if(address.isEmpty()){
            errorMSG +="address can not be empty.\n";
        }



        if(errorMSG.isEmpty()){
            if(rememberVal!=null){
                String user = sharedPreferences.getString("email", null);
                String pswrd = sharedPreferences.getString("password", null);
                if(user.equals(email)){
                    prefsEditor.putString("password",pass1);
                }
                prefsEditor.commit();
            }
            String value=email+";"+name+";"+pass1+";"+phone+";"+"COMPANY"+";"+website+";"+address;
            String key= email;

            System.out.print("\nkey: "+key);
            System.out.print("\nvalue: "+value);

            Util.getInstance().setKeyValue(EditCompanyProfile.this,key,value);
            showDialog("Profile Successfully Updated!","Info!!","OK",false,email);
        }
        else{
            showDialog(errorMSG,"Error in Profile Information!!","Back",true,email);
        }


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
                            Intent i = new Intent(EditCompanyProfile.this, CompanyFeed.class);
                            i.putExtra("ComEmail",email);
                            startActivity(i);
                            finish();
                        }

                    }
                });

        AlertDialog alert = builder.create();

        alert.show();
    }
    private void initializeFormWithExistingData(String eventKey) {
        String value = Util.getInstance().getValueByKey(this, eventKey);
        System.out.println("Value: " + value);

        if (value != null) {
            String[] fieldValues = value.split(";");
            String name = fieldValues[1];
            String pass1=fieldValues[2];
            String address = "";
            String phone = fieldValues[3];
            String website = "";
            if (fieldValues.length > 5) {
                website = fieldValues[5];
                address = fieldValues[6];
            }
            emailTV.setText(eventKey);


            nameTF.setText(name);
            pass1TF.setText(pass1);
            pass2TF.setText(pass1);
            addressTF.setText(address);
            phoneTF.setText(phone);
            websiteTF.setText(website);


        }
            else {
                System.out.println("Data not found");
            }
        }



}

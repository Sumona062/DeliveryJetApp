package com.example.deliveryjet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class LoginActivity extends Activity {
    private EditText passTF, emailTF;
    private RadioButton companyRB, deliverymanRB;
    String errorMSG="",rememberVal;
    private CheckBox remMeCB;
    private SharedPreferences.Editor prefsEditor;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        prefsEditor=sharedPreferences.edit();
        rememberVal=sharedPreferences.getString("remember",null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTF=findViewById(R.id.eEmail);
        passTF=findViewById(R.id.ePass);
        remMeCB=findViewById(R.id.cbRemMe);

        companyRB=findViewById(R.id.rbCompany);
        deliverymanRB=findViewById(R.id.rbDeliveryman);




        if(rememberVal!=null){
            initialize();
        }



        findViewById(R.id.btnLogIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveEventData();
            }
        });
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
    private  void saveEventData(){
        errorMSG="";
        String pass = passTF.getText().toString();
        String email = emailTF.getText().toString();


        String radioButtonValue="";
        if(companyRB.isChecked()){
            radioButtonValue="COMPANY";
        }
        else if(deliverymanRB.isChecked()){
            radioButtonValue="DELIVERYMAN";
        }


        if(pass.isEmpty()){
            errorMSG +="Please Enter your password\n";
        }
        if(email.isEmpty()){
            errorMSG +="Please Enter your email.\n";
        }

        if(radioButtonValue==""){
            errorMSG +="Please select any User type.\n";
        }


        if(errorMSG.isEmpty()){
            checkUserExists(email,pass,radioButtonValue);

        }
        else{
            showDialog(errorMSG,"Error!");
        }


    }
    private void checkCheckbox(){

    }
    private void checkUserExists(String eventKey,String password,String userType){
        errorMSG="";
        String value =Util.getInstance().getValueByKey(this,eventKey);
        System.out.println("Value: "+value);

        if(value!= null) {
            String[] fieldValues = value.split(";");
            String pass = fieldValues[2];
            String type = fieldValues[4];
            if(pass.equals(password)==false){
                errorMSG +="Wrong Password.\n";
            }
            if(type.equals(userType)==false){
                errorMSG +="Wrong User type.\n";
            }


            if(errorMSG.isEmpty()) {
                String user = sharedPreferences.getString("email", null);
                String pswrd = sharedPreferences.getString("password", null);
                String Utype = sharedPreferences.getString("type", null);
                if(user==null||pswrd==null||Utype==null){
                    if(remMeCB.isChecked()){
                        prefsEditor.putString("email",eventKey);
                        prefsEditor.putString("password",password);
                        prefsEditor.putString("type",userType);
                        prefsEditor.putString("remember", "login");
                        prefsEditor.commit();
                    }
                    typeUser(userType,eventKey);

                }else if(user.equals(eventKey)){
                    if(remMeCB.isChecked()){
                        prefsEditor.putString("remember", "login");
                    }else{
                        prefsEditor.putString("email",null);
                        prefsEditor.putString("password",null);
                        prefsEditor.putString("type",null);
                        prefsEditor.putString("remember", null);

                    }
                    prefsEditor.commit();
                    typeUser(userType,eventKey);

                }else if(user!=null &&!user.equals(eventKey)){
                    showDialog("You are not an authorized user for this Device","Error!!");

                }


                }
            else{
                showDialog(errorMSG,"Error!!");
            }


        }
        else{
            errorMSG +="Email Does not match.\n";
            showDialog(errorMSG,"Error!!");
        }
    }
    public void typeUser(String userType,String key){
        if (userType.equals("DELIVERYMAN")) {

            Intent i = new Intent(LoginActivity.this, DeliveryManFeed.class);
            i.putExtra("DelEmail", key);
            startActivity(i);
            LoginActivity.this.finish();
        } else if (userType.equals("COMPANY")) {
            Intent i1 = new Intent(LoginActivity.this, CompanyFeed.class);
            i1.putExtra("ComEmail", key);
            startActivity(i1);

            LoginActivity.this.finish();
        }

    }
    private void showDialog(String message,String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(true)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.show();
    }
    private void initialize(){

        if(rememberVal.equals("login")){
            String user=sharedPreferences.getString("email",null);
            String pass=sharedPreferences.getString("password",null);
            String Utype=sharedPreferences.getString("type",null);

            emailTF.setText(user);
            passTF.setText(pass);
            remMeCB.setChecked(true);
            if(Utype.equals("DELIVERYMAN")){
                deliverymanRB.setChecked(true);
            }else if(Utype.equals("COMPANY")){
                companyRB.setChecked(true);
            }


        }

    }

}

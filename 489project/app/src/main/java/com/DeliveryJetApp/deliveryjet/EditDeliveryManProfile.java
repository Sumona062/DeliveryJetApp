package com.DeliveryJetApp.deliveryjet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class EditDeliveryManProfile extends Activity {
    private EditText nameTF, pass1TF,pass2TF,addressTF, phoneTF, documentNoTF;
    private RadioButton nidRB, passportRB, drivingLicenceRB;
    private SharedPreferences.Editor prefsEditor;
    private TextView emailTV;
    private SharedPreferences sharedPreferences;
    String rememberVal;
    Uri selectedImageUri;
    private ImageView image;
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences=getSharedPreferences("MySharedPref",MODE_PRIVATE);
        prefsEditor=sharedPreferences.edit();
        rememberVal=sharedPreferences.getString("remember",null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryman_edit_profile);


        nameTF=findViewById(R.id.eName);
        pass1TF=findViewById(R.id.ePass1);
        pass2TF=findViewById(R.id.ePass2);
        phoneTF=findViewById(R.id.ePhone);
        addressTF=findViewById(R.id.eAddress);
        documentNoTF=findViewById(R.id.eDocNum);
        setEditTextMaxLength(phoneTF,11);
        emailTV=findViewById(R.id.labelEmail);
        image=(ImageView) findViewById(R.id.imageView);


        nidRB=findViewById(R.id.rbNID);
        passportRB=findViewById(R.id.rbPassport);
        drivingLicenceRB=findViewById(R.id.rbDrivingLicense);

        Intent i= getIntent();
        String key=i.getStringExtra("email");
        System.out.println(key);
        initializeFormWithExistingData(key);
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EditDeliveryManProfile.this,DeliveryManFeed.class);
                i.putExtra("DelEmail",key);
                startActivity(i);
            }
        });
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventData(key);

            }
        });
        findViewById(R.id.btnImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();

            }
        });

    }
    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    image.setImageURI(selectedImageUri);
                }
            }
        }
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
        String documentNo = documentNoTF.getText().toString();
        String address = addressTF.getText().toString();
        String uri=" ";
        if(selectedImageUri != null ){
            uri=encodeImage(image);

        }



        String radioButtonValue="";
        if(nidRB.isChecked()){
            radioButtonValue="NID";
        }
        else if(passportRB.isChecked()){
            radioButtonValue="Passport";
        }
        else if(drivingLicenceRB.isChecked()){
            radioButtonValue="Driving License";
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

        if(phone.isEmpty()){
            errorMSG +="Contact no: can not be empty.\n";
        }
        if(radioButtonValue.equals("")){
            errorMSG +="Please select any User type.\n";
        }
        if(documentNo.isEmpty()){
            errorMSG +="Document no: can not be empty.\n";
        }
        if(address.isEmpty()){
            errorMSG +="address can not be empty.\n";
        }



        if(errorMSG.isEmpty()){
            if(rememberVal!=null){
                String user = sharedPreferences.getString("email", null);
                String pswrd = sharedPreferences.getString("password", null);
                if(user.equals(email)){
                    prefsEditor.putString("password",pswrd);
                }
                prefsEditor.commit();
            }
            String value=email+";"+name+";"+pass1+";"+phone+";"+"DELIVERYMAN"+";"+radioButtonValue+";"+documentNo+";"+address+";"+uri;
            String key= email;

            System.out.print("\nkey: "+key);
            System.out.print("\nvalue: "+value);

            Util.getInstance().setKeyValue(EditDeliveryManProfile.this,key,value);
            showDialog("Profile successfully updated!!","Info!","OK",false,email);
        }
        else{
            showDialog(errorMSG,"Error in profile information!!","Back",true,email);
        }


    }
    public String encodeImage(ImageView myImageView){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitmapDrawable drawable = (BitmapDrawable) myImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap = scaleDown(bitmap, 160,120 , true); // if you want to resize the image with max height/width 120dp
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }
    public Bitmap scaleDown(Bitmap realImage, float maxImageWidth,float maxImageHeight, boolean filter) {
        float ratioWidth = (float) maxImageWidth / realImage.getWidth();
        float ratioHeight = (float) maxImageHeight / realImage.getHeight();

        //float ratio = Math.min( (float) maxImageSize / realImage.getWidth(), (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratioWidth * realImage.getWidth());
        int height = Math.round((float) ratioHeight * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }
    public void showDecodedImage(ImageView myImageView, String encodedImageString){
        byte[] decodedString = Base64.decode(encodedImageString, Base64.DEFAULT);
        Bitmap myImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        myImageView.setImageBitmap(myImage);
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
                            Intent i = new Intent(EditDeliveryManProfile.this, DeliveryManFeed.class);
                            i.putExtra("DelEmail",email);
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
            String documentNo = "";
            String documentType = "";
            String uri = null;
            if (fieldValues.length > 5) {
                documentType = fieldValues[5];
                documentNo = fieldValues[6];
                address = fieldValues[7];
                if(fieldValues.length>8 && !fieldValues[8].equals(" ")){
                    System.out.println(fieldValues[8]);
                    uri=fieldValues[8];
                    showDecodedImage(image,uri);

                }

            }

            emailTV.setText(eventKey);
            nameTF.setText(name);
            pass1TF.setText(pass1);
            pass2TF.setText(pass1);
            addressTF.setText(address);
            phoneTF.setText(phone);
            documentNoTF.setText(documentNo);




            if (documentType.equals("NID")) {
                nidRB.setChecked(true);
            } else if (documentType.equals("Passport")) {
                passportRB.setChecked(true);
            } else if (documentType.equals("Driving License")) {
                drivingLicenceRB.setChecked(true);
            }
        }
        else {
            System.out.println("Data not found");
        }
    }



}

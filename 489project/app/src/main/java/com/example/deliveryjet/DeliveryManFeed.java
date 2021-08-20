package com.example.deliveryjet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DeliveryManFeed extends Activity {
    private ListView lvServices;
    private ArrayList<Order> orders;
    private CustomServiceAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryman_feed);

        lvServices= findViewById(R.id.lvServices);
        Intent i= getIntent();
        String key=i.getStringExtra("DelEmail");
        System.out.println(key);

        initializeCustomServiceList(key);

        findViewById(R.id.btnLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DeliveryManFeed.this,LoginActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.tabDeliverymanProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DeliveryManFeed.this,EditDeliveryManProfile.class);
                i.putExtra("email",key);
                startActivity(i);
            }
        });


    }
    private void initializeCustomServiceList(String delEmail){

        KeyValueDB db=new KeyValueDB(this);
        Cursor orderRows=db.execute("SELECT * FROM key_value_pairs");
        if(orderRows.getCount()==0){

            return;
        }


        orders=new ArrayList<>();



        while(orderRows.moveToNext()){
            String key= orderRows.getString(0);
            String orderData=orderRows.getString(1);


            if (key.contains(";")){
                System.out.println(key);
                String[] keyValues=key.split(";");
                if (!keyValues[1].equals("null")) {
                    System.out.println(keyValues[1]);
                    String[] fieldValues = orderData.split(";");
                    String orderId = fieldValues[0];
                    String email = fieldValues[1];
                    String details = fieldValues[2];
                    String weight = fieldValues[3];
                    String customerName = fieldValues[4];
                    String phone = fieldValues[5];
                    String address = fieldValues[6];
                    String date = fieldValues[7];
                    String status = fieldValues[8];
                    String code = fieldValues[9];
                    if(status.equals("Assigned")){
                        Order o= new Order(key, orderId, email, details, weight, customerName, phone, address, date,status,code);
                        orders.add(o);
                    }



                }



            }

        }
        db.close();
        adapter=new CustomServiceAdapter(this,orders);
        lvServices.setAdapter(adapter);

        lvServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                System.out.println(position);

                Intent i = new Intent(DeliveryManFeed.this, OrderDetails.class);
                i.putExtra("ORDERID", orders.get(position).key);
                i.putExtra("DelEmail", delEmail);
                startActivity(i);
            }
        });



    }

}

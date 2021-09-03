package com.DeliveryJetApp.deliveryjet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class CompanyFeed extends Activity {

    private ListView lvOrders;
    private ArrayList<Order> orders;
    private CustomOrderAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_company_feed);
        lvOrders= findViewById(R.id.lvOrders);
        Intent i= getIntent();
        String key=i.getStringExtra("ComEmail");
        initializeCustomEventList(key);




        findViewById(R.id.btnLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CompanyFeed.this,LoginActivity.class);
                startActivity(i);
                CompanyFeed.this.finish();
            }
        });

        findViewById(R.id.tabCompanyProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CompanyFeed.this,EditCompanyProfile.class);
                i.putExtra("email",key);
                startActivity(i);
                CompanyFeed.this.finish();

            }
        });
        findViewById(R.id.tabCreateOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CompanyFeed.this,CreateOrder.class);
                i.putExtra("email",key);
                startActivity(i);

            }
        });


    }
    private void initializeCustomEventList(String ComEmail){

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

                String[] keyValues=key.split(";");
                if(keyValues[1].equals(ComEmail)) {

                    String[] fieldValues = orderData.split(";");
                    String orderId = fieldValues[0];
                    String email = fieldValues[1];
                    String details = fieldValues[2];
                    String weight = fieldValues[3];
                    String customerName = fieldValues[4];
                    String customerEmail = fieldValues[5];
                    String phone = fieldValues[6];
                    String address = fieldValues[7];
                    String date = fieldValues[8];
                    String status = fieldValues[9];
                    String code = fieldValues[10];

                    Order o= new Order(key, orderId, email, details, weight, customerName,customerEmail, phone, address, date,status,code);
                    orders.add(o);
                }

            }

        }
        db.close();
        adapter=new CustomOrderAdapter(this,orders);
        lvOrders.setAdapter(adapter);

        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                System.out.println(position);
                if(orders.get(position).status.equals("Unassigned")){
                    Intent i = new Intent(CompanyFeed.this, CreateOrder.class);
                    i.putExtra("ORDERID", orders.get(position).key);
                    i.putExtra("email",ComEmail);
                    startActivity(i);
                }else{
                        Intent i = new Intent(CompanyFeed.this, OrderDetailsCompany.class);
                        i.putExtra("ORDERID", orders.get(position).key);
                        i.putExtra("email",ComEmail);
                        startActivity(i);
                }


            }
        });
        lvOrders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String message="Do you want to delete order -"+orders.get(position).orderId+" ?";
                showDialog(message,"Delete Order",position,ComEmail);
                return true;
            }
        });


    }
    private void showDialog(String message,String title,int position,String ComEmail){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file


        //Setting message manually and performing action on button click
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Util.getInstance().deleteByKey(CompanyFeed.this,orders.get(position).key);
                        initializeCustomEventList(ComEmail);
                        adapter.notifyDataSetChanged();
                        //lvEvents.notifyAll();
                        dialog.cancel();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();

        alert.show();
    }

}

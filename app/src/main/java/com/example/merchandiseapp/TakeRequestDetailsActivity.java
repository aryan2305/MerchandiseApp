package com.example.merchandiseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.merchandiseapp.Prevalent.Prevalent;
import com.example.merchandiseapp.Prevalent.Prevalent_Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TakeRequestDetailsActivity extends AppCompatActivity
{
    private ArrayList<String> orderid_list;
    private ArrayList<String> group_list;
    private ArrayList<String> product_list;
    private EditText PhoneNumber, Address, EmailID;
    private Button Pay_Now, Pay_Later;
    private String orderID, groupName, productID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_request_details);

        PhoneNumber = (EditText) findViewById(R.id.Request_Phone_Number);
        Address = (EditText) findViewById(R.id.Request_Address);
        EmailID = (EditText) findViewById(R.id.Request_Email);
        Pay_Now = (Button) findViewById(R.id.Btn_Pay_Now);
        Pay_Later = (Button) findViewById(R.id.Btn_Pay_Later);

        orderID = getIntent().getStringExtra("orderID");
        groupName = getIntent().getStringExtra("group_name");
        productID = getIntent().getStringExtra("product_id");

        orderid_list = new ArrayList<>();
        group_list = new ArrayList<>();
        product_list = new ArrayList<>();

        updateWallet();
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Requests_Temp").child(Prevalent.currentOnlineUser).child(orderID);

        requestRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Requests requests =  dataSnapshot.getValue(Requests.class);
                EmailID.setText(requests.getEmail());
                Address.setText(requests.getAddress());
                PhoneNumber.setText(requests.getContact());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


        Pay_Now.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Pay_Now_Function2();
                Pay_Now_Function();
            }
        });

        Pay_Later.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Pay_Later_Function2();
                Pay_Later_Function();
            }
        });
    }

    private void Pay_Now_Function()
    {
        if(PhoneNumber.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Please Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
            return;
        }


        if(Address.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Address Field can't be Empty. Please Enter Delivery Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(EmailID.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Group").child(groupName).child("Requests").child(productID).child("Requests").child(orderID);

        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Contact", PhoneNumber.getText().toString());
        requestMap.put("Address", Address.getText().toString());
        requestMap.put("Email",EmailID.getText().toString());
        //requestMap.put("IsPaid", "true");

        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(TakeRequestDetailsActivity.this, PaymentActivity.class);
                orderid_list.add(orderID);
                group_list.add(groupName);
                product_list.add(productID);
                intent.putExtra("orderid_list", orderid_list);
                intent.putExtra("group_list", group_list);
                intent.putExtra("product_list", product_list);
                startActivity(intent);
                //Toast.makeText(TakeRequestDetailsActivity.this, "Successfully Paid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Pay_Later_Function()
    {
        if(PhoneNumber.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Please Enter Valid Contact Number", Toast.LENGTH_SHORT).show();
            return;
        }


        if(Address.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Address Field can't be Empty. Please Enter Delivery Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(EmailID.getText().toString().trim().length() == 0)
        {
            Toast.makeText(TakeRequestDetailsActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Group").child(groupName).child("Requests").child(productID).child("Requests").child(orderID);

        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Contact", PhoneNumber.getText().toString());
        requestMap.put("Address", Address.getText().toString());
        requestMap.put("Email",EmailID.getText().toString());
        requestMap.put("IsPaid", "false");

        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                Intent intent = new Intent(TakeRequestDetailsActivity.this, HomeActivity.class);
                Prevalent_Intent.setIntent(intent);
                intent.putExtra("orderType", Prevalent.currentOrderType);
                startActivity(intent);
            }
        });

    }

    private void Pay_Now_Function2()
    {
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Requests_Temp").child(Prevalent.currentOnlineUser).child(orderID);
        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Contact", PhoneNumber.getText().toString());
        requestMap.put("Address", Address.getText().toString());
        requestMap.put("Email",EmailID.getText().toString());
        //requestMap.put("IsPaid", "true");

        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

            }
        });
    }

    private void Pay_Later_Function2()
    {
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("Requests_Temp").child(Prevalent.currentOnlineUser).child(orderID);
        final HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put("Contact", PhoneNumber.getText().toString());
        requestMap.put("Address", Address.getText().toString());
        requestMap.put("Email",EmailID.getText().toString());
        requestMap.put("IsPaid", "false");

        requestRef.updateChildren(requestMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

            }
        });

    }

    public void updateWallet()
    {
        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser);
        UserData.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Wallet_Money").exists())
                {
                    Prevalent.currentWalletMoney = dataSnapshot.child("Wallet_Money").getValue().toString();
                }

                else
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Wallet_Money", "0");
                    Prevalent.currentWalletMoney = "0";

                    UserData.updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}

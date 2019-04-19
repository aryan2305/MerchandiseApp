package com.example.merchandiseapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EditMerchandise extends AppCompatActivity {
    private ArrayList<String> AccessGroups = new ArrayList<String>();
    private ArrayList<String> size = new ArrayList<String>();
    private ArrayList<String> qty = new ArrayList<String>();
    private ArrayList<String> listSQ = new ArrayList<String>();

    private Button add_accessgroup;
    private Button addSQ;
    private Button UpdateMerchandise;

    private EditText access_editText;
    private EditText size_edt;
    private EditText qty_edt;
    private EditText mat;
    private EditText price_edt;


    private TextView txtView_category;
    private TextView txtView_PID;



    private android.widget.ListView accessGroupListView;
    private ListView sizeQtyListView;


    private String GroupName ;
    private String Category ;
    private ArrayList<String> Image;
    private String Material;
    private String PID;
    private String Price;
    private String OrderType;

    ArrayAdapter adapter;
    ArrayAdapter adapterSize_qty;

    DatabaseReference myRef ;
    DatabaseReference myRef2 ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_merchandise);



//        Group Name, PID and Category to be passed by the user
            Intent intent  = getIntent();


            GroupName =  intent.getStringExtra("GroupName");
            PID =  intent.getStringExtra("PID");
            Category =  intent.getStringExtra("Category");




        txtView_category = (TextView) findViewById(R.id.textView_Category);
        txtView_category.setText(txtView_category.getText().toString()+" "+ Category);

        txtView_PID = (TextView) findViewById(R.id.textView_PID);
        txtView_PID.setText(txtView_PID.getText().toString()+" "+ PID);

        add_accessgroup = (Button) findViewById(R.id.button_add_accessgroup);
        addSQ = (Button) findViewById(R.id.button_size_qty);
        UpdateMerchandise = (Button) findViewById(R.id.button_submit);

        access_editText = (EditText) findViewById(R.id.editText_accessgroup);
        size_edt = (EditText) findViewById(R.id.editText_size);
        qty_edt = (EditText) findViewById(R.id.editText_qty);

        mat = (EditText) findViewById(R.id.EditText_Material);
        price_edt = (EditText) findViewById(R.id.EditText_Price);

        accessGroupListView = (ListView) findViewById(R.id.listView_accessgroup);
        sizeQtyListView =(ListView) findViewById(R.id.listView_size_qty);




        myRef = FirebaseDatabase.getInstance().getReference().child("Group").child("CSEA").child("Merchandise");

        myRef.child(Category).child(PID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderType = dataSnapshot.child("OrderType").getValue().toString();
                Price = dataSnapshot.child("Price").getValue().toString();
                Material = dataSnapshot.child("Material").getValue().toString();
                size = (ArrayList<String>) dataSnapshot.child("Size").getValue();
                qty = (ArrayList<String>) dataSnapshot.child("Quantity").getValue();
                AccessGroups = (ArrayList<String>) dataSnapshot.child("Size").getValue();
                Image = (ArrayList<String>) dataSnapshot.child("Image").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        for (int i =0;i<qty.size();i++)
        {
            listSQ.add("Size=" + size.get(i) + ":Qty=" + qty.get(i));
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,AccessGroups);
        adapterSize_qty  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listSQ);




        AccessGroups.add(GroupName);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,AccessGroups);
        adapterSize_qty  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listSQ);

        add_accessgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(access_editText.getText().toString()=="")
                {
                    return;
                }
                AccessGroups.add(access_editText.getText().toString());
                adapter.notifyDataSetChanged();
                access_editText.setText("");

            }
        });
        accessGroupListView.setAdapter(adapter);

        addSQ.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                if (size_edt.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter the size", Toast.LENGTH_LONG);
                }
                else if (qty_edt.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter the quantity", Toast.LENGTH_LONG);
                }
                else {
                    listSQ.add("Size=" + size_edt.getText().toString() + ":Qty=" + qty_edt.getText().toString());
                    size.add(size_edt.getText().toString());
                    qty.add(qty_edt.getText().toString());
                    adapterSize_qty.notifyDataSetChanged();
                    size_edt.setText("");
                    qty_edt.setText("");
                }

            }
        });
        sizeQtyListView.setAdapter(adapterSize_qty);


        UpdateMerchandise.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                Image = new ArrayList<>();
                Material = mat.getText().toString();
                Price = price_edt.getText().toString();
                System.out.println(GroupName+Category+Image+Material+PID+Price);

                if(listSQ.size()==0)
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Quantity and Size",Toast.LENGTH_LONG).show();
                }
                else if(Price.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter a Valid Price",Toast.LENGTH_LONG).show();
                }
                else {



                    myRef = FirebaseDatabase.getInstance().getReference().child("Group").child("CSEA").child("Merchandise");
                    myRef2 = FirebaseDatabase.getInstance().getReference().child("Merchandise");
                    final Query queries = myRef2.child(Category).orderByKey().equalTo(PID);

                    queries.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists()) {

                                Toast.makeText(getApplicationContext(),"Enter Valid Product ID. This Id Already Exist",Toast.LENGTH_LONG).show();

                            } else {
                                //Toast.makeText(CartActivity.this,"no data exists",Toast.LENGTH_SHORT).show();
                                Merchandise merchandise = new Merchandise(GroupName, Category, Image, Material, PID, Price, qty, size, AccessGroups, OrderType, "true");
                                HashMap<String, Object> merchandiseValues = merchandise.toMap();

                                HashMap<String, Object> childUpdates = new HashMap<>();

                                childUpdates.put(Category + "/" + PID, merchandiseValues);


                                myRef.updateChildren(childUpdates);
                                myRef2.updateChildren(childUpdates);

                                access_editText.setText("");
                                size_edt.setText("");
                                qty_edt.setText("");
                                mat.setText("");
                                price_edt.setText("");
                                Toast.makeText(getApplicationContext(),"Merchandise Added Successfully",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });




                }

            }
        });
    }


}

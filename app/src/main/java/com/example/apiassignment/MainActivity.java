package com.example.apiassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apiassignment.Model.Address;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etFilter;
    Button btnNew;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference fbRef;
    ArrayList<Address> addressList = new ArrayList<>();
    Address_Adapter address_adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        fbRef = firebaseDatabase.getReference();
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        btnNew = findViewById(R.id.btn_main_add);
        btnNew.setOnClickListener(this);
        getAddresses();

        etFilter = (EditText) findViewById(R.id.et_filter);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterArray(s.toString().toLowerCase());

            }
        });

    }

    private void filterArray(String filter) {
        ArrayList<Address> filteredList = new ArrayList<>();
        for (Address address : addressList) {
            if (address.getName().toLowerCase().contains(filter)
                    || address.getStreet().toLowerCase().contains(filter)
                    || address.getCity().toLowerCase().contains(filter)
                    || address.getSuburb().toLowerCase().contains(filter)){
                filteredList.add(address);
            }
        }
        address_adapter = new Address_Adapter(filteredList);
        rv.setAdapter(address_adapter);
        address_adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == btnNew.getId()) {
            startActivity(new Intent(this, AddAddress.class));
        }
    }

    private void getAddresses() {
        fbRef.child("Address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Address address = ds.getValue(Address.class);
                    addressList.add(address);
                }
                address_adapter = new Address_Adapter(addressList);

                rv.setAdapter(address_adapter);
                address_adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Could not connect to database", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
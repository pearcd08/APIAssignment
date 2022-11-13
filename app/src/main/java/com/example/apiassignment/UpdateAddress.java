package com.example.apiassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apiassignment.Model.Address;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateAddress extends AppCompatActivity implements View.OnClickListener {
    EditText etName, etStreet, etSuburb, etCity;
    Button btnUpdate, btnDelete, btnBack;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference fbRef;
    String addressID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            addressID = extras.getString("addressid");
        }


        etName = findViewById(R.id.et_updatefullName);
        etStreet = findViewById(R.id.et_updatestreetAddress);
        etSuburb = findViewById(R.id.et_updatesuburb);
        etCity = findViewById(R.id.et_updatecity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        fbRef = firebaseDatabase.getReference();

        btnUpdate = findViewById(R.id.btn_updateaddress);
        btnDelete = findViewById(R.id.btn_deleteaddress);
        btnBack = findViewById(R.id.btn_updateback);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        loadAddress();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnUpdate.getId()) {
            updateAddress();
            Toast.makeText(this, "UPDATE", Toast.LENGTH_SHORT).show();

        }
        if (v.getId() == btnDelete.getId()) {
            deleteAddress();
            Toast.makeText(this, "DELETE", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == btnBack.getId()) {
            startActivity(new Intent(this, MainActivity.class));
        }

    }


    private void updateAddress() {
        String name = etName.getText().toString();
        String street = etStreet.getText().toString();
        String suburb = etSuburb.getText().toString();
        String city = etCity.getText().toString();

        fbRef.child("Address").child(addressID).child("name").setValue(name);
        fbRef.child("Address").child(addressID).child("street").setValue(street);
        fbRef.child("Address").child(addressID).child("suburb").setValue(suburb);
        fbRef.child("Address").child(addressID).child("city").setValue(city);

        Toast.makeText(UpdateAddress.this, "Updated!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UpdateAddress.this, MainActivity.class));


    }

    private void deleteAddress() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference fbRef = firebaseDatabase.getReference();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm");
        builder.setMessage("Delete Address?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fbRef.child("Address").child(addressID).removeValue();
                        Toast.makeText(UpdateAddress.this, "Deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateAddress.this, MainActivity.class));

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void loadAddress() {
        fbRef.child("Address").child(addressID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Address address = snapshot.getValue(Address.class);
                etName.setText(address.getName());
                etStreet.setText(address.getStreet());
                etSuburb.setText(address.getSuburb());
                etCity.setText(address.getCity());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}
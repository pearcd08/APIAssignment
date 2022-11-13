package com.example.apiassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;
import com.example.apiassignment.Model.Address;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
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

public class AddAddress extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etStreet, etSuburb, etCity;
    Button btnSave, btnBack;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference fbRef;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private String apiKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);


        requestQueue = getRequestQueue(getApplicationContext());

        apiKey = "AIzaSyDGYwtmPp1374_zw8rVE31FNMMJgXPYFUU";


        firebaseDatabase = FirebaseDatabase.getInstance();
        fbRef = firebaseDatabase.getReference();

        etName = findViewById(R.id.et_fullName);
        etStreet = findViewById(R.id.et_streetAddress);
        etSuburb = findViewById(R.id.et_suburb);
        etCity = findViewById(R.id.et_city);

        etName.setVisibility(View.INVISIBLE);
        etStreet.setVisibility(View.INVISIBLE);
        etSuburb.setVisibility(View.INVISIBLE);
        etCity.setVisibility(View.INVISIBLE);

        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        startPlaces();

    }

    private RequestQueue getRequestQueue(Context applicationContext) {
        Log.d("JSON", " getRequestQueue ");
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }

        return requestQueue;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnSave.getId()) {
            saveAddress();
        }
        if (v.getId() == btnBack.getId()){
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    private void saveAddress() {
        String name = etName.getText().toString();
        String street = etStreet.getText().toString();
        String suburb = etSuburb.getText().toString();
        String city = etCity.getText().toString();

        String key = fbRef.push().getKey();
        Address address = new Address(key, name, street, suburb, city);
        fbRef.child("Address").child(key).setValue(address);
        Toast.makeText(this, name + " saved!", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm");
        builder.setMessage("Add Another Address?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etName.setVisibility(View.INVISIBLE);
                        etStreet.setVisibility(View.INVISIBLE);
                        etSuburb.setVisibility(View.INVISIBLE);
                        etCity.setVisibility(View.INVISIBLE);
                        etName.setText("");
                        etStreet.setText("");
                        etSuburb.setText("");
                        etCity.setText("");

                    }
                });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(AddAddress.this, MainActivity.class));

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();





    }

    private void startPlaces() {
        //Initialise places
        Places.initialize(getApplicationContext(), apiKey);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID));

        //Restrict searches to New Zealand
        autocompleteFragment.setCountry("NZ");

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                etName.setVisibility(View.VISIBLE);
                etStreet.setVisibility(View.VISIBLE);
                etSuburb.setVisibility(View.VISIBLE);
                etCity.setVisibility(View.VISIBLE);
                String placeID = place.getId();
                volleyAPICall(placeID);

            }
        });

    }

    private void volleyAPICall(String placeID) {

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key=" + apiKey;
        System.out.println(url);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String streetNo = "";
                    String streetName = "";
                    String suburb = "";
                    String city = "";

                    System.out.println(response + "");
                    JSONObject data = response.getJSONObject("result");
                    JSONArray components = data.getJSONArray("address_components");
                    for (int i = 0; i < components.length(); i++) {
                        JSONObject component = components.getJSONObject(i);
                        JSONArray types = component.getJSONArray("types");
                        for (int j = 0; j < types.length(); j++) {
                            if (types.get(j).equals("street_number")) {
                                streetNo = component.getString("long_name");

                            }
                            if (types.get(j).equals("route")) {
                                streetName = component.getString("long_name");

                            }
                            if (types.get(j).equals("sublocality") || types.get(j).equals("sublocality_level_1")
                                    || types.get(j).equals("neighborhood")){
                                suburb = component.getString("long_name");

                            }
                            if (types.get(j).equals("locality") || types.get(j).equals("postal_town")) {
                                city = component.getString("long_name");

                            }


                        }
                        etStreet.setText(streetNo+" "+streetName);
                        etSuburb.setText(suburb);
                        etCity.setText(city);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);

            }
        });
        requestQueue.add(jsonObjectRequest);


    }
}
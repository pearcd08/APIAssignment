package com.example.apiassignment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiassignment.Model.Address;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Address_Adapter extends RecyclerView.Adapter<Address_Holder> {
    private ArrayList<Address> mAddressArray;

    public Address_Adapter(ArrayList<Address> addressArray){
        mAddressArray = addressArray;

    }
    @NonNull
    @Override
    public Address_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new Address_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Address_Holder holder, int position) {

        String addressID = mAddressArray.get(position).getAddressID().toString();
        holder.tvName.setText("Name: "+mAddressArray.get(position).getName());
        holder.tvStreet.setText("Street: "+mAddressArray.get(position).getStreet());
        holder.tvSuburb.setText("Suburb: "+mAddressArray.get(position).getSuburb());
        holder.tvCity.setText("City: "+mAddressArray.get(position).getCity());


        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UpdateAddress.class);
                i.putExtra("addressid", addressID);
                v.getContext().startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
      return mAddressArray.size();
    }
}

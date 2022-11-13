package com.example.apiassignment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Address_Holder extends RecyclerView.ViewHolder {
    TextView tvName, tvStreet, tvSuburb, tvCity;
    Button btnDelete, btnUpdate;

    public Address_Holder(@NonNull View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tv_name);
        tvStreet = itemView.findViewById(R.id.tv_street);
        tvSuburb = itemView.findViewById(R.id.tv_suburb);
        tvCity = itemView.findViewById(R.id.tv_city);

        btnUpdate = itemView.findViewById(R.id.btn_update);

    }
}

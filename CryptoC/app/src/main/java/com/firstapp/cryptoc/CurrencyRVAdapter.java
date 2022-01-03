package com.firstapp.cryptoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder>
{
    private static DecimalFormat df2=new DecimalFormat("#.##");
    private ArrayList<CurrencyRvModal> currencyRvModals;
    private Context context;

    public CurrencyRVAdapter(ArrayList<CurrencyRvModal> currencyRvModalArrayList, Context context) {
        this.currencyRvModals = currencyRvModalArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<CurrencyRvModal> filteredList){
        currencyRvModals=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.currency_rv_item,parent,false);
        return new CurrencyRVAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.ViewHolder holder, int position) {
        CurrencyRvModal currencyRvModal=currencyRvModals.get(position);
        holder.currencyNameTv.setText(currencyRvModal.getName());
        holder.rateTV.setText("$ " + df2.format(currencyRvModal.getPrice()));
        holder.symbolTV.setText(currencyRvModal.getSymbol());

    }

    @Override
    public int getItemCount() {
        return currencyRvModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView currencyNameTv,symbolTV,rateTV;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            currencyNameTv=itemView.findViewById(R.id.idcurrencyName);
            symbolTV=itemView.findViewById(R.id.idTVSymbol);
            rateTV=itemView.findViewById(R.id.idTVCurrencyRate);
        }
    }
}

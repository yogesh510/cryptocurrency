package com.firstapp.cryptoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity
{
    private EditText searchEdt;
    private RecyclerView currenciesRV;
    private ProgressBar loadingPB;
    private ArrayList<CurrencyRvModal> currencyRVModalArrayList;
    private CurrencyRVAdapter currencyRVAdapter;
    private ProgressBar LoadingPB;

    private FirebaseDatabase firebaseDatabase;
    private Button logout;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEdt =findViewById(R.id.EdtSearch);
        //initializing all our variables and array list.
        currenciesRV =findViewById(R.id.idRvCurrencies);
        loadingPB =findViewById(R.id.idPBLoading);

        firebaseDatabase=FirebaseDatabase.getInstance();

        currencyRVModalArrayList=new ArrayList<>();
        currencyRVAdapter=new CurrencyRVAdapter(currencyRVModalArrayList,this);
        currenciesRV.setLayoutManager(new LinearLayoutManager(this));
        currenciesRV.setAdapter(currencyRVAdapter);
        getCurrencyDate();

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrencies(s.toString());
            }
        });





        logout=(Button) findViewById(R.id.B_logout);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                finish();
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });
    }
    private void filterCurrencies(String filter){
        ArrayList<CurrencyRvModal> filteredList=new ArrayList<>();
        for(CurrencyRvModal item:currencyRVModalArrayList){
            if(item.getName().toLowerCase().contains(filter.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No currency found", Toast.LENGTH_SHORT).show();
        }
        else{
            currencyRVAdapter.filterList(filteredList);
        }
    }
    private void getCurrencyDate()
    {
        loadingPB.setVisibility(View.VISIBLE);
        String url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()

        {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                try {
                    JSONArray dataArray=response.getJSONArray("data");
                    for(int i=0;i<dataArray.length();i++)
                    {
                        JSONObject dataObj=dataArray.getJSONObject(i);
                        String name=dataObj.getString("name");
                        String symbol=dataObj.getString("symbol");
                        JSONObject quote=dataObj.getJSONObject("quote");
                        JSONObject USD=quote.getJSONObject("USD");
                        double price=USD.getDouble("price");
                        currencyRVModalArrayList.add(new CurrencyRvModal(name,symbol,price));
                    }
                    currencyRVAdapter.notifyDataSetChanged();

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Fail to extract JSON data..", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Fail to get  the data.", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY", "4e5d6103-c96a-4bb7-93ea-f9ca5cde0137");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    public ProgressBar getLoadingPB() {
        return LoadingPB;
    }

}



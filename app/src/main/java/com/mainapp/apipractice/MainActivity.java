package com.mainapp.apipractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText currencyToBeConverted;
    EditText currencyConverted;
    Spinner convertToDropdown;
    Spinner convertFromDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currencyConverted = findViewById(R.id.currency_converted);
        currencyToBeConverted = findViewById(R.id.currency_to_be_converted);
        convertToDropdown = findViewById(R.id.to);
        convertFromDropdown = findViewById(R.id.from);
        button = findViewById(R.id.button);

        String[] dropDownList = {"USD", "INR","EUR","NZD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, dropDownList);
        convertToDropdown.setAdapter(adapter);
        convertFromDropdown.setAdapter(adapter);

        button.setOnClickListener(v -> {
            RetrofitInterface retrofitInterface = RetrofitModel.getRetrofitInstance().create(RetrofitInterface.class);
            Call<JsonObject> call = retrofitInterface.getExchangeCurrency(convertFromDropdown.getSelectedItem().toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    JsonObject res = response.body();
                    assert res != null;
                    JsonObject rates = res.getAsJsonObject("conversion_rates");
                    double currency = Double.parseDouble(currencyToBeConverted.getText().toString());
                    double multiplier = Double.parseDouble(rates.get(convertToDropdown.getSelectedItem().toString()).toString());
                    double result = currency * multiplier;
                    currencyConverted.setText(String.valueOf(result));
                }
                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) { }
            });
        });
    }
}
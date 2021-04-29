package com.example.android.intentsplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.intentsplayground.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
     private int qty = 0;
     private int maxVal,minVal;
    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        eventHandler();
        getInitialCount();
    }


    private void getInitialCount() {
        Bundle bundle = getIntent().getExtras();
        qty = getIntent().getIntExtra(Constants.INITIAL_COUNT_KEY,0);
        minVal = bundle.getInt(Constants.INITIAL_MIN_VALUE,Integer.MIN_VALUE);
        maxVal = bundle.getInt(Constants.INITIAL_MAX_VALUE,Integer.MAX_VALUE);
        b.qty.setText(String.valueOf(qty));

        if (qty != 0){
            b.button2.setVisibility(View.VISIBLE);
        }
    }
    //handling events in application.
    private void eventHandler() {
        b.decBtn.setOnClickListener(v -> decqty());

        b.incBtn.setOnClickListener(v -> incqty());
    }

    //To increase the quantity
    private void incqty() {
        b.qty.setText( "" + qty++);
    }

    //To decrease the quantity
    private void decqty() {
        b.qty.setText("" + qty--);
    }
    //To send result upon pressing of the button
    public void sendBack(View view) {
        if (qty >= minVal && qty <= maxVal){
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_VALUE,qty);
            setResult(RESULT_OK,intent);
            finish();
        }
        else
            Toast.makeText(this,"Not in range",Toast.LENGTH_SHORT).show();
    }
}
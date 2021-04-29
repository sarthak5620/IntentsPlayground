package com.example.android.intentsplayground;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.android.intentsplayground.databinding.ActivityIntentsPlaygroundBinding;

public class IntentsPlayground extends AppCompatActivity {
    private static final int REQUEST_CODE =0;
    ActivityIntentsPlaygroundBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Intents Playground");
        setupHideErrorForEditText();

    }

    //basic Layout methods
    private void setupHideErrorForEditText() {
       TextWatcher myTextWatcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
            b.data.getEditText().addTextChangedListener(myTextWatcher);
            b.data2.getEditText().addTextChangedListener(myTextWatcher);
    }
//Explicit intent Demo
    public void openMainActivity(View view) {
        Intent intent1 = new Intent(this,MainActivity.class);
        startActivity(intent1);
    }
//Implicit Intent Demo
    public void sendImplicitIntent(View view) {
        //Validate data input
        String input = b.data.getEditText().getText().toString().trim();
        if (input.isEmpty()) {
            b.data.setError("Please Enter Something!");
            return;
        }
        //Get the respective id of the button
        int type = b.radioGroup.getCheckedRadioButtonId();
        if (type==b.openWebPageBtn.getId())
            openWebPage(input);
        else if (type==b.DialNumberButton.getId())
            dialNumber(input);
        else if (type==b.ShareText.getId())
            shareText(input);
        else
            Toast.makeText(this,"Please select an intent type",Toast.LENGTH_SHORT).show();
    }
//Implicit intent methods
    private void shareText(String text) {
        Intent intent = new Intent(); intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(Intent.createChooser(intent, "Share text via"));
    }

    private void dialNumber(String number) {
        if (!number.matches("^\\d{10}$")) {
            b.data.setError("Please enter valid number ");
            return;
        }

        Uri uri = Uri.parse("tel" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);

        hideError();
    }

    private void openWebPage(String url) {
        if (!url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
            b.data.setError("Invalid URL!");
            return;
        }

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

        hideError();
    }
    //Get the result of other activity in the first activity
    @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            int count = data.getIntExtra(Constants.FINAL_VALUE,Integer.MIN_VALUE);
            b.result.setText(" Final Count is: " + count);
            b.result.setVisibility(View.VISIBLE);
        }
    }

    public void sendcounterBtn(View view) {
        String input = b.data2.getEditText().getText().toString().trim();
        if (input.isEmpty()) {
            b.data2.setError("Please Enter Something!");
            return;
        }
        //GetCount
        int initialCount = Integer.parseInt(input);
        Intent intent = new Intent(this,MainActivity.class);
     //   intent.putExtra(Constants.INITIAL_COUNT_KEY,initialCount);
        //bundle to keep all kinds of data at single place
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.INITIAL_COUNT_KEY,initialCount);
        bundle.putInt(Constants.INITIAL_MIN_VALUE,-100);
        bundle.putInt(Constants.INITIAL_MAX_VALUE, 100);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
    }
    //Utility Methods
    private void hideError(){
        b.data.setError(null);
    }

}
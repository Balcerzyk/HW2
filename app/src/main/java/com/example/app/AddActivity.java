package com.example.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setButtonClickListener();
    }

    public void setButtonClickListener(){
        final Button add = (Button) findViewById(R.id.add);

        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = findViewById(R.id.input_title);
                TextView author = findViewById(R.id.input_author);
                TextView date = findViewById(R.id.input_date);

                String titleString = title.getText().toString();
                String authorString = author.getText().toString();
                String dateString = date.getText().toString();

                if(!TextUtils.isEmpty(titleString) && !TextUtils.isEmpty(authorString) && !TextUtils.isEmpty(dateString)){
                    finishActivity(titleString, authorString, dateString);
                }
                else{
                    if(TextUtils.isEmpty(titleString)) title.setError("Podaj tytuł!");
                    if(TextUtils.isEmpty(authorString)) author.setError("Podaj autora!");
                    if(TextUtils.isEmpty(dateString)) date.setError("Podaj date publikacji!");
                }
            }
        };

        add.setOnClickListener(myClickListener);
    }

    void finishActivity(String title, String author, String date){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("titleRet", title);
        returnIntent.putExtra("authorRet", author);
        returnIntent.putExtra("dateRet", date);
        setResult(AddActivity.RESULT_OK,returnIntent);
        finish();
    }

}

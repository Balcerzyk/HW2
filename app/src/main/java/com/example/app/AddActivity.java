package com.example.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class AddActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    String picPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File image = null;
                try {
                    image = makeFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(this, getString(R.string.myFileprovider), image);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AddActivity.RESULT_OK) {
            Log.v("2", "camera ok");
        }
        else if(resultCode == AddActivity.RESULT_CANCELED) {
            Intent returnIntent = new Intent();
            setResult(AddActivity.RESULT_CANCELED,returnIntent);
            finish();
        }
    }

    void finishActivity(String title, String author, String date){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("titleRet", title);
        returnIntent.putExtra("authorRet", author);
        returnIntent.putExtra("dateRet", date);
        if(picPath != null){
            returnIntent.putExtra("picPath", picPath);
        }
        setResult(AddActivity.RESULT_OK,returnIntent);
        finish();
    }

    private File makeFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        picPath = image.getAbsolutePath();
        return image;
    }

}

package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.tasks.BookListContent;

import java.util.Random;

import static com.example.app.tasks.BookListContent.addItem;
import static com.example.app.tasks.BookListContent.createBook;


public class MainActivity extends AppCompatActivity implements BookFragment.OnListFragmentInteractionListener {

    public static final String bookExtra = "bookExtra";
    public SharedPreferences preferences;
    String PREFERENCES_NAME = "myPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(PREFERENCES_NAME, MainActivity.MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        //editor.clear();
        //editor.commit();
        loadData();
        addFabsListeners();

    }

    private void addFabsListeners() {
        FloatingActionButton fabPlus = findViewById(R.id.fabPlus);
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(view.getContext(), AddActivity.class);
                startActivityForResult(intentAdd, 1);
            }
        });
        FloatingActionButton fabCamera = findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Camera", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == AddActivity.RESULT_OK) {
                String title = data.getStringExtra("titleRet");
                String author = data.getStringExtra("authorRet");
                String date = data.getStringExtra("dateRet");

                Random rand = new Random();
                int number = rand.nextInt(5);
                number += 1;

                saveData(title, author, date, "pic" + number);
                loadData();
                ((BookFragment) getSupportFragmentManager().findFragmentById(R.id.bookFragment)).notifyDataChange();

            }
            if (resultCode == AddActivity.RESULT_CANCELED) {

            }
        }
    }//onActivityResult

    private void saveData(String title, String author, String date, String picName) {
        int positionItem = 0;
        while (preferences.getString("title" + positionItem, "") != "")
            positionItem++; //save to first empty

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("title" + positionItem, title);
        preferencesEditor.putString("author" + positionItem, author);
        preferencesEditor.putString("date" + positionItem, date);
        preferencesEditor.putString("picName" + positionItem, picName);
        preferencesEditor.commit();
    }

    private void loadData() {
        BookListContent.deleteItems();
        int positionItem = 0;
        while (preferences.getString("title" + positionItem, "") != "") {
            String titleFromPreferences = preferences.getString("title" + positionItem, "");
            String authorFromPreferences = preferences.getString("author" + positionItem, "");
            String dateFromPreferences = preferences.getString("date" + positionItem, "");
            String picFromPreferences = preferences.getString("picName" + positionItem, "");

            addItem(createBook(positionItem++, titleFromPreferences, authorFromPreferences, dateFromPreferences, picFromPreferences));
            ((BookFragment) getSupportFragmentManager().findFragmentById(R.id.bookFragment)).notifyDataChange();
        }
    }

    @Override
    public void onListFragmentClickInteraction(BookListContent.Book book, int position) {
        startBookDetailsActivity(book, position);
    }

    private void startBookDetailsActivity(BookListContent.Book book, int position) {
        Intent intentDetails = new Intent(this, BookDetailsActivity.class);
        intentDetails.putExtra(bookExtra, book);
        startActivity(intentDetails);
    }

    public void OnDeleteButtonClickListener(int position) {
        deleteItem(position);
    }

    private void deleteItem(int position) {
        preferences.edit().remove("title" + position).apply();
        preferences.edit().remove("author" + position).apply();
        preferences.edit().remove("date" + position).apply();
        preferences.edit().remove("picName" + position).apply();
        repairId(position);
        loadData();
        ((BookFragment) getSupportFragmentManager().findFragmentById(R.id.bookFragment)).notifyDataChange();
    }

    private void repairId(int deletedPosition) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        int nextPosition = deletedPosition + 1;
        int previewPosition = deletedPosition;

        while (preferences.getString("title" + nextPosition, "") != "") {
            String titleFromPreferences = preferences.getString("title" + nextPosition, "");       //read next item
            String authorFromPreferences = preferences.getString("author" + nextPosition, "");
            String dateFromPreferences = preferences.getString("date" + nextPosition, "");
            String picFromPreferences = preferences.getString("picName" + nextPosition, "");

            preferencesEditor.putString("title" + previewPosition, titleFromPreferences);           //save with --id
            preferencesEditor.putString("author" + previewPosition, authorFromPreferences);
            preferencesEditor.putString("date" + previewPosition, dateFromPreferences);
            preferencesEditor.putString("picName" + previewPosition, picFromPreferences);

            previewPosition++;
            nextPosition++;
        }
        preferencesEditor.apply();

        preferences.edit().remove("title" + previewPosition).apply();        //delete last duplicate
        preferences.edit().remove("author" + previewPosition).apply();
        preferences.edit().remove("date" + previewPosition).apply();
        preferences.edit().remove("picName" + previewPosition).apply();

    }
}

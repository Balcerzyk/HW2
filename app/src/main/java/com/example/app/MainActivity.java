package com.example.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.app.tasks.BookListContent;

import java.util.Random;

import static com.example.app.tasks.BookListContent.addItem;
import static com.example.app.tasks.BookListContent.createBook;


public class MainActivity extends AppCompatActivity implements BookFragment.OnListFragmentInteractionListener {

    public static final String bookExtra = "bookExtra";
    public SharedPreferences preferences;
    String PREFERENCES_NAME = "myPreferences";

    public static final int REQUEST_IMAGE_CAPTURE = 1; // request code for image capture
    private String mCurrentPhotoPath; // String used to save the path of the picture
    private BookListContent.Book displayedBook;

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
                Intent intentAdd = new Intent(view.getContext(), AddActivity.class);
                intentAdd.putExtra("camera", 1)
;               startActivityForResult(intentAdd, 1);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == AddActivity.RESULT_OK) {
                String title = data.getStringExtra("titleRet");
                String author = data.getStringExtra("authorRet");
                String date = data.getStringExtra("dateRet");
                String picPath = data.getStringExtra("picPath");

                if(picPath == null){
                    Random rand = new Random();
                    int number = rand.nextInt(5);
                    number += 1;

                    saveData(title, author, date, "pic" + number);
                }
                else saveData(title, author, date, picPath);

                loadData();
                ((BookFragment) getSupportFragmentManager().findFragmentById(R.id.bookFragment)).notifyDataChange();

            } else if (resultCode == AddActivity.RESULT_CANCELED) {

            }

        }

    }

    private void saveData(String title, String author, String date, String picPath) {
        int positionItem = 0;
        while (preferences.getString("title" + positionItem, "") != "")
            positionItem++; //save to first empty

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString("title" + positionItem, title);
        preferencesEditor.putString("author" + positionItem, author);
        preferencesEditor.putString("date" + positionItem, date);
        preferencesEditor.putString("picPath" + positionItem, picPath);
        preferencesEditor.commit();
    }

    private void loadData() {
        BookListContent.deleteItems();
        int positionItem = 0;
        while (preferences.getString("title" + positionItem, "") != "") {
            String titleFromPreferences = preferences.getString("title" + positionItem, "");
            String authorFromPreferences = preferences.getString("author" + positionItem, "");
            String dateFromPreferences = preferences.getString("date" + positionItem, "");
            String picFromPreferences = preferences.getString("picPath" + positionItem, "");

            addItem(createBook(positionItem++, titleFromPreferences, authorFromPreferences, dateFromPreferences, picFromPreferences));
            ((BookFragment) getSupportFragmentManager().findFragmentById(R.id.bookFragment)).notifyDataChange();
        }
    }

    @Override
    public void onListFragmentClickInteraction(BookListContent.Book book, int position) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TextView titleDetails = findViewById(R.id.book_details_title);
            TextView authorDetails = findViewById(R.id.book_details_author);
            TextView dateDetails = findViewById(R.id.book_details_date);
            TextView picDetails = findViewById(R.id.book_details_image);

            titleDetails.setText("LEL");
        } else {
            startBookDetailsActivity(book, position);
        }

    }

    private void startBookDetailsActivity(BookListContent.Book book, int position) {
        Intent intentDetails = new Intent(this, BookDetailsActivity.class);
        intentDetails.putExtra(bookExtra, book);
        startActivity(intentDetails);
    }

    public void OnDeleteButtonClickListener(int position) {
        deleteItem(position);
    }

    private void deleteItem(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Potwierdź usunięcie")
                .setMessage("Czy napewno chcesz usunąć tę książkę?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        preferences.edit().remove("title" + position).apply();
                        preferences.edit().remove("author" + position).apply();
                        preferences.edit().remove("date" + position).apply();
                        preferences.edit().remove("picPath" + position).apply();
                        repairId(position);
                        loadData();
                        ((BookFragment) getSupportFragmentManager().findFragmentById(R.id.bookFragment)).notifyDataChange();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    private void repairId(int deletedPosition) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        int nextPosition = deletedPosition + 1;
        int previewPosition = deletedPosition;

        while (preferences.getString("title" + nextPosition, "") != "") {
            String titleFromPreferences = preferences.getString("title" + nextPosition, "");       //read next item
            String authorFromPreferences = preferences.getString("author" + nextPosition, "");
            String dateFromPreferences = preferences.getString("date" + nextPosition, "");
            String picFromPreferences = preferences.getString("picPath" + nextPosition, "");

            preferencesEditor.putString("title" + previewPosition, titleFromPreferences);           //save with --id
            preferencesEditor.putString("author" + previewPosition, authorFromPreferences);
            preferencesEditor.putString("date" + previewPosition, dateFromPreferences);
            preferencesEditor.putString("picPath" + previewPosition, picFromPreferences);

            previewPosition++;
            nextPosition++;
        }
        preferencesEditor.apply();

        preferences.edit().remove("title" + previewPosition).apply();        //delete last duplicate
        preferences.edit().remove("author" + previewPosition).apply();
        preferences.edit().remove("date" + previewPosition).apply();
        preferences.edit().remove("picPath" + previewPosition).apply();

    }
}

package com.example.app;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.tasks.BookListContent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BookDetailsFragment extends Fragment{

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    public void displayBookDetails(BookListContent.Book book){
        FragmentActivity activity = getActivity();

        TextView bookTitle = activity.findViewById(R.id.book_details_title);
        TextView bookAuthor = activity.findViewById(R.id.book_details_author);
        TextView bookDate = activity.findViewById(R.id.book_details_date);
        ImageView bookImage = activity.findViewById(R.id.book_details_image);

        bookTitle.setText(book.title);
        bookAuthor.setText("Autor: " + book.author);
        bookDate.setText("Data Publikacji: " + book.date);

        String picPath = book.picPath;

        Bitmap image = BitmapFactory.decodeFile(picPath);

        bookImage.setImageBitmap(image);

        //bookImage.setImageResource(getResources().getIdentifier(picPath, "drawable", getActivity().getPackageName()));
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if(intent != null){
            BookListContent.Book receivedBook = intent.getParcelableExtra("bookExtra");
            if(receivedBook != null){
                displayBookDetails(receivedBook);
            }
        }
    }
}

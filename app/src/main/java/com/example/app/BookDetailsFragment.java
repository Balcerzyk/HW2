package com.example.app;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.tasks.BookListContent;


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

        String picName = book.picName;
        bookImage.setImageResource(getResources().getIdentifier(picName, "drawable", getActivity().getPackageName()));
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

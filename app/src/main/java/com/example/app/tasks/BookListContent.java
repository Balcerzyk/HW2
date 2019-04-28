package com.example.app.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BookListContent {

    public static final List<Book> ITEMS = new ArrayList<Book>();
    public static final Map<String, Book> ITEM_MAP = new HashMap<String, Book>();

    public static void addItem(Book item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static Book createBook(int position, String title, String author, String date, String picName) {

        return new Book(String.valueOf(position), title ,author, date, picName);
    }

    public static void deleteItems(){
        ITEMS.clear();
    }

    public static class Book implements Parcelable {
        public final String id;
        public final String title;
        public final String author;
        public final String date;
        public final String picName;

        public Book(String id, String title, String author, String date) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.date = date;
            this.picName = "";
        }

        public Book(String id, String title, String author, String date, String picName) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.date = date;
            this.picName = picName;
        }

        protected Book(Parcel in) {
            id = in.readString();
            title = in.readString();
            author = in.readString();
            date = in.readString();
            picName = in.readString();
        }

        public static final Creator<Book> CREATOR = new Creator<Book>() {
            @Override
            public Book createFromParcel(Parcel in) {
                return new Book(in);
            }

            @Override
            public Book[] newArray(int size) {
                return new Book[size];
            }
        };

        @Override
        public String toString() {
            return title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(title);
            dest.writeString(author);
            dest.writeString(date);
            dest.writeString(picName);
        }
    }
}

package com.needle.soniaditi380.books;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static java.lang.Double.NaN;

/**
 * Created by Aditya on 23-Jan-18.
 */

public class SearchAdapter extends ArrayAdapter<Book>{


    /**

     * Constructs a new {@link SearchAdapter}.

     *

     * @param context     of the app

     * @param books is the list of books, which is the data source of the adapter

     */

    public SearchAdapter(Context context, List<Book> books) {

        super(context, 0, books);

    }



    /**

     * Returns a list item view that displays information about the booksat the given position

     * in the list of books.

     */

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if there is an existing list item view (called convertView) that we can reuse,

        // otherwise, if convertView is null, then inflate a new list item layout.

        View listItemView = convertView;

        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(

                    R.layout.book_list_item, parent, false);

        }



        // Find the book at the given position in the list of earthquakes

        Book currentBook = getItem(position);





        // Find the TextView with view ID book_title

        TextView titleView = (TextView) listItemView.findViewById(R.id.book_title);

        titleView.setText(currentBook.getmTitle());


        // Find the TextView with view ID Author

        TextView AuthorView = (TextView) listItemView.findViewById(R.id.Author);

        // Display the Author of the current book in that TextView

        AuthorView.setText("by "+currentBook.getAuthor());

        // Find the ImageView with view ID coverimage

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.coverimage);

        // Display the cover of the current book in that imageView
        // Check if an image is provided for this book or not

        if (currentBook.hasImage()) {

            // If an image is available, display the provided image based on the resource ID

            Picasso.with(getContext()).load(currentBook.getImageResourceUrl().toString()).into(imageView);


        } else {

            // Otherwise set the ImageView to default image

            Picasso.with(getContext()).load("https://www.sarvgyan.com/hc/wp-content/uploads/2014/03/image-unavailable.jpg").into(imageView);

        }


        // Find the Rating bar with view ID ratingbar

        RatingBar ratingBar = (RatingBar) listItemView.findViewById(R.id.ratingbar);

        ratingBar.setRating((float) currentBook.getRatings());

        //changin the color of stars od rating bar

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ffc837"), PorterDuff.Mode.SRC_ATOP);

        // Find the textview with view ID rating_text
        TextView ratingText= (TextView) listItemView.findViewById(R.id.rating_text);
        ratingText.setText(currentBook.getRatings()+"");


        // Return the list item view that is now showing the appropriate data

        return listItemView;



    }


}

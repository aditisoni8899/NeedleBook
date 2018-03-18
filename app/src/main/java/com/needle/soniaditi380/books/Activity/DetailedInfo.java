package com.needle.soniaditi380.books.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.needle.soniaditi380.books.R;
import com.needle.soniaditi380.books.SaveSharedPrefrence;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static android.content.ContentValues.TAG;

public class DetailedInfo extends AppCompatActivity {

    //uri to which the requesst should be made
    static String url = "";
    //bookTitle of the book
    static String bookTitle = "";
    //author name of the book
    static String authName = "";
    //book Id of the book
    static String bookVolumeId = "";
    //response code for adding to the bookshelf
    public static int responseCode = 0;
    //rating of the book
    static double rating =0.0;


    ImageView imageView;
    TextView bookName;
    TextView authorName;
    FloatingTextButton addToShelf;
    RatingBar ratingBar;
    TextView ratingText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        // to set the width and height of the class
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * (.8)));

        //get the data from intent
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        //extracting extras from the intent

        url = extras.getString("url");
        bookTitle = extras.getString("book_tit");
        authName = extras.getString("author");
        bookVolumeId = extras.getString("book_id");
        rating = extras.getDouble("book_ratings");

        Log.d("volId", "onCreate: "+bookVolumeId);


        imageView = (ImageView) findViewById(R.id.info_coverimage);
        bookName = (TextView) findViewById(R.id.bookname);
        authorName = (TextView) findViewById(R.id.authname);
        // Find the Rating bar with view ID ratingbar
        ratingBar = (RatingBar) findViewById(R.id.ratingbar);
        // Find the textview with view ID rating_text
        ratingText= (TextView) findViewById(R.id.rating_text);




        //setting the title oof the book the textview
        bookName.setText(bookTitle);
        authorName.setText("by "+authName);
        ratingBar.setRating((float) rating);
        //changin the color of stars od rating bar

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ffc837"), PorterDuff.Mode.SRC_ATOP);
        //setting the rating text to the textview
       ratingText.setText(rating+"");

        // If an image is available, display the provided image based on the resource ID
        if(!url.isEmpty())
            Picasso.with(this).load(url).into(imageView);
        else
            imageView.setImageResource(R.drawable.noimageavailable);


        addToShelf = (FloatingTextButton) findViewById(R.id.action_button);
        //on click listener on the add to bookshelves
        addToShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get a reference to the ConnectivityManager to check state of network connectivity

                ConnectivityManager connMgr = (ConnectivityManager)

                        getSystemService(Context.CONNECTIVITY_SERVICE);


                // Get details on the currently active default data network

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


                // If there is a network connection, fetch data

                if (networkInfo != null && networkInfo.isConnected()) {

                    AddToShelf login = new AddToShelf();

                    login.execute();

                } else {

                    Toast.makeText(DetailedInfo.this, "No Internet connection available", Toast.LENGTH_LONG).show();

                }

            }

        });
    }

    class AddToShelf extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = ProgressDialog.show(DetailedInfo.this, "", "Loading", true);

        }


        @Override

        protected Void doInBackground(Void... strings) {




                        Uri baseUri = Uri.parse("https://www.googleapis.com/books/v1/mylibrary/bookshelves/0/addVolume?");

                        //buildUpon () Constructs a new builder, copying the attributes from this Uri.

                        Uri.Builder uriBuilder = baseUri.buildUpon();

                        uriBuilder.appendQueryParameter("volumeId", bookVolumeId);

                        uriBuilder.appendQueryParameter("key", "AIzaSyAkTkENhX49nx4WYVxIIANdSvEEBtaLMqI");

                        uriBuilder.appendQueryParameter("Content-Type", "application/json");

                        uriBuilder.appendQueryParameter("Content-Length", "CONTENT_LENGTH");


                        URL url = null;
                        try {
                            url = new URL(uriBuilder.toString());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        HttpURLConnection urlConnection = null;

                        try {

                            urlConnection = (HttpURLConnection) url.openConnection();

                            urlConnection.setReadTimeout(10000 /* milliseconds */);

                            urlConnection.setConnectTimeout(15000 /* milliseconds */);

                            urlConnection.setRequestMethod("POST");

                            urlConnection.setRequestProperty("Authorization", "Bearer " + SaveSharedPrefrence.getAccessToken(DetailedInfo.this));

                            Log.d(TAG, "makeHttpRequest: " + SaveSharedPrefrence.getAuthCode(DetailedInfo.this));

                            urlConnection.connect();


                            //getting response code
                            responseCode= urlConnection.getResponseCode();

                            Log.d(TAG, "doInBackground: "+ responseCode + " " + urlConnection.getResponseMessage());

                        } catch (IOException e) {

                            Log.e("DetailInfo Class", "Problem in accessing server", e);

                        } finally {

                            if (urlConnection != null) {

                                urlConnection.disconnect();

                            }


                            return null;

                        }


        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (responseCode == 204) {
                progressDialog.dismiss();
                Toast.makeText(DetailedInfo.this, "added to the bookshelf", Toast.LENGTH_LONG).show();

            } else {

                progressDialog.dismiss();
                Toast.makeText(DetailedInfo.this, "something went wrong", Toast.LENGTH_LONG).show();

            }


        }
    }

}

package com.needle.soniaditi380.books.Activity;


import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.needle.soniaditi380.books.Book;
import com.needle.soniaditi380.books.BookLoader;
import com.needle.soniaditi380.books.R;
import com.needle.soniaditi380.books.SearchAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    /**

     * TextView that is displayed when the list is empty

     */

    private TextView mEmptyStateTextView;



    /**

     * Constant value for the book loader ID. We can choose any integer.

     * This really only comes into play if you're using multiple loaders.

     */

    private static final int BOOK_LOADER_ID = 1;





    /**

     * Adapter for the list of books

     */

    private SearchAdapter mAdapter;



    /**

     * URL for earthquake data from the USGS dataset

     */

    private static final String BOOK_REQUEST_URL =

            "https://www.googleapis.com/books/v1/volumes?";



    private static final String LOG_TAG = SearchActivity.class.getName();

    final String QUERY_PARAM = "q"; // Parameter for the search string.

    final String MAX1_RESULT = "maxResults"; // Parameter that limits search results.

    final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

    final String ORDER_BY = "orderBy";//parameter to filter by relevance

    final String KEY = "key"; // api key


    static String volume = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        //change Action bar text color
        SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#616163")), 0, getSupportActionBar().getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);


        //get the data from intent
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        volume = extras.getString("query");


        // Find a reference to the {@link ListView} in the layout

        final ListView bookListView = (ListView) findViewById(R.id.list);





        // Create a new adapter that takes an empty list of books as input

        mAdapter = new SearchAdapter(this, new ArrayList<Book>());



        // Set the adapter on the {@link ListView}

        // so the list can be populated in the user interface

        bookListView.setAdapter(mAdapter);



        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        bookListView.setEmptyView(mEmptyStateTextView);



        // Set an item click listener on the ListView, which sends an intent to DetailedInfo Activity

        // to open a book with more information about the selected book.

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current earthquake that was clicked on

                Book currentBook = mAdapter.getItem(position);


                String id=currentBook.getmID();
                URL url = currentBook.getImageResourceUrl();
                String title = currentBook.getmTitle();
                String author= currentBook.getAuthor();
                String bookId = currentBook.getmID();
                double rating = currentBook.getRatings();

                Intent intent = new Intent(getApplicationContext(),DetailedInfo.class);


                if (url==null) {
                    try {
                        url = new URL("https://www.sarvgyan.com/hc/wp-content/uploads/2014/03/image-unavailable.jpg");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                intent.putExtra("url",url.toString());
                intent.putExtra("book_tit",title);
                intent.putExtra("author",author);
                intent.putExtra("book_id",bookId);
                intent.putExtra("book_ratings",rating);

                //View sharedView = view.findViewById(R.id.book_title);
                Pair<View, String> p1 = Pair.create(view.findViewById(R.id.coverimage),"MyImage");
                Pair<View, String> p2 = Pair.create(view.findViewById(R.id.book_title),"Book_Name_shared");
                Pair<View, String> p3 = Pair.create(view.findViewById(R.id.Author),"Author_name_shared");


                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation
                        (SearchActivity.this,p1,p2,p3);
                //Send the intent to launch a new activity
                startActivity(intent, transitionActivityOptions.toBundle());


            }

        });



        // Get a reference to the ConnectivityManager to check state of network connectivity

        ConnectivityManager connMgr = (ConnectivityManager)

                getSystemService(Context.CONNECTIVITY_SERVICE);



        // Get details on the currently active default data network

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();



        // If there is a network connection, fetch data

        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.

            LoaderManager loaderManager = getLoaderManager();



            // Initialize the loader. Pass in the int ID constant defined above and pass in null for

            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid

            // because this activity implements the LoaderCallbacks interface).

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        } else {

            // Otherwise, display error

            // First, hide loading indicator so error message will be visible

            View loadingIndicator = findViewById(R.id.loading_indicator);

            loadingIndicator.setVisibility(View.GONE);



            // Update empty state with no connection error message

            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }


    }


    @Override

    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(BOOK_REQUEST_URL);

        //buildUpon () Constructs a new builder, copying the attributes from this Uri.

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(QUERY_PARAM,volume);

        uriBuilder.appendQueryParameter(MAX1_RESULT, "10");

        uriBuilder.appendQueryParameter(PRINT_TYPE, "books");

        uriBuilder.appendQueryParameter(ORDER_BY,"relevance");

      uriBuilder.appendQueryParameter(KEY,"AIzaSyAkTkENhX49nx4WYVxIIANdSvEEBtaLMqI");

        Log.d("oooooo", "onCreateLoader: "+uriBuilder);

        return new BookLoader(this, uriBuilder.toString());



    }



    @Override

    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {



        // Hide loading indicator because the data has been loaded

        View loadingIndicator = findViewById(R.id.loading_indicator);

        loadingIndicator.setVisibility(View.GONE);



        // Set empty state text to display "No Book found."

        mEmptyStateTextView.setText(R.string.no_books);



        // Clear the adapter of previous book data

        mAdapter.clear();



        // If there is a valid list of {@link Book}s, then add them to the adapter's

        // data set. This will trigger the ListView to update.

        if ( books != null && !books.isEmpty()) {

            mAdapter.addAll(books);

        }

    }



    @Override

    public void onLoaderReset(Loader<List<Book>> loader) {

        // Loader reset, so we can clear out our existing data.

        mAdapter.clear();



    }
}

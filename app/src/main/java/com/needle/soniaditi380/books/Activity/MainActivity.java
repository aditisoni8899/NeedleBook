package com.needle.soniaditi380.books.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.needle.soniaditi380.books.MyNavDrawerAdapter;
import com.needle.soniaditi380.books.R;
import com.needle.soniaditi380.books.SaveSharedPrefrence;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    GoogleApiClient mGoogleApiClient;

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    String TITLES[] = {"Discover","My Books","Sign Out"};


    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "Aditi";
    String EMAIL = "";
    URL PROFILE = null;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    public MainActivity() throws MalformedURLException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting view to the Activity
        setContentView(R.layout.activity_main);

        /* Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar
        */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        NAME= SaveSharedPrefrence.getName(getApplicationContext());
        EMAIL=SaveSharedPrefrence.getUserName(getApplicationContext());
        try {
            if(SaveSharedPrefrence.getphoto(getApplication()).toString().length()!=0)
            PROFILE= new URL(SaveSharedPrefrence.getphoto(getApplicationContext()).toString());
            else {
                PROFILE=new URL("https://dealmarkaz.pk/oc-content/themes/DealMarkaz/images/user-default-profile-pict.jpg");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        //for navigation drawer
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new MyNavDrawerAdapter(TITLES,NAME,EMAIL,PROFILE,this);       // Creating the Adapter of MyNavDrawerAdapter class(which we are going to see in a bit)
        // And passing the titles,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView



        // to identify gesture and close the navigation drawer on item click event
        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();

                    if(mRecyclerView.getChildPosition(child)==1)
                    {

                    }


                    if(mRecyclerView.getChildPosition(child)==2)
                    {
                        //Intent intent = new Intent(getApplicationContext(), MyBooks.class);
                        //startActivity(intent);
                    }


                    if(mRecyclerView.getChildPosition(child)==3)
                    {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        // ...
                                        Toast.makeText(getApplicationContext(),"Signed Out",Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(getApplicationContext(),SignIn.class);
                                        startActivity(i);
                                        SaveSharedPrefrence.clear(getApplicationContext());
                                        finish();
                                    }
                                });



                    }
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });







        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State




        //change Action bar text color
        SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#616163")), 0, getSupportActionBar().getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

    }


    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_search_icon);
        SearchView searchView = (SearchView) item.getActionView();
        //addingg listener for user actions within the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Called when the user submits the query.
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            //Called when the query text is changed by the user.
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search_icon) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }




}
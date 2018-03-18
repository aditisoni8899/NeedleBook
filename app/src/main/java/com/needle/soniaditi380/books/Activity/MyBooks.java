package com.needle.soniaditi380.books.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.needle.soniaditi380.books.R;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import static com.google.android.gms.drive.Drive.getDriveResourceClient;

public class MyBooks extends AppCompatActivity {

    private static final String TAG = "drive-quickstart";

    private static final int REQUEST_CODE_SIGN_IN = 0;

    private static final int REQUEST_CODE_CREATOR = 2;

    private static final int FILE_SELECT_CODE = 3;





    private GoogleSignInClient mGoogleSignInClient;

    private DriveClient mDriveClient;

    private DriveResourceClient mDriveResourceClient;

    private Uri mBitmapToSave;

    private String path;

    private FloatingActionButton fab;



    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_books);

        //change Action bar text color
        SpannableString s = new SpannableString(getSupportActionBar().getTitle());
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#616163")), 0, getSupportActionBar().getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        fab= (FloatingActionButton) findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }



    /** Start sign in activity. */

    private void signIn() {

        Log.i(TAG, "Start sign in");

        mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);


    }
    /**

     * Fires an intent to spin up the "file chooser" UI and select an image.

     */

    private void showFileChooser() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file

        // browser.

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("application/pdf");

        // Filter to only show results that can be "opened", such as a

        // file (as opposed to a list of contacts or timezones)

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager

        Intent sintent = new Intent("com.sec.android.app.myfiles.PICK_DATA");

        sintent.putExtra("CONTENT_TYPE", "*/*");

        sintent.setType("text/pdf");

        sintent.addCategory(Intent.CATEGORY_DEFAULT);





        Intent chooserIntent;

        if (getPackageManager().resolveActivity(sintent, 0) != null){

            // it is device with samsung file manager

            chooserIntent = Intent.createChooser(sintent,"Select Pdf File to Upload");

            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent});

        }

        else {

            chooserIntent = Intent.createChooser(intent,"Select Pdf File to Upload");

        }





        try {



            startActivityForResult(chooserIntent,FILE_SELECT_CODE);



        } catch (android.content.ActivityNotFoundException ex) {

            // Potentially direct the user to the Market with a Dialog

            Toast.makeText(this, "Please install a File Manager.",

                    Toast.LENGTH_SHORT).show();

        }

    }



    /** Build a Google SignIn client. */

    private GoogleSignInClient buildGoogleSignInClient() {

        GoogleSignInOptions signInOptions =

                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                        .requestScopes(Drive.SCOPE_FILE)

                        .build();

        return GoogleSignIn.getClient(this, signInOptions);

    }



    /** Create a new file and save it to Drive. */

    private void saveFileToDrive() {

        // Start by creating a new contents, and setting a callback.

        Log.i(TAG, "Creating new contents.");

        final Uri image = mBitmapToSave;


        mDriveResourceClient

                .createContents()

                .continueWithTask(

                        new Continuation<DriveContents, Task<Void>>() {

                            @Override


                            public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {

                                return createFileIntentSender(task.getResult(), image);

                            }

                        })

                .addOnFailureListener(

                        new OnFailureListener() {

                            @Override

                            public void onFailure(@NonNull Exception e) {

                                Log.w(TAG, "Failed to create new contents.", e);

                            }

                        });

    }



    /**

     * Creates an {@link IntentSender} to start a dialog activity with configured {@link

     * CreateFileActivityOptions} for user to create a new pdf in Drive.

     */

    private Task<Void> createFileIntentSender(DriveContents driveContents, Uri image) {

        Log.i(TAG, "New contents created.    "+image);

        // to extract filename from the path
      String filename = path.substring(path.lastIndexOf("/") + 1);

        DriveContents contents = driveContents;
        OutputStream outputStream = contents.getOutputStream();
    try{
        InputStream inputStream = getContentResolver().openInputStream(image);

        if (inputStream != null) {
            byte[] data = new byte[1024];
            while (inputStream.read(data) != -1) {
                outputStream.write(data);
            }
            inputStream.close();
        }

        outputStream.close();
    } catch (IOException e) {
        Log.e(TAG, e.getMessage());
    }




        // Create the initial metadata - MIME type and title.

        // Note that the user will be able to change the title later.

        MetadataChangeSet metadataChangeSet =

                new MetadataChangeSet.Builder()
                        .setTitle(filename)
                        .setMimeType("text/pdf")
                        .setStarred(true)
                        .build();

        // Set up options to configure and display the create file activity.

        CreateFileActivityOptions createFileActivityOptions =

                new CreateFileActivityOptions.Builder()

                        .setInitialMetadata(metadataChangeSet)

                        .setInitialDriveContents(driveContents)

                        .build();



        return mDriveClient

                .newCreateFileActivityIntentSender(createFileActivityOptions)

                .continueWith(

                        new Continuation<IntentSender, Void>() {

                            @Override

                            public Void then(@NonNull Task<IntentSender> task) throws Exception {

                                startIntentSenderForResult(task.getResult(), REQUEST_CODE_CREATOR, null, 0, 0, 0);

                                return null;

                            }

                        });

    }



    @Override

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE_SIGN_IN:

                Log.i(TAG, "Sign in request code");

                // Called after user is signed in.

                if (resultCode == RESULT_OK) {

                    Log.i(TAG, "Signed in successfully.");

                    // Use the last signed in account here since it already have a Drive scope.

                    mDriveClient = Drive.getDriveClient(this, GoogleSignIn.getLastSignedInAccount(this));

                    // Build a drive resource client.

                    mDriveResourceClient =

                            getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this));


                    // Start file chooser

                   showFileChooser();

                }

                break;

            case FILE_SELECT_CODE:

                Log.i(TAG, "select file request code");

                // Called after a photo has been taken.

                if (resultCode == Activity.RESULT_OK) {

                    Log.i(TAG, "pdf selected successfully.");

                    // The document selected by the user won't be returned in the intent.

                    // Instead, a URI to that document will be contained in the return intent

                    // provided to onactivityresult method as a parameter.

                    // Pull that URI using resultData.getData().

                    // Get the Uri of the selected file

                    Uri uri = data.getData();

                    Log.d("onActivityREsult", "File Uri: " + uri.toString());

                    // Get the path
                    mBitmapToSave=uri;
                    // Get the path
                    try {
                        path= getFilePath(getApplicationContext(),uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "onActivityResult: "+mBitmapToSave);

                    saveFileToDrive();
                }

                break;

            case REQUEST_CODE_CREATOR:

                Log.i(TAG, "creator request code");

                // Called after a file is saved to Drive.

                if (resultCode == RESULT_OK) {

                    Log.i(TAG, "Image successfully saved.");

                    mBitmapToSave = null;

                    // Just start the camera again for another photo.


                    new Intent(getApplicationContext(),MyBooks.class);
                    Toast.makeText(getApplicationContext(),"uploaded successfully",Toast.LENGTH_SHORT).show();

                }

                break;

        }

    }


    //get path of the file

    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isDriveDocument(Uri uri) {
        return "com.google.android.apps.docs.storage/document".equals(uri.getAuthority());
    }


}

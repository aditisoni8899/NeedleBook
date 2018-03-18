package com.needle.soniaditi380.books;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Aditya on 23-Jan-18.
 */

public class QueryUtils {

    /** Tag for the log messages */

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String client_id ="62897478377-lqovsp4j12rdj1nvjr4leegechjgucos.apps.googleusercontent.com";
    private static final String client_secret = "vPu0WLGtQq_X7___VU4SyDT3";
    final private List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/books");


    /**

     * Create a private constructor because no one should ever create a {@link QueryUtils} object.

     * This class is only meant to hold static variables and methods, which can be accessed

     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).

     */

    private QueryUtils() {



    }





    /**

     * Query the USGS dataset and return a list of {@link Book} objects.

     */

    public static List<Book> fetchEarthquakeData(String requestUrl,Context context) {



        try {

            Thread.sleep(2000);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }



        // Create URL object

        URL url = createUrl(requestUrl);



        // Perform HTTP request to the URL and receive a JSON response back

        String jsonResponse = null;

        try {

            jsonResponse = makeHttpRequest(url,context);
            Log.d("basejson", "fetchEarthquakeData: "+jsonResponse);

        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem making the HTTP request.", e);

        }



        // Extract relevant fields from the JSON response and create a list of {@link Book}s

        List<Book> books = extractFeatureFromJson(jsonResponse);



        // Return the list of {@link Book}s

        return books;

    }





    /**

     * Return a list of {@link Book} objects that has been built up from

     * parsing the given JSON response.

     */

    private static List<Book> extractFeatureFromJson(String bookJSON) {

        // If the JSON string is empty or null, then return early.

        if (TextUtils.isEmpty(bookJSON)) {

            return null;

        }



        // Create an empty ArrayList that we can start adding earthquakes to

        List<Book> books = new ArrayList<>();



        // Try to parse the JSON response string. If there's a problem with the way the JSON

        // is formatted, a JSONException exception object will be thrown.

        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {



            // Create a JSONObject from the JSON response string

            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Get the JSONArray of book items.

            JSONArray bookArray = baseJsonResponse.getJSONArray("items");



            // For each book in the bookArray, create an {@link Book} object

            for (int i = 0; i < bookArray.length(); i++) {



                // Get a single book at position i within the list of books

                JSONObject currentBook = bookArray.getJSONObject(i);

                String id= currentBook.getString("id");


                // For a given book, extract the JSONObject associated with the

                // key called "volumeInfo", which represents a list of all properties

                // for that book.

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                //getting title of the book
                String  title = volumeInfo.optString("title");
                //getting  author of the book
                String  authors = volumeInfo.optString("authors");
                String auth="";
                if(authors.length()>0)
                auth =authors.substring(2,authors.length()-2);

                //getting  average ratings of the books
               double rating = volumeInfo.optDouble("averageRating");


                //getting the description of the book
                String description =volumeInfo.optString(  "description");

                //getting the pageCount of the book
                int pageCount= volumeInfo.optInt("pageCount");

               //getting the category of the book
                String cate= volumeInfo.optString("categories");
                String category="";
                if(cate.length()>0)
                category = cate.substring(2,cate.length()-2);

                //getting rating Count of the book
                int ratingCount= volumeInfo.optInt("ratingsCount");
                Log.d("99999999", "extractFeatureFromJson: "+ratingCount);
                //getting preview url of the book
                String bookpreview =volumeInfo.optString("previewLink");
                URL previewlink=null;
                try {
                    previewlink=new URL(bookpreview);
                }catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }

                //getting price and its currency code of the book;
                JSONObject saleinfo= currentBook.getJSONObject("saleInfo");
                double price = 0;
                String currencyCode = "INR";
                String saleability = saleinfo.optString("saleability");
                if (saleability.equalsIgnoreCase("FOR_SALE")) {
                    JSONObject listprice = saleinfo.optJSONObject("listPrice");
                    if(listprice!=null) {
                        price = listprice.optDouble("amount");
                        currencyCode = listprice.optString("currencyCode");

                    }
                }
                else

                {

                    price=0;
                    currencyCode="NAN";
                }




                JSONObject imgagelink = volumeInfo.optJSONObject("imageLinks");
                //getting cover image link
                String link="";
                if (imgagelink!=null)
                    link= imgagelink.getString("smallThumbnail");
                URL url=null;
                try {

                    //converting string to url
                    URI uri = new URI(link);
                    url = uri.toURL();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                // Create a new {@link Book} object with the id, title, author, rating

                // and url of image from the JSON response.

                Book book = new Book(rating, title, auth,url,id,description
                        ,pageCount,category,
                        ratingCount,previewlink,price,currencyCode);

                // Add the new {@link Book} to the list of books.

                books.add(book);

            }



        } catch (JSONException e) {

            // If an error is thrown when executing any of the above statements in the "try" block,

            // catch the exception here, so the app doesn't crash. Print a log message

            // with the message from the exception.

            Log.e("QueryUtils", "Problem parsing the books JSON results", e);

        }



        // Return the list of books

        return books;

    }







    /**

     * Returns new URL object from the given string URL.

     */

    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {

            url = new URL(stringUrl);

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Problem building the URL ", e);

        }

        return url;

    }



    /**

     * Make an HTTP request to the given URL and return a String as the response.

     */

    private static String makeHttpRequest(URL url, Context c) throws IOException {

        String jsonResponse = "";
        String token = null;



        // If the URL is null, then return early.

        if (url == null) {

            return jsonResponse;

        }


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://www.googleapis.com/oauth2/v4/token");

        try {
            List nameValuePairs = new ArrayList(1);
            nameValuePairs.add(new BasicNameValuePair("Content-Type:","application/x-www-form-urlencoded"));
            nameValuePairs.add(new BasicNameValuePair("code",SaveSharedPrefrence.getAuthCode(c)));
            nameValuePairs.add(new BasicNameValuePair("scope", "https://www.googleapis.com/auth/books"));
            nameValuePairs.add(new BasicNameValuePair("client_id", client_id));
            nameValuePairs.add(new BasicNameValuePair("client_secret",client_secret));
            nameValuePairs.add(new BasicNameValuePair("redirect_uri","https://oauth2.example.com/code" ));
            nameValuePairs.add(new BasicNameValuePair("grant_type","authorization_code" ));
            nameValuePairs.add(new BasicNameValuePair("response_type","token" ));
            nameValuePairs.add(new BasicNameValuePair("access_type","offline" ));


            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());

           JSONObject basejson = new JSONObject(responseBody);
           //getting access token
            token=basejson.getString("access_token");
            //storing access token to the shared pref
            SaveSharedPrefrence.setAccessToken(c,token);
            //getting refreh token
            String refreshToken = basejson.optString("refresh_token");
            //setting ti to the shared  pref
            if(refreshToken!=null)
            SaveSharedPrefrence.setRefreshToken(c,refreshToken);


            Log.i(TAG, "Signed in as: " + responseBody +"        "+statusCode);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error sending ID token to backend.", e);
        } catch (IOException e) {
            Log.e(TAG, "Error sending ID token to backend.", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000 /* milliseconds */);

            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setRequestMethod("GET");

            Log.d(TAG, "makeHttpRequest: "+ SaveSharedPrefrence.getAuthCode(c));

            urlConnection.connect();



            // If the request was successful (response code 200),

            // then read the input stream and parse the response.

            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);

            } else {

                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());

            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);

        } finally {

            if (urlConnection != null) {

                urlConnection.disconnect();

            }

            if (inputStream != null) {

                // Closing the input stream could throw an IOException, which is why

                // the makeHttpRequest(URL url) method signature specifies than an IOException

                // could be thrown.

                inputStream.close();

            }

        }

        return jsonResponse;

    }



    /**

     * Convert the {@link InputStream} into a String which contains the

     * whole JSON response from the server.

     */

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {

                output.append(line);

                line = reader.readLine();

            }

        }

        return output.toString();

    }
}

package com.needle.soniaditi380.books;

import java.net.URL;

/**
 * Created by Aditya on 23-Jan-18.
 */

public class Book {

    /**id of the book**/

    private String mid;


    /**ratings of the book**/

    private double mRatings;



    /**Title of the book **/

    private String mTitle;


    /** Author of the book */

    private String mAuthor;

    /** Image resource ID for the book Cover */

    private URL mImageResourceUrl =null;


    /** description of the book */

    private String mdescription;

    /** pageCount of the book */

    private int mpageCount;

    /** category of the book */

    private String mcategory;

    /** rating count of the book */

    private int mratingCount;

    /** previewlink of the book */

    private URL mpreviewLink;


    /** price of the book */

    private double mPrice;


    /** CurrencyCode of the book */

    private String mCurrencyCode;


    /**

     * Constructs a new {@link Book} object.

     *

     * @param ratings  is the ratings of the book

     * @param title is the title of the book

     * @param author is the author of the book

     * @param coverImage is the cover of the book

     */

    public Book(double ratings, String  title, String author , URL coverImage, String id
                , String desc, int pgcount, String categ, int ratecount,URL preview,
                 double cost,String currencycode) {

        mRatings = ratings;

        mTitle = title;

        mAuthor = author;

        mImageResourceUrl = coverImage;

        mid= id;

        mdescription=desc;

        mpageCount=pgcount;

        mcategory=categ;

        mratingCount=ratecount;

        mpreviewLink=preview;

        mPrice=cost;

        mCurrencyCode=currencycode;


    }

    /**

     * Get the average ratings of book

     */

    public double getRatings() {

        return mRatings;

    }





    /**

     * Get the title of the book

     */

    public String getmTitle() {

        return mTitle;

    }



    /**

     * Get the Author of the book

     */

    public String getAuthor () {

        return mAuthor;

    }



    /**

     * Returns the Cover Image of the book.

     */

    public URL getImageResourceUrl() {

        return mImageResourceUrl;

    }

    /**

     * Returns whether or not there is an image for this word.

     */

    public boolean hasImage() {

        return mImageResourceUrl != null;

    }

    /**

     * Returns the id of the book

     */

    public String getmID() {

        return mid;

    }
        /**

         * Returns the description of the book

         */

    public String getmDescription() {

        return mdescription;

    }


    /**

     * Returns the pageCount of the book

     */

    public int getmPageCount() {

        return mpageCount;
    }



    /**

     * Returns the Category of the book

     */

    public String getmCategory() {

        return mcategory;
    }


    /**

     * Returns the rating Count of the book

     */

    public int getmRatingCount() {

        return mratingCount;
    }



    /**

     * Returns the previewlink of the book

     */

    public URL getmpreviewLink() {

        return mpreviewLink;
    }



    /**

     * Returns the Price of the book

     */

    public double getmPrice() {

        return mPrice;
    }

    /**

     * Returns the currency code for the price of the book

     */

    public String geCurrencyCode() {

        return mCurrencyCode;
    }
}

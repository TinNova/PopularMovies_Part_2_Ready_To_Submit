package com.example.tin.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.example.tin.popularmovies.Data.FavouritesContract.FavouritesEntry.TABLE_NAME;

/**
 * Created by Tin on 06/11/2017.
 */

public class FavouritesContentProvider extends ContentProvider {

    // Defining final integer constants for the directory of favouritesMovies and a single Item
    // It's convention to use 100, 200, 300 ect for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int FAVOURITEMOVIE = 100;
    public static final int FAVOURITEMOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Defining a static buildUriMatcher method that associates URI's with their int match
    public static UriMatcher buildUriMatcher() {
        // .NO_MATCH defines it as an empty uriMatcher (because we haven't added an int match yet
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add matches with addURI (String authority, String path, int code), this means we are adding the Uri
        // This is for an entire directory
        // content://com.example.tin.popularmovies/favouriteFilms/100
        uriMatcher.addURI(FavouritesContract.AUTHORITY, FavouritesContract.PATH_FAVOURITE_FILMS, FAVOURITEMOVIE);
        // This is for a single item
        // content://com.example.tin.popularmovies/favouriteFilms/2/101
        uriMatcher.addURI(FavouritesContract.AUTHORITY, FavouritesContract.PATH_FAVOURITE_FILMS + "/#", FAVOURITEMOVIE_WITH_ID);

        return uriMatcher;
    }

    private FavouritesDbHelper mFavouritesDbHelper;

    // Where you should initiliase everything you need to access your data, in this case it's the SQLite database
    @Override
    public boolean onCreate() {

        Context context = getContext();
        mFavouritesDbHelper = new FavouritesDbHelper(context);

        // Return true because the method is done
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        /** WE STILL DO NOT HAVE A QUERY FOR A SINGLE ITEM*/

        final SQLiteDatabase db = mFavouritesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {
            // Query for the favouriteMovies directory
            case FAVOURITEMOVIE:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            // Query for a single item in the directory, we don't need one as we are saving everything
            // into a Model List called FavouriteMovie, from here we can select individual items
            // HOWEVER see "Lesson 10, Video 26. Query for One Item" to learn more...

            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        // Initialise the SQLite database as a getWritableDatabase so we can insert data
        final SQLiteDatabase favouritesDB = mFavouritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAVOURITEMOVIE:
                // Inserting values into favourites SQLite table
                // This insert should return an id, if unsuccessful it will return -1
                // if successful, it should take the id and return a new uri for that new item
                long id = favouritesDB.insert(TABLE_NAME, null, contentValues);
                // if statement to check if the insert was successful
                if (id > 0) {
                    // success
                    returnUri = ContentUris.withAppendedId(
                            FavouritesContract.FavouritesEntry.CONTENT_URI, id);

                } else { // id is -1, therefore it failed
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Here we notify the resolver that the uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognise a single item
        final SQLiteDatabase db = mFavouritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int favouriteMoviesDeleted; // starts as 0

        // Write the code to delete a single row of data
        // Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FAVOURITEMOVIE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                favouriteMoviesDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (favouriteMoviesDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return favouriteMoviesDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
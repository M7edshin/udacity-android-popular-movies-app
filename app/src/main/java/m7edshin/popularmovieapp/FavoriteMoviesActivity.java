package m7edshin.popularmovieapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import m7edshin.popularmovieapp.CustomAdapters.MoviesCursorAdapter;
import m7edshin.popularmovieapp.InterfaceUtilities.RecyclerViewTouchListener;
import m7edshin.popularmovieapp.MoviesDatabase.DbSQLiteOpenHelper;

import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_POSTER_PATH;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_RATING;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_RELEASE_DATE;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_SYNOPSIS;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_TITLE;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.CONTENT_URI;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.TABLE_NAME;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry._ID;
import static m7edshin.popularmovieapp.Utilities.Constants.LOADER_MANAGER_ID;

public class FavoriteMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.rv_fav_movies) RecyclerView rv_fav_movies;

    private MoviesCursorAdapter moviesCursorAdapter;
    private DbSQLiteOpenHelper dbSQLiteOpenHelper;
    private LoaderManager loaderManager;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_fav_movies.setLayoutManager(layoutManager);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_MANAGER_ID, null, this);

        rv_fav_movies.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), rv_fav_movies, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(cursor.moveToPosition(position)){
                    int idColumnIndex = cursor.getColumnIndex(_ID);
                    String id = cursor.getString(idColumnIndex);
                    delete(id);
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    loaderManager.restartLoader(LOADER_MANAGER_ID, null, FavoriteMoviesActivity.this);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                _ID, COLUMN_TITLE,
                COLUMN_SYNOPSIS,
                COLUMN_POSTER_PATH,
                COLUMN_RATING,
                COLUMN_RELEASE_DATE};

        return new CursorLoader(this, CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        moviesCursorAdapter = new MoviesCursorAdapter(data);
        rv_fav_movies.setAdapter(moviesCursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesCursorAdapter = new MoviesCursorAdapter(null);
    }

    private void delete(String id){
        dbSQLiteOpenHelper = new DbSQLiteOpenHelper(this);
        SQLiteDatabase database = dbSQLiteOpenHelper.getReadableDatabase();
        database.delete(TABLE_NAME, _ID + "=?", new String[]{id});
        database.close();
    }
}

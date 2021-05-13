package com.example.books.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.example.books.adapters.BookAdapter;
import com.example.books.adapters.BookViewHolder;
import com.example.books.interfaces.IOnBookItemClickListener;
import com.example.books.models.Book;
import com.example.books.net.BookClient;
import com.google.android.material.card.MaterialCardView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BookActivity extends AppCompatActivity {
    //Variable declaration
    private static final String TAG = "BOOK ACTIVITY";
    public static final String BOOK_DETAIL_KEY = "book";
    private MaterialCardView noBooksCard;
    private RecyclerView booksRecyclerView;
    private RecyclerView.Adapter booksAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Book> bookData = new ArrayList<>();
    private IOnBookItemClickListener listener;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        noBooksCard = findViewById(R.id.no_trans_card_view);
        booksRecyclerView = findViewById(R.id.trans_recycler_view);
        mLayoutManager = new LinearLayoutManager(this); //this
        booksRecyclerView.setLayoutManager(mLayoutManager);
        progress = new ProgressDialog(this, R.style.MyAlertDialogStyle);

        boolean noTransactions = bookData.isEmpty();
        int transVisibility = noTransactions ? View.GONE : View.VISIBLE;
        int noTransVisibility = noTransactions ? View.VISIBLE : View.GONE;

        noBooksCard.setVisibility(noTransVisibility);
        booksRecyclerView.setVisibility(transVisibility);

        //Listener for item click in the image view
        listener = new IOnBookItemClickListener() {
            //Bookmark button click implementation
            @Override
            public void onBookmarkClick(Book book, BookViewHolder holder) {
                Toast.makeText(BookActivity.this, "Book marked", Toast.LENGTH_LONG)
                        .show();

                holder.bookmark.setBackgroundColor(holder.bookmark.getContext().getResources()
                        .getColor(R.color.black));
            }

            //image click implementation for book's detail view
            @Override
            public void onImageClick(Book book){
                Intent intent = new Intent(BookActivity.this, BookDetailActivity.class);
                intent.putExtra(BOOK_DETAIL_KEY, book);
                startActivity(intent);

            }
        };
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks(String query) {
        // Show progress bar before making network request
        showDialog();
        BookClient client = new BookClient();
        try {
            //Make network request
            client.getBooks(query, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode,headers,response);
                    try {
                        System.out.println("Request successful");
                        // hide progress bar
                        closeDialog();
                        JSONArray docs = null;
                        if (response != null) {
                            // Get the docs json array
                            docs = response.getJSONArray("docs");
                            // Parse json array into array of model objects
                            final ArrayList<Book> books = Book.fromJson(docs);

                            //Populating the recyclerview
                            booksAdapter = new BookAdapter(books, BookActivity.this, listener);
                            booksRecyclerView.setAdapter(booksAdapter);

                            noBooksCard.setVisibility(View.GONE);
                            booksRecyclerView.setVisibility(View.VISIBLE);
                            booksAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        // Invalid JSON format, show appropriate error.
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject responseString) {
                    super.onFailure(statusCode,headers,throwable, responseString);
                    System.out.println("Request failed");
                    closeDialog();
                    Toast.makeText(BookActivity.this,"An error occurred. Please try again.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }catch (Exception e){
            System.out.println("Error occured in book activity");
            e.printStackTrace();
            closeDialog();
            Toast.makeText(BookActivity.this,"An error occurred. Please try again.",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_book_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                fetchBooks(query);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set activity title to search query
                BookActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    private void closeDialog(){
        if(progress.isShowing()) progress.dismiss();
    }
}

package com.example.books.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.books.R;
import com.example.books.models.Book;
import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Fetch views
        ImageView ivBookCover = (ImageView) findViewById(R.id.ivBdBookCover);
        TextView tvTitle = (TextView) findViewById(R.id.tvBdTitle);
        TextView tvAuthor = (TextView) findViewById(R.id.tvBdAuthor);
        TextView tvBdPublished = (TextView) findViewById(R.id.tvBdPublishYear);
        TextView tvBdDescription = (TextView) findViewById(R.id.tvBdDescription);
        // Use the book to populate the data into our views
        Book book = (Book) getIntent().getSerializableExtra(BookActivity.BOOK_DETAIL_KEY);
        String publish = "Published in " + book.getPublication();
        String author = "Authured by " + book.getAuthor();
        this.setTitle(book.getTitle());
        // Populate data
        Picasso.with(BookDetailActivity.this).load(Uri.parse(book.getLargeCoverUrl()))
                .error(R.drawable.ic_nocover)
                .placeholder( R.drawable.progress_animation )
                .into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(author);
        tvBdPublished.setText(publish);
        tvBdDescription.setText(book.getDescription());
    }
}

package com.example.books.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.example.books.interfaces.IOnBookItemClickListener;
import com.example.books.models.Book;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

public class BookViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = "BookViewHolder";
    private final MaterialTextView author;
    private final MaterialTextView title;
    private final MaterialTextView published;
    public final ImageView cover;
    public final MaterialButton bookmark;
    public View mView;

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        author = itemView.findViewById(R.id.tvAuthor);
        title = itemView.findViewById(R.id.tvTitle);
        published = itemView.findViewById(R.id.tvPublication);
        cover = itemView.findViewById(R.id.ivBookCover);
        bookmark = itemView.findViewById(R.id.btnBookmark);
    }

    public void bindData(Book dm, Context context) {
        try {
            title.setText(dm.getTitle());
            author.setText(dm.getAuthor());
            published.setText(dm.getPublication());
            Picasso.with(context)
                    .load(Uri.parse(dm.getCoverUrl()))
                    .placeholder(R.drawable.circle_loading_black)
                    .fit()
                    .error(R.drawable.ic_nocover)
                    .into(cover);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

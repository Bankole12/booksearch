package com.example.books.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.example.books.interfaces.IOnBookItemClickListener;
import com.example.books.models.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
    // View lookup cache
    private ArrayList<Book> dataModelList;
    private Context mContext;
    private final IOnBookItemClickListener listener;

    public BookAdapter(ArrayList<Book> modelList, Context context, IOnBookItemClickListener listener) {
        dataModelList = modelList;
        mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        // Return a new view holder

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, final int position) {
        // Bind data for the item at position
        holder.bookmark.setOnClickListener(v -> listener.onBookmarkClick(dataModelList.get(position),
                holder));

        holder.cover.setOnClickListener(v -> listener.onImageClick(dataModelList.get(position)));

        holder.bindData(dataModelList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items

        return dataModelList == null ? 0 : dataModelList.size();
    }
}

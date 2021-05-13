package com.example.books.interfaces;

import com.example.books.adapters.BookViewHolder;
import com.example.books.models.Book;

public interface IOnBookItemClickListener {
    void onBookmarkClick(Book item, BookViewHolder holder);
    void onImageClick(Book book);
}

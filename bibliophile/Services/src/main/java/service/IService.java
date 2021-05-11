package service;

import domain.Book;
import domain.Librarian;
import domain.Reader;

public interface IService extends Observable
{
    public Librarian loginLibrarian(String name, String password, String pic);
    public Reader loginReader(String name, String password, String pic);
    Iterable<Book> getBooks();
    Iterable<Book> getBooksForBorrowing();
}

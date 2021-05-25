package service;

import domain.*;

public interface IService extends Observable
{
    public Librarian loginLibrarian(String name, String password, String pic);
    public Reader loginReader(String name, String password, String pic);
    Iterable<Book> getBooks();
    Iterable<Loan> getLoans();
    Iterable<Book> getBooksForBorrowing();
    boolean borrowBook(Loan loan);
    Iterable<Return> getMyBooks(Reader reader);
    void returnLoan(Return ret);
}

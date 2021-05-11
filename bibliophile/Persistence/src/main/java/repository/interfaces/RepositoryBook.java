package repository.interfaces;
import domain.Book;


public interface RepositoryBook extends Repository<Long, Book>
{
    Iterable<Book> findBooksForBorrowing();
}

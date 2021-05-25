package repository.interfaces;
import domain.Book;
import domain.Return;

public interface RepositoryReturn extends Repository<Long, Return>
{
    Return findReturnByLoan(Long id);
}

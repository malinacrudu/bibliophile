package repository.interfaces;
import domain.Loan;
import domain.Return;


public interface RepositoryLoan extends Repository<Long, Loan>
{
    Iterable<Loan> getReturnedLoans(Long idReader);

    Iterable<Loan> findAllUnreturned();
}

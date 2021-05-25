package repository.hbm_repos;
import domain.Book;
import domain.Loan;
import domain.Return;
import domain.validators.Validator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.interfaces.RepositoryLoan;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository implements RepositoryLoan
{

    private static SessionFactory sessionFactory;
    private final Validator<Loan> validator;


    public LoanRepository(Validator<Loan> validator, SessionFactory sessionFactory)
    {
        this.validator = validator;
        LoanRepository.sessionFactory = sessionFactory;
    }

    static void close()
    {
        if ( sessionFactory != null )
        {
            sessionFactory.close();
        }
    }


    @Override
    public Loan findOne(Long aLong)
    {

        Loan loan = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                loan = session.createQuery("FROM Loan WHERE id = "+aLong, Loan.class).setMaxResults(1).uniqueResult();
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return loan;
    }

    @Override
    public Iterable<Loan> findAll()
    {
        List<Loan> loans = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                loans = session.createQuery("FROM Loan", Loan.class).list();
                System.out.println( loans.size() + " loan(s) found:" );
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return loans;
    }


    @Override
    public void save(Loan entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(Long aLong)
    {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                Loan loan= session.createQuery("FROM Loan WHERE id="+aLong, Loan.class).setMaxResults(1).uniqueResult();
                System.out.println("Delete the loan"+loan);
                session.delete(loan);
                tx.commit();
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(Loan entity) {

    }


    @Override
    public Iterable<Loan> getReturnedLoans(Long idReader)
    {
        List<Loan> loans = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                loans = session.createQuery("FROM Loan WHERE returned=:t AND readerId="+idReader, Loan.class).setParameter("t",true).list();
                System.out.println( loans.size() + " loan(s) found:" );
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                System.out.println(ex);
                if (tx!=null)
                    tx.rollback();
            }
        }
        return loans;
    }

    @Override
    public Iterable<Loan> findAllUnreturned()
    {
        List<Loan> loans = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                loans = session.createQuery("FROM Loan WHERE returned=:t", Loan.class).setParameter("t",false).list();
                System.out.println( loans.size() + " loan(s) found:" );
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return loans;
    }
}


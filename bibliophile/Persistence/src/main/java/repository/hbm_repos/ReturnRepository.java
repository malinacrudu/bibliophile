package repository.hbm_repos;
import domain.Book;
import domain.Return;
import domain.validators.Validator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.interfaces.RepositoryReturn;
import java.util.ArrayList;
import java.util.List;

public class ReturnRepository implements RepositoryReturn
{

    private static SessionFactory sessionFactory;
    private final Validator<Return> validator;


    public ReturnRepository(Validator<Return> validator, SessionFactory sessionFactory)
    {
        this.validator = validator;
        ReturnRepository.sessionFactory = sessionFactory;
    }

    static void close()
    {
        if ( sessionFactory != null )
        {
            sessionFactory.close();
        }
    }


    @Override
    public Return findOne(Long aLong)
    {

        Return ret = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                ret = session.createQuery("FROM Return WHERE id = "+aLong, Return.class).setMaxResults(1).uniqueResult();
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return ret;
    }

    @Override
    public Iterable<Return> findAll()
    {
        System.out.println("BDUIQWBDOIQ");
        List<Return> returns = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                returns = session.createQuery("FROM Return", Return.class).list();
                System.out.println( returns.size() + " return(s) found:" );
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                System.out.println(ex);
                if (tx!=null)
                    tx.rollback();
            }
        }

        return returns;
    }


    @Override
    public void save(Return entity)
    {
        try(Session session = sessionFactory.openSession())
        {
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
                Return ret= session.createQuery("FROM Return WHERE id="+aLong, Return.class).setMaxResults(1).uniqueResult();
                System.out.println("Delete the ret"+ret);
                session.delete(ret);
                tx.commit();
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(Return entity) {

    }


    @Override
    public Return findReturnByLoan(Long id)
    {
        Return ret = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                ret = session.createQuery("FROM Return WHERE loanId= "+id, Return.class).setMaxResults(1).uniqueResult();
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        System.out.println(ret);
        return ret;
    }

}


package repository.hbm_repos;
import domain.Reader;
import domain.User;
import domain.validators.Validator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.interfaces.RepositoryReader;

import java.util.ArrayList;
import java.util.List;

public class ReaderRepository implements RepositoryReader
{

    private static SessionFactory sessionFactory;
    private final Validator<User> validator;


    public ReaderRepository(Validator<User> validator, SessionFactory sessionFactory)
    {
        this.validator = validator;
        ReaderRepository.sessionFactory = sessionFactory;
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }


    @Override
    public Reader findOne(Long aLong)
    {

        Reader reader = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                reader = session.createQuery("FROM Reader WHERE id = "+aLong, Reader.class).setMaxResults(1).uniqueResult();
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return reader;
    }

    @Override
    public Iterable<Reader> findAll()
    {
        List<Reader> readers = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                readers = session.createQuery("FROM Reader", Reader.class).list();
                System.out.println( readers.size() + " reader(s) found:" );
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return readers;
    }


    @Override
    public void save(Reader entity)
    {
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
                Reader reader= session.createQuery("FROM Reader WHERE id="+aLong, Reader.class).setMaxResults(1).uniqueResult();
                System.out.println("Delete the reader"+reader);
                session.delete(reader);
                tx.commit();
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(Reader entity) {

    }



    @Override
    public Reader findReaderByPic(String pic)
    {

        Reader reader= null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                reader = session.createQuery("FROM Reader WHERE pic = "+"'"+pic+"'", Reader.class).setMaxResults(1).uniqueResult();
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return reader;
    }
}

package repository.hbm_repos;
import domain.Librarian;
import domain.User;
import domain.validators.Validator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.interfaces.RepositoryLibrarian;

import java.util.ArrayList;
import java.util.List;


public class LibrarianRepository implements RepositoryLibrarian
{

    private static SessionFactory sessionFactory;
    private final Validator<User> validator;


    public LibrarianRepository(Validator<User> validator,SessionFactory sessionFactory)
    {
        this.validator = validator;
        LibrarianRepository.sessionFactory = sessionFactory;
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }


    @Override
    public Librarian findOne(Long aLong)
    {

        Librarian librarian = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                librarian = session.createQuery("FROM Librarian WHERE id = "+aLong, Librarian.class).setMaxResults(1).uniqueResult();
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return librarian;
    }

    @Override
    public Iterable<Librarian> findAll()
    {
        List<Librarian> librarians = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                librarians = session.createQuery("FROM Librarian", Librarian.class).list();
                System.out.println( librarians.size() + " librarian(s) found:" );
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return librarians;
    }


    @Override
    public void save(Librarian entity) {
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
                Librarian librarian= session.createQuery("FROM Librarian WHERE id="+aLong, Librarian.class).setMaxResults(1).uniqueResult();
                System.out.println("Delete the librarian"+librarian);
                session.delete(librarian);
                tx.commit();
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(Librarian entity)
    {

    }


    @Override
    public Librarian findLibrarianByPic(String pic)
    {
        Librarian librarian = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                librarian = session.createQuery("FROM Librarian WHERE pic = "+pic, Librarian.class).setMaxResults(1).uniqueResult();
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return librarian;
    }
}

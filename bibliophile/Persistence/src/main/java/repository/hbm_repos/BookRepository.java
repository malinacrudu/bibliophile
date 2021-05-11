package repository.hbm_repos;
import domain.Book;
import domain.validators.Validator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.interfaces.RepositoryBook;
import java.util.ArrayList;
import java.util.List;

public class BookRepository implements RepositoryBook
{

    private static SessionFactory sessionFactory;
    private final Validator<Book> validator;


    public BookRepository(Validator<Book> validator, SessionFactory sessionFactory)
    {
        this.validator = validator;
        BookRepository.sessionFactory = sessionFactory;
    }

    static void close()
    {
        if ( sessionFactory != null )
        {
            sessionFactory.close();
        }
    }


    @Override
    public Book findOne(Long aLong)
    {

        Book book = null;
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try
            {
                tx = session.beginTransaction();
                book = session.createQuery("FROM Book WHERE id = "+aLong, Book.class).setMaxResults(1).uniqueResult();
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return book;
    }

    @Override
    public Iterable<Book> findAll()
    {
        List<Book> books = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                books = session.createQuery("FROM Book", Book.class).list();
                System.out.println( books.size() + " librarian(s) found:" );
                tx.commit();
            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return books;
    }


    @Override
    public void save(Book entity) {
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
                Book book= session.createQuery("FROM Book WHERE id="+aLong, Book.class).setMaxResults(1).uniqueResult();
                System.out.println("Delete the book"+book);
                session.delete(book);
                tx.commit();
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void update(Book entity)
    {

    }

    @Override
    public Iterable<Book> findBooksForBorrowing()
    {
        List<Book> books = new ArrayList<>();
        try(Session session = sessionFactory.openSession())
        {

            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                books = session.createQuery("FROM Book WHERE already_borrowed=:t", Book.class).setParameter("t",false).list();
                System.out.println( books.size() + " book(s) found:" );
                tx.commit();

            }
            catch(RuntimeException ex)
            {
                if (tx!=null)
                    tx.rollback();
            }
        }
        return books;
    }
}


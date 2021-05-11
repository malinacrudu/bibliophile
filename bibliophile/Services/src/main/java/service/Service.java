package service;
import domain.Book;
import domain.Librarian;
import domain.Reader;
import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.interfaces.RepositoryBook;
import repository.interfaces.RepositoryLibrarian;
import repository.interfaces.RepositoryReader;
import java.util.ArrayList;
import java.util.List;

public class Service implements IService
{

    private RepositoryLibrarian librarianRepository;
    private RepositoryReader readerRepository;
    private RepositoryBook bookRepository;

    private static final Logger logger= LogManager.getLogger();

    public Service( RepositoryLibrarian librarianRepository, RepositoryReader readerRepository,  RepositoryBook bookRepository)
    {
       this.bookRepository = bookRepository;
       this.librarianRepository = librarianRepository;
       this.readerRepository = readerRepository;
       logger.info("Initializing service");
    }


    private List<Observer> observers = new ArrayList<>();


    @Override
    public void addObserver(Observer o) {
       if(o!=null) observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
            observers.remove(o);
    }

    @Override
    public Librarian loginLibrarian(String name, String password, String pic)
    {
        logger.traceEntry();
        if(name==null||password==null||pic==null)
        {
            logger.error("The credentials cannnot be null!");
            throw new IllegalArgumentException("The credentials cannnot be null!");
        }
        Librarian librarian = librarianRepository.findLibrarianByPic(pic);
        if(librarian==null)
        {
            logger.error("This user does not exist! Try another one!");
            throw new IllegalArgumentException("This user does not exist! Try another one!");
        }
        if(!librarian.getPassword().equals(password))
        {
            logger.error("Incorrect password!");
            throw new IllegalArgumentException("Incorrect password!");
        }
        if(!librarian.getName().equals(name))
        {
            logger.error("Incorrect name!");
            throw new IllegalArgumentException("Incorrect name!");
        }
        logger.traceExit(librarian);
        return librarian;
    }

    @Override
    public Reader loginReader(String name, String password, String pic)
    {
        logger.traceEntry();
        if(name==null||password==null||pic==null)
        {
            logger.error("The credentials cannnot be null!");
            throw new IllegalArgumentException("The credentials cannnot be null!");
        }
        System.out.println(name+password+pic);
        Reader reader = readerRepository.findReaderByPic(pic);
        if(reader==null)
        {
            logger.error("This user does not exist! Try another one!");
            throw new IllegalArgumentException("This user does not exist! Try another one!");
        }
        if(!reader.getPassword().equals(password))
        {
            logger.error("Incorrect password!");
            throw new IllegalArgumentException("Incorrect password!");
        }
        if(!reader.getName().equals(name))
        {
            logger.error("Incorrect name!");
            throw new IllegalArgumentException("Incorrect name!");
        }
        logger.traceExit(reader);
        return reader;
    }

    @Override
    public Iterable<Book> getBooks()
    {
        logger.traceEntry();
        Iterable<Book> books= bookRepository.findAll();
        logger.traceExit();
        return books;
    }


    @Override
    public Iterable<Book> getBooksForBorrowing()
    {
        logger.traceEntry();
        Iterable<Book> books= bookRepository.findBooksForBorrowing();
        logger.traceExit();
        return books;
    }
}

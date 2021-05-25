package service;
import domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.interfaces.*;
import java.util.ArrayList;
import java.util.List;

public class Service implements IService
{

    private RepositoryLibrarian librarianRepository;
    private RepositoryReader readerRepository;
    private RepositoryBook bookRepository;
    private RepositoryLoan loanRepository;
    private RepositoryReturn returnRepository;
    private List<Observer> observers = new ArrayList<>();
    private static final Logger logger= LogManager.getLogger();

    public Service( RepositoryLibrarian librarianRepository, RepositoryReader readerRepository,  RepositoryBook bookRepository, RepositoryLoan loanRepository, RepositoryReturn returnRepository)
    {
       this.bookRepository = bookRepository;
       this.librarianRepository = librarianRepository;
       this.readerRepository = readerRepository;
       this.loanRepository = loanRepository;
       this.returnRepository = returnRepository;
       logger.info("Initializing service");
    }


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
    public Iterable<Loan> getLoans()
    {
        logger.traceEntry();
        Iterable<Loan> loans= loanRepository.findAllUnreturned();
        for(Loan l : loans)
        {
            Book book = bookRepository.findOne(l.getBookId());
            Reader reader = readerRepository.findOne(l.getReaderId());
            l.setBook(book);
            l.setReader(reader);
        }
        logger.traceExit();
        return loans;
    }


    @Override
    public Iterable<Book> getBooksForBorrowing()
    {
        logger.traceEntry();
        Iterable<Book> books= bookRepository.findBooksForBorrowing();
        logger.traceExit();
        return books;
    }

    @Override
    public boolean borrowBook(Loan loan)
    {
        Book book = loan.getBook();
        Book found = bookRepository.findOne(book.getId());
        if(found.getAlready_borrowed())
            return false;
        else
            {
            found.setAlready_borrowed(true);
            bookRepository.update(found);
            loan.setBook(found);
            loanRepository.save(loan);
            for(Observer o : observers)
            {
                o.update();
            }
            return  true;
           }
    }

    @Override
    public Iterable<Return> getMyBooks(Reader reader)
    {
        ArrayList<Return> returns = new ArrayList<>();
        Iterable<Loan> loans = loanRepository.getReturnedLoans(reader.getId());
        System.out.println(loans);
        for (Loan loan : loans)
        {
            Book book = bookRepository.findOne(loan.getBookId());
            loan.setBook(book);
            Return ret = returnRepository.findReturnByLoan(loan.getId());
            ret.setLoan(loan);
            returns.add(ret);
        }
        return returns;
    }

    @Override
    public void returnLoan(Return ret)
    {
        try {
            returnRepository.save(ret);
            Loan loan = ret.getLoan();
            loan.setReturned(true);
            loanRepository.update(loan);
            Book book = ret.getLoan().getBook();
            book.setAlready_borrowed(false);
            bookRepository.update(book);
            for(Observer o : observers)
            {
                o.update();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

}

package rpcprotocol;
import domain.Book;
import domain.Librarian;
import domain.Reader;
import service.IService;
import service.Observer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServicesRpcProxy implements IService
{
    private String host;
    private int port;

    private List<Observer> clients= new ArrayList<>();

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;


    public ServicesRpcProxy(String host, int port)
    {
        this.host = host;
        this.port = port;
        this.clients= new ArrayList<>();
        qresponses=new LinkedBlockingQueue<Response>();
    }



    private void closeConnection()
    {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            clients = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        }
        catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }

    private Response readResponse() throws Exception 
	{
        Response response=null;
        try{

            response=qresponses.take();

        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection()
    {
        try
        {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response)
    {
        for (Observer o : clients)
            o.update();
    }

    private boolean isUpdate(Response response)
    {
        return response.type()== ResponseType.UPDATE;
    }

    @Override
    public Librarian loginLibrarian(String name, String password, String pic)
    {
        initializeConnection();
        Librarian librarian = new Librarian(name,password,pic);
        Request request = new Request.Builder().type(RequestType.LOGIN_LIBRARIAN).data(librarian).build();
        try {
            sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response response = null;
        try
        {
            response = readResponse();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(response.type() == ResponseType.OK)
        {
            librarian = (Librarian) response.data();
            return librarian;
        }
        closeConnection();
        throw new IllegalArgumentException(response.data().toString());
    }

    @Override
    public Reader loginReader(String name, String password, String pic)
    {
        initializeConnection();
        Reader reader= new Reader(name,password,pic);
        Request request = new Request.Builder().type(RequestType.LOGIN_READER).data(reader).build();
        try {
            sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response response = null;
        try
        {
            response = readResponse();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(response.type() == ResponseType.OK)
        {
            reader = (Reader) response.data();
            return reader;
        }
        closeConnection();
        throw new IllegalArgumentException(response.data().toString());
    }

    @Override
    public Iterable<Book> getBooks()
    {
        Request request = new Request.Builder().type(RequestType.GET_BOOKS).build();
        try
        {
            sendRequest(request);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Response response = null;
        try
        {
            response= readResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(response.type() == ResponseType.OK)
        {
            Iterable<Book> books = (Iterable<Book>) response.data();
            return books;
        }
        return null;
    }

    @Override
    public Iterable<Book> getBooksForBorrowing()
    {
        Request request = new Request.Builder().type(RequestType.GET_BOOKS_FOR_BORROWING).build();
        try
        {
            sendRequest(request);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        Response response = null;
        try
        {
            response= readResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(response.type() == ResponseType.OK)
        {
            Iterable<Book> books = (Iterable<Book>) response.data();
            return books;
        }
        return null;
    }


    @Override
    public void addObserver(Observer o)
    {
        System.out.println("OBSERVER:"+o);
        System.out.println("CLIENTS:"+clients);
        if(o!=null) clients.add(o);
        System.out.println("CLIENTS:"+clients);
    }

    @Override
    public void removeObserver(Observer o)
    {
        clients.clear();
        Request request = new Request.Builder().type(RequestType.LOGOUT).build();
        try
            {
                sendRequest(request);
            }
        catch (Exception e)
            {
                e.printStackTrace();
            }
        Response response = null;
        try
            {
                response= readResponse();
            }
        catch (Exception e)
            {
                e.printStackTrace();
            }
        if(response.type()==ResponseType.ERROR)
                throw new IllegalArgumentException(response.data().toString());

        closeConnection();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response))
                    {
                        handleUpdate((Response)response);
                    }
                    else {

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}

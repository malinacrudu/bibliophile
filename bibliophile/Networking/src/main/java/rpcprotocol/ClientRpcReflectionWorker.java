package rpcprotocol;
import domain.*;
import service.Observer;
import service.Service;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;


public class ClientRpcReflectionWorker implements Runnable, Observer
{
    private Service server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(Service server, Socket connection)
    {
        this.server = server;
        this.connection = connection;
        server.addObserver(this);
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
                Thread.sleep(1000);
             }
                catch (Exception e)
                {
                e.printStackTrace();
                connected=false;
                }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }


    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();

    private Response handleRequest(Request request){
        Response response=null;
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException
    {
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    private Response handleLOGIN_READER(Request request)
    {

        try
        {
            Reader reader = (Reader) request.data();
            reader = server.loginReader(reader.getName(), reader.getPassword(), reader.getPic());
            Response response = new Response.Builder().type(ResponseType.OK).data(reader).build();
            return response;
        }
       catch (Exception e)
       {
           return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
       }
    }

    private Response handleLOGIN_LIBRARIAN(Request request)
    {

        try
        {
            Librarian librarian = (Librarian) request.data();
            librarian = server.loginLibrarian(librarian.getName(), librarian.getPassword(), librarian.getPic());
            Response response = new Response.Builder().type(ResponseType.OK).data(librarian).build();
            return response;
        }
        catch (Exception e)
        {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request)
    {
        try
        {
            server.removeObserver(this);
            Response response = new Response.Builder().type(ResponseType.OK).build();
            return response;
        }
        catch (Exception e)
        {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_BOOKS(Request request)
    {
        try
        {
            Iterable<Book> bookIterable = server.getBooks();
            Response response = new Response.Builder().type(ResponseType.OK).data(bookIterable).build();
            return response;
        }
        catch (Exception e)
        {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleGET_LOANS(Request request)
    {
        try
        {
            Iterable<Loan> loanIterable = server.getLoans();
            Response response = new Response.Builder().type(ResponseType.OK).data(loanIterable).build();
            return response;
        }
        catch (Exception e)
        {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_MY_BOOKS(Request request)
    {
        try
        {
            Reader reader = (Reader) request.data();
            Iterable<Return> myReturns = server.getMyBooks(reader);
            Response response = new Response.Builder().type(ResponseType.OK).data(myReturns).build();
            return response;
        }
        catch (Exception e)
        {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_BOOKS_FOR_BORROWING(Request request)
    {
        try
        {
            Iterable<Book> booksForBorrowing = server.getBooksForBorrowing();
            Response response = new Response.Builder().type(ResponseType.OK).data(booksForBorrowing).build();
            return response;
        }
        catch (Exception e)
        {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleBORROW_BOOK(Request request)
    {
        try
        {
            Loan loan = (Loan) request.data();
            boolean ok = server.borrowBook(loan);
            Response response;
            if (ok)
                response = new Response.Builder().type(ResponseType.OK).data(ok).build();
            else
                response = new Response.Builder().type(ResponseType.NO).data(ok).build();
            return response;
        }
        catch (Exception e)
        {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    @Override
    public void update()
    {
        try
        {
            sendResponse(new Response.Builder().type(ResponseType.UPDATE).build());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

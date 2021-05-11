import domain.validators.ValidatorBook;
import domain.validators.ValidatorUser;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.hbm_repos.BookRepository;
import repository.hbm_repos.LibrarianRepository;
import repository.hbm_repos.ReaderRepository;
import repository.interfaces.RepositoryBook;
import repository.interfaces.RepositoryLibrarian;
import repository.interfaces.RepositoryReader;
import service.Service;
import utils.AbstractServer;
import utils.RpcConcurrentServer;
import utils.ServerException;
import java.io.IOException;
import java.util.Properties;

public class StartRpcServer
{
    private static int defaultPort=55555;
    public static void main(String[] args)
    {

        Properties serverProps=new Properties();
        try
        {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set.");
            serverProps.list(System.out);
        }
        catch (IOException e)
        {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        SessionFactory s = initialize();
        RepositoryLibrarian repositoryLibrarian=new LibrarianRepository(new ValidatorUser(), s);
        RepositoryReader repositoryReader=new ReaderRepository(new ValidatorUser(), s);
        RepositoryBook repositoryBook=new BookRepository(new ValidatorBook(), s);

        Service service = new Service(repositoryLibrarian,repositoryReader,repositoryBook);
        int serverPort=defaultPort;
        try
        {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }
        catch (NumberFormatException nef)
        {
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, service);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server:" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server:"+e.getMessage());
            }
        }
    }

    static SessionFactory initialize()
    {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("h.xml") // configures settings from h.xml
                .build();
        try {
            return new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
        return null;
    }

}

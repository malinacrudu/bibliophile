import controller.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rpcprotocol.ServicesRpcProxy;
import service.IService;
import java.io.IOException;
import java.util.Properties;


public class StartRpcClient extends Application {
    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Properties clientProps=new Properties();
        try {
            clientProps.load(StartRpcClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties "+e);
            return;
        }
        String serverIP=clientProps.getProperty("server.host",defaultServer);
        int serverPort=defaultChatPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+defaultChatPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);
        IService server = new ServicesRpcProxy(serverIP, serverPort);
        try{

            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(StartRpcClient.class.getResource("/view/start.fxml"));
            BorderPane root=loader.load();
            primaryStage.initStyle(StageStyle.DECORATED);
            StartController startController = loader.getController();
            primaryStage.setScene(new Scene(root, 800, 800, Color.TRANSPARENT));
            startController.setService(server,primaryStage);
            primaryStage.show();
        }
        catch(Exception e)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app "+e);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}

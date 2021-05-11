package controller;
import domain.Librarian;
import domain.Reader;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.IService;

public class AuthenticationController
{

    public TextField NameField;
    public TextField PicField;
    public PasswordField PasswordField;
    private IService service;

    Stage stage;
    Stage old_stage;
    String function;

    public void setService(IService service,Stage old_stage,Stage stage, String function)
    {
        this.service=service;
        this.stage=stage;
        this.old_stage=old_stage;
        this.function = function;
    }
    public void log_in(ActionEvent actionEvent)
    {

        try
        {
            String name= NameField.getText();
            String password= PasswordField.getText();
            String pic= PicField.getText();
            NameField.clear();
            PasswordField.clear();
            PicField.clear();
            FXMLLoader loader = new FXMLLoader();
            if(this.function.equals("librarian"))
            {
                Librarian librarian = service.loginLibrarian(name,password,pic);
                loader.setLocation(getClass().getResource("/view/loggedin_librarian.fxml"));
                BorderPane root = loader.load();
                Stage loggedInStage = new Stage();
                loggedInStage.setWidth(800);
                loggedInStage.setHeight(800);
                loggedInStage.initStyle(StageStyle.DECORATED);
                LoggedInControllerLibrarian loggedInControllerLibrarian = loader.getController();
                loggedInStage.setScene(new Scene(root, 800, 800, Color.TRANSPARENT));
                loggedInControllerLibrarian.setService(service, this.stage, loggedInStage, librarian);
                this.stage.hide();
                loggedInStage.show();
            }
            else {
                Reader reader = service.loginReader(name,password,pic);
                loader.setLocation(getClass().getResource("/view/loggedin_reader.fxml"));
                BorderPane root = loader.load();
                Stage loggedInStage = new Stage();
                loggedInStage.setWidth(800);
                loggedInStage.setHeight(800);
                loggedInStage.initStyle(StageStyle.DECORATED);
                LoggedInControllerReader loggedInControllerReader = loader.getController();
                loggedInStage.setScene(new Scene(root, 800, 800, Color.TRANSPARENT));
                loggedInControllerReader.setService(service, this.stage, loggedInStage, reader);
                this.stage.hide();
                loggedInStage.show();
            }

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.setResizable(true);
            alert.show();
        }
    }
}

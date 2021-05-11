package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.IService;

public class StartController
{
    private IService service;
    Stage stage;

    public void setService(IService service,Stage stage)
    {
        this.service=service;
        this.stage=stage;
    }

    public void log_in_librarian(ActionEvent actionEvent)
    {

            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/authentication.fxml"));
                BorderPane root = loader.load();
                Stage authentificationStage = new Stage();
                authentificationStage.initStyle(StageStyle.DECORATED);
                AuthenticationController authenticationController = loader.getController();
                authentificationStage.setScene(new Scene(root, 800, 800, Color.TRANSPARENT));
                authenticationController.setService(service, this.stage, authentificationStage, "librarian");
                this.stage.hide();
                authentificationStage.show();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                alert.setResizable(true);
                alert.show();
            }
    }

    public void log_in_reader(ActionEvent actionEvent)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/authentication.fxml"));
            BorderPane root = loader.load();
            Stage authentificationStage = new Stage();
            authentificationStage.initStyle(StageStyle.DECORATED);
            AuthenticationController authenticationController = loader.getController();
            authentificationStage.setScene(new Scene(root, 800, 800, Color.TRANSPARENT));
            authenticationController.setService(service, this.stage, authentificationStage,"reader");
            this.stage.hide();
            authentificationStage.show();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.setResizable(true);
            alert.show();
        }
    }
}

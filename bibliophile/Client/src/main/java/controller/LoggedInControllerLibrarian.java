package controller;
import domain.Librarian;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.IService;

public class LoggedInControllerLibrarian
{
    public Label labelName;
    public Tab loansTab;
    private IService service;
    Stage stage;
    Stage old_stage;
    Librarian librarian;

    public void setService(IService service,Stage old_stage,Stage stage,Librarian librarian)
    {
        this.service=service;
        this.stage=stage;
        this.old_stage= old_stage;
        this.librarian= librarian;
        initConnections();
    }

    private void initConnections()
    {
        try
        {
            labelName.setText("Name: "+librarian.getName());
            labelName.setFont(Font.font("Century Gothic", 13));
            labelName.setTextFill(Paint.valueOf("#B97D60"));

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/loans.fxml"));
            BorderPane root;
            root = loader.load();
            Stage loansStage = new Stage();
            loansStage.initStyle(StageStyle.DECORATED);
            LoansController loansController= loader.getController();
            loansStage.setScene(new Scene(root, 800, 800, Color.TRANSPARENT));
            loansController.setService(service, librarian);
            loansTab.setContent(loansStage.getScene().getRoot());

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.setResizable(true);
            alert.show();
            e.printStackTrace();
        }
    }

    public void log_out(ActionEvent actionEvent)
    {
    }
}

package controller;
import domain.Reader;
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

public class LoggedInControllerReader
{

    public Label labelName;
    public Tab discoverBooksTab;
    public Tab myBooksTab;
    private IService service;
    Stage stage;
    Stage old_stage;
    Reader reader;

    public void setService(IService service, Stage old_stage, Stage stage, Reader reader)
    {
        this.service=service;
        this.stage=stage;
        this.old_stage= old_stage;
        this.reader= reader;
        initConnections();
    }

    public void initConnections()
    {
        try
        {
            labelName.setText("Name: "+reader.getName());
            labelName.setFont(Font.font("Century Gothic", 13));
            labelName.setTextFill(Paint.valueOf("#B97D60"));

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/borrow&discover.fxml"));
            BorderPane root;
            root = loader.load();
            Stage discoverStage = new Stage();
            discoverStage.initStyle(StageStyle.DECORATED);
            DiscoverController discoverController= loader.getController();
            discoverStage.setScene(new Scene(root, 800, 800, Color.TRANSPARENT));
            discoverController.setService(service, reader);
            discoverBooksTab.setContent(discoverStage.getScene().getRoot());

            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/mybooks.fxml"));
            root= loader.load();
            Stage myBooksStage = new Stage();
            myBooksStage.initStyle(StageStyle.DECORATED);
            MyBooksController myBooksController= loader.getController();
            myBooksStage.setScene(new Scene(root, 800, 800, Color.TRANSPARENT));
            myBooksController.setService(service, reader);
            myBooksTab.setContent(myBooksStage.getScene().getRoot());

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.setResizable(true);
            alert.show();
            e.printStackTrace();
        }
    }

    public void log_out(ActionEvent actionEvent) {
    }
}

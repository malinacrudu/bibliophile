package controller;
import domain.Librarian;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.IService;

public class LoggedInControllerLibrarian
{
    public Label labelName;
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
    }

    public void log_out(ActionEvent actionEvent) {
    }
}

package controller;
import domain.Librarian;
import domain.Loan;
import domain.Return;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import service.IService;
import service.Observer;

import java.time.LocalDate;


public class LoansController implements Observer
{
    public TableView<Loan> tableView;
    public TableColumn<Loan,String> tableColumnId;
    public TableColumn<Loan,String> tableColumnName;
    public TableColumn<Loan,String> tableColumnAuthor;
    public TableColumn<Loan,String> tableColumnTitle;
    public TableColumn<Loan, LocalDate> tableColumnDateB;
    public TableColumn<Loan,String> tableColumnISBN;

    ObservableList<Loan> model = FXCollections.observableArrayList();
    Librarian librarian;
    private IService service;

    public void setService(IService service, Librarian librarian)
    {
        this.service = service;
        this.librarian = librarian;
        this.service.addObserver(this);
        initModel();
    }
    @FXML
    public void initialize()
    {
        tableColumnId.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getReader().getPic()));
        tableColumnName.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getReader().getName()));
        tableColumnTitle.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getBook().getTitle()));
        tableColumnAuthor.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getBook().getAuthor()));
        tableColumnISBN.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getBook().getIsbn()));
        tableColumnDateB.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getLoanDate()));
        tableView.setItems(model);
    }

    private void initModel()
    {
        model.clear();
        Iterable<Loan> loans = service.getLoans();
        loans.forEach(loan->model.add(loan));
    }

    public void return_book(ActionEvent actionEvent)
    {
        Loan loan = tableView.getSelectionModel().getSelectedItem();
        System.out.println(loan);
        try
        {
            Return ret = new Return(librarian,loan,LocalDate.now());
            service.returnLoan(ret);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
            alert.setResizable(true);
            alert.show();
        }
    }

    @Override
    public void update() {
        Platform.runLater(()->{model.clear();model.clear();initModel();});
    }
}

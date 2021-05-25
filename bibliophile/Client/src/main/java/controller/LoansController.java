package controller;

import domain.Librarian;
import domain.Loan;
import domain.Reader;
import domain.Return;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import service.IService;

import java.time.LocalDate;
import java.util.Locale;

public class LoansController
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

    public void return_book(ActionEvent actionEvent) {
    }
}

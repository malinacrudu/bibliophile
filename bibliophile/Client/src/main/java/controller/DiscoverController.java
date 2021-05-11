package controller;
import domain.Book;
import domain.Reader;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import service.IService;
import java.io.File;

public class DiscoverController {

    public TableColumn<Book, ImageView> tableColumnImg;
    public TableColumn<Book,String> tableColumnIsbn;
    public TableColumn<Book,String> tableColumnTitle;
    public TableColumn<Book,String> tableColumnAuthor;
    public TableColumn<Book,String> tableColumnEdition;
    public TableColumn<Book,String> tableColumnPH;
    public TableView<Book> tableView;
    public TextField searchField;
    public CheckBox checkBox;
    private IService service;
    ObservableList<Book> model = FXCollections.observableArrayList();
    Reader reader;

    public void setService(IService service, Reader reader) {
        this.service = service;
        this.reader = reader;
        initModel();
    }

    @FXML
    public void initialize()
    {
        tableColumnIsbn.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getIsbn()));
        tableColumnAuthor.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getAuthor()));
        tableColumnTitle.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getTitle()));
        tableColumnEdition.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getEdition()));
        tableColumnPH.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getPublishing_house()));
        tableColumnImg.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(get_image_from_path(x.getValue().getImg_path())));
        FilteredList<Book> filteredData=new FilteredList<>(model, u->true);
        searchField.textProperty().addListener((observable,oldValue,newValue)->
                {
                    filteredData.setPredicate(book->
                    {
                        if(newValue==null||newValue.isEmpty())
                            return true;
                        else return book.getAuthor().contains(newValue) || book.getEdition().contains(newValue) | book.getTitle().contains(newValue)
                                || book.getIsbn().contains(newValue)||book.getPublishing_house().contains(newValue);
                    });
                }
        );
        tableView.setItems(filteredData);
    }

    private void initModel()
    {
        model.clear();
        Iterable<Book> books = service.getBooks();
        books.forEach(book->model.add(book));

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>()
        {

            public void handle(ActionEvent e)
            {
                if(checkBox.isSelected())
                {
                    model.clear();
                    Iterable<Book> books = service.getBooksForBorrowing();
                    books.forEach(book->model.add(book));
                }
                else
                {
                    model.clear();
                    Iterable<Book> books = service.getBooks();
                    books.forEach(book->model.add(book));
                }
            }

        };
        checkBox.setOnAction(event);
    }

    private ImageView get_image_from_path(String path)
    {
        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        ImageView iv = new ImageView();
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setImage(image);
        iv.setFitWidth(60);
        iv.setFitHeight(120);
        return iv;
    }

    public void borrow(ActionEvent actionEvent)
    {
    }
}

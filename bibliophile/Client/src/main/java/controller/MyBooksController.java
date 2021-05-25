package controller;
import domain.Reader;
import domain.Return;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import service.IService;
import service.Observer;

import java.io.File;
import java.time.LocalDate;

public class MyBooksController implements Observer
{
    public TableView<Return> tableView;
    public TableColumn<Return,ImageView> tableColumnImg;
    public TableColumn<Return, LocalDate> tableColumnStart;
    public TableColumn<Return,LocalDate> tableColumnEnd;
    private IService service;
    ObservableList<Return> model = FXCollections.observableArrayList();
    Reader reader;

    public void setService(IService service, Reader reader)
    {
        this.service = service;
        this.reader = reader;
        this.service.addObserver(this);
        initModel();
    }
    @FXML
    public void initialize()
    {
        tableColumnEnd.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getReturnDate()));
        tableColumnStart.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(x.getValue().getLoan().getLoanDate()));
        tableColumnImg.setCellValueFactory(x->new ReadOnlyObjectWrapper<>(get_image_from_path(x.getValue().getLoan().getBook().getImg_path())));
        tableView.setItems(model);
    }

    private void initModel()
    {
        model.clear();
        Iterable<Return> returns = service.getMyBooks(reader);
        returns.forEach(ret->model.add(ret));
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

    @Override
    public void update() {
        Platform.runLater(()->{model.clear();model.clear();initModel();});
    }
}

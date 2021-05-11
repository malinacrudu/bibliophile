package controller;

import domain.Reader;
import service.IService;

public class MyBooksController
{
    private IService service;
    Reader reader;

    public void setService(IService service, Reader reader) {
        this.service = service;
        this.reader = reader;
    }

}

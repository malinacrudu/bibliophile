package domain;
import java.io.Serializable;

public class Librarian extends User implements Serializable
{
    public Librarian() {
    }

    public Librarian(String name, String password, String pic) {
        super(name, password, pic);
    }
}

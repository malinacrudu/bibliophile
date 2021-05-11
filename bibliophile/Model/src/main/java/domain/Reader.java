package domain;
import java.io.Serializable;

public class Reader extends User implements Serializable
{
    public Reader() {
    }

    public Reader(String name, String password, String pic) {
        super(name, password, pic);
    }
}

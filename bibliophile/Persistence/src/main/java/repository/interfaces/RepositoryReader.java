package repository.interfaces;
import domain.Librarian;
import domain.Reader;

public interface RepositoryReader extends Repository<Long, Reader>
{
    Reader findReaderByPic(String pic);
}

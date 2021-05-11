package repository.interfaces;
import domain.Librarian;


public interface RepositoryLibrarian extends Repository<Long, Librarian>
{
   Librarian findLibrarianByPic(String pic);
}

package domain;
import java.io.Serializable;
import java.util.Objects;

public class Book extends Entity<Long> implements Serializable
{
    private String isbn;
    private String title;
    private String author;
    private String publishing_house;
    private String edition;
    private String img_path;
    private boolean already_borrowed;

    public Book() {
    }

    public Book(String isbn, String title, String author, String publishing_house, String edition, String img_path, boolean already_borrowed)
    {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publishing_house = publishing_house;
        this.edition = edition;
        this.img_path = img_path;
        this.already_borrowed = already_borrowed;
    }

    public Boolean getAlready_borrowed() {
        return already_borrowed;
    }

    public void setAlready_borrowed(Boolean already_borrowed) {
        this.already_borrowed = already_borrowed;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishing_house(String publishing_house) {
        this.publishing_house = publishing_house;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishing_house() {
        return publishing_house;
    }

    public String getEdition() {
        return edition;
    }

    public String getImg_path() {
        return img_path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(publishing_house, book.publishing_house) &&
                Objects.equals(edition, book.edition) &&
                Objects.equals(img_path, book.img_path) &&
                Objects.equals(already_borrowed, book.already_borrowed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, title, author, publishing_house, edition, img_path, already_borrowed);
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishing_house='" + publishing_house + '\'' +
                ", edition='" + edition + '\'' +
                ", img_path='" + img_path + '\'' +
                ", already_borrowed=" + already_borrowed +
                '}';
    }
}

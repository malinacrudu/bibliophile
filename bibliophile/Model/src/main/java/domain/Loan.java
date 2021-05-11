package domain;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Loan extends Entity<Long> implements Serializable
{
    private Reader reader;
    private Book book;
    private LocalDate loanDate;
    private boolean returned;

    public Loan(Reader reader, Book book, LocalDate loanDate, boolean returned) {
        this.reader = reader;
        this.book = book;
        this.loanDate = loanDate;
        this.returned = returned;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public Reader getReader() {
        return reader;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return returned == loan.returned &&
                Objects.equals(reader, loan.reader) &&
                Objects.equals(book, loan.book) &&
                Objects.equals(loanDate, loan.loanDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reader, book, loanDate, returned);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "reader=" + reader +
                ", book=" + book +
                ", loanDate=" + loanDate +
                ", returned=" + returned +
                '}';
    }
}

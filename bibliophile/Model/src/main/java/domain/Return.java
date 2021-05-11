package domain;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Return extends Entity<Long> implements Serializable
{
    private Librarian librarian;
    private Loan loan;
    private LocalDate returnDate;

    public Return(Librarian librarian, Loan loan, LocalDate returnDate) {
        this.librarian = librarian;
        this.loan = loan;
        this.returnDate = returnDate;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Return aReturn = (Return) o;
        return Objects.equals(librarian, aReturn.librarian) &&
                Objects.equals(loan, aReturn.loan) &&
                Objects.equals(returnDate, aReturn.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(librarian, loan, returnDate);
    }

    @Override
    public String toString() {
        return "Return{" +
                "librarian=" + librarian +
                ", loan=" + loan +
                ", returnDate=" + returnDate +
                '}';
    }
}

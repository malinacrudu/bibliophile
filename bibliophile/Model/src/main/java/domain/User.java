package domain;
import java.io.Serializable;
import java.util.Objects;

public class User extends Entity<Long> implements Serializable
{
        private String name;
        private String password;
        private String pic;

        public User(String name, String password, String pic)
        {
         this.name=name;
         this.password=password;
         this.pic=pic;
        }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPic() {
        return pic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(pic, user.pic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, pic);
    }
}

package academy.kovalevskyi.javadeepdive.week2.day2;

public class User {
    public String mail;
    public String firstName;
    public String lastName;
    public String password;

    public User() {}
    public static final String[] header = {"mail"
                            , "firstName"
                            , "lastName"
                            , "password"};

    public String[] toStringArray() {
        return new String[] {mail,firstName,lastName,password};
    }

    public User(String mail, String firstName, String lastName, String password) {
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
    public static class Builder {
        private String mail;
        private String firstName;
        private String lastName;
        private String password;

        public Builder mail(String mail) {
            this.mail = mail;
            return this;
        }
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this.mail, this.firstName, this.lastName, this.password);
        }
    }
}
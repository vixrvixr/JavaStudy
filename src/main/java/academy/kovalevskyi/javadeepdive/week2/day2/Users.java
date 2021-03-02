package academy.kovalevskyi.javadeepdive.week2.day2;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import academy.kovalevskyi.javadeepdive.week0.day3.RequestException;
import academy.kovalevskyi.javadeepdive.week2.day1.Controller;
import academy.kovalevskyi.javadeepdive.week2.day1.Get;
import academy.kovalevskyi.javadeepdive.week2.day1.Path;
import academy.kovalevskyi.javadeepdive.week2.day1.Post;

import java.util.Optional;

@Controller
public class Users {

    private static UserDB db = new UserDB(new CSV.Builder()
            .header(User.header)
            .values(new String[0][])
            .build());

    @Get
    @Path("/users")
    public String[] users() {
        String[] result = new String[0];
        try {
            return Users.db.getUsersMails();
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Get
    @Path("/first")
    public User firstUser() {
        String mail;
        Optional<User> user = Optional.empty();
        try {
            mail = Users.db.getUsersMails()[0];
            user = Users.db.getUser(mail);
        } catch (RequestException e) {
            e.printStackTrace();
        }
        if (user.isPresent())
            return user.get();
        return null;
    }

    @Post
    @Path("/users")
    public void addUser(User user) {
        try {
            Users.db.addUser(user);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }
}
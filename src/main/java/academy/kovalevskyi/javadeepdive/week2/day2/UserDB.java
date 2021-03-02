package academy.kovalevskyi.javadeepdive.week2.day2;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import academy.kovalevskyi.javadeepdive.week0.day3.InsertRequest;
import academy.kovalevskyi.javadeepdive.week0.day3.RequestException;
import academy.kovalevskyi.javadeepdive.week0.day3.SelectRequest;
import academy.kovalevskyi.javadeepdive.week0.day3.Selector;

import java.util.Optional;

public class UserDB {
    private CSV csv;
    public UserDB(CSV csv) {
        this.csv = csv;
    }
    public synchronized void addUser(User user) throws RequestException {
        this.csv = new InsertRequest.Builder()
                .insert(user.toStringArray())
                .to(this.csv)
                .build()
                .execute();
    }

    public String[] getUsersMails() throws RequestException {
        String[][] answer = (new SelectRequest.Builder())
                .select(new String[] { "mail" })
                .from(this.csv)
                .build()
                .execute();
        String[] usersMails = new String[answer.length];
        for (int i = 0; i < answer.length; i++) {
            for (int j = 0; j < answer[i].length; j++) {
                usersMails[i] = answer[i][j];
            }
        }
        return usersMails;
    }

    public Optional<User> getUser(String mail) throws RequestException {
        String[][] answer = (new SelectRequest.Builder())
                .select(new String[] {
                        "mail"
                        , "firstName"
                        , "lastName"
                        , "password"})
                .from(this.csv)
                .where((new Selector.Builder()).fieldName("mail").value(mail)
                        .build())
                .build()
                .execute();

        User user = (new User.Builder())
                .mail(answer[0][0])
                .firstName(answer[0][1])
                .lastName(answer[0][2])
                .password(answer[0][3]).build();
        return Optional.ofNullable(user);
    }
}
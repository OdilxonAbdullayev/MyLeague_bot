package user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
    private final List<User> users = new ArrayList<>();

    {
        users.add(new User("1167142966", "PRIVATE_ADMIN", "+", UserState.HOME, UserRole.ADMIN));
    }


    private static final UserService service = new UserService();

    public User getOrElseCreate(String id, String firstName) {
        Optional<User> first = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
        if (first.isEmpty()) {
            User newUser = User.builder()
                    .id(id).name(firstName).state(UserState.START).role(UserRole.USER).build();
            users.add(newUser);
            return newUser;
        } else {
            return first.get();
        }
    }

    public static UserService getInstance() {
        return service;
    }

    public String getTeacherId() {
        User user1 = users.stream().filter(user -> user.getRole().equals(UserRole.ADMIN)).findFirst().get();
        return user1.getId();
    }

    public List<User> getAllUser() {
        return users;
    }

    public User findByFirstName(String firstName) {
        for (User user : users) {
            if (user.getName().equals(firstName)) {
                return user;
            }
        }
        return null;
    }

    public void editUsername(User user, String text) {
        for (User value : users) {
            if (value.getId().equals(user.getId())) {
                value.setName(text);
            }
        }
    }

    public void editPhoneNumber(User user, String phoneNumber) {
        for (User value : users) {
            if (value.getId().equals(user.getId())) {
                value.setPhoneNumber(phoneNumber);
            }
        }
    }

}

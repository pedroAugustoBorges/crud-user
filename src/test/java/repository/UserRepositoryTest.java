package repository;

import connection.ConnectionFactory;
import domain.User;
import org.h2.engine.UserBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private Connection connection;

    @BeforeEach
    void setUp () throws SQLException {
        connection = ConnectionFactory.getConnection();

        try ( Statement statement = connection.createStatement()) {
//            statement.execute("DROP TABLE IF EXISTS \"user\" CASCADE");
            statement.executeUpdate("CREATE TABLE \"user\" (id_user SERIAL PRIMARY KEY, name VARCHAR (255), phone VARCHAR (30) NOT NULL, email VARCHAR (255) NOT NULL)");
        }


    }

    @AfterEach
    void tearDown () throws SQLException {
        connection = ConnectionFactory.getConnection();

        try ( Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE \"user\"");
        }


    }

    @Test
    void findByName() {
        User user = User.builder()
                .name("Taina Cristina")
                .phone("13 94025-2255")
                .email("tcsantos@gmail.com")
                .build();

        UserRepository.save(user);

        List<User> users = UserRepository.findBYName("Cristina");

        assertEquals(1, users.size());
        assertEquals("Taina Cristina", users.get(0).getName());
    }

    @Test
    void findById() {

        User user = User.builder()
                .name("Pedro Pablo")
                .phone("43 29959-4429")
                .email("mcph2024@gmail.com")
                .build();

        UserRepository.save(user);


        List<User> users = UserRepository.allUserWithId();

        Integer userId = users.get(0).getId();

        Optional<User> foundUser = UserRepository.findById(userId);

       assertTrue(foundUser.isPresent());

       assertEquals("Pedro Pablo",  foundUser.get().getName());

    }

    @Test
    void delete() {

        User user = User.builder()
                .name("Delete me")
                .phone("66554433")
                .email("deletedmeplease@gmail.com")
                .build();

        UserRepository.save(user);

        List<User> users = UserRepository.findBYName("Delete me");

        assertEquals(1, users.size());

        User userGet = users.get(0);

        UserRepository.delete(userGet.getId());

        assertTrue(UserRepository.findBYName("Delete me").isEmpty());


    }

    @Test
    void save() {
        User user = User.builder()
                .name("João Gabriel")
                .phone("13 94025-2255")
                .email("joaobibi244@gmail.com")
                .build();

        UserRepository.save(user);

        List<User> users = UserRepository.findBYName("Jo");

        assertEquals(1, users.size());
        assertEquals("João Gabriel", users.get(0).getName());
        assertEquals("13 94025-2255", users.get(0).getPhone());
        assertEquals("joaobibi244@gmail.com", users.get(0).getEmail());
    }

    @Test
    void update() {
        User user = User.builder()
                .name("João Gabriel")
                .phone("13 94025-2255")
                .email("joaobibi244@gmail.com")
                .build();

        UserRepository.save(user);

        List<User> users = UserRepository.findBYName(user.getName());

        User oldUser = users.get(0);

        String oldUserName = oldUser.getName();

        User userToUpdate = User.builder()
                .id(oldUser.getId())
                .name("Pedro Pablo")
                .phone("43 29959-4429")
                .email("mcph2024@gmail.com")
                .build();

        UserRepository.update(userToUpdate);

        List<User> usersUpdated = UserRepository.findBYName("Pedro Pablo");

        assertEquals(1, usersUpdated.size());

        User updatedUser = usersUpdated.get(0);

        assertNotEquals(oldUserName, updatedUser.getName());
        assertEquals("Pedro Pablo", userToUpdate.getName());
        assertEquals("43 29959-4429", userToUpdate.getPhone());
        assertEquals("mcph2024@gmail.com", userToUpdate.getEmail());
    }
//
//    @Test
//    void allUserWithId() {
//    }
}
package service;

import domain.User;
import repository.UserRepository;

import java.util.Optional;
import java.util.Scanner;

public class UserService {

    private final Scanner SCANNER;
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.SCANNER = new Scanner(System.in);
        this.userRepository = userRepository;
    }

    public void buildMenu(int op) {

        switch (op) {
            case 1 -> findByName();
            case 2 -> delete();
            case 3 -> save();
            case 4 -> update();
            default -> throw new IllegalArgumentException("Not a valid option");
        }

    }

    private void findByName() {
        System.out.println("Insert name for the search");

        String name = SCANNER.nextLine().trim();

        UserRepository.findBYName(name).forEach(u -> System.out.printf("[%d} - %s, %s, %s%n", u.getId(), u.getName(), u.getPhone(), u.getEmail()));

    }

    private void delete() {
        System.out.println("Type one of the below to delete");
        int id;

        try {
            id = Integer.parseInt(SCANNER.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
            return;
        }

        validId(id);

        System.out.println("Are you sure? (Y/N)");
        String choice = SCANNER.nextLine().trim();

        if ("Y".equalsIgnoreCase(choice)) {
            UserRepository.delete(id);
        } else {
            System.out.println("Operation delete canceled");
        }
    }

    private void save() {
        System.out.println("Type the datas of producer");

        System.out.println("Name:");

        String name = SCANNER.nextLine().trim();

        System.out.println("Phone: ");

        String phone = SCANNER.nextLine().trim();

        System.out.println("Email: ");

        String email = SCANNER.nextLine().trim();
        UserRepository.save(User.builder().
                name(name)
                .phone(phone)
                .email(email)
                .build());
    }

    private void update() {
        Optional<User> userOptional = UserRepository.findById(Integer.parseInt(SCANNER.nextLine()));

        if (userOptional.isEmpty()) {
            System.out.println("User not found");
            return;
        }

        User userFromDatabase = userOptional.get();


        System.out.println("Type the name or enter to keep the same (input empty) ");

        System.out.println("Name: ");
        String name = SCANNER.nextLine();

        name = name.isEmpty() ? userFromDatabase.getName() : name;

        System.out.println("Phone: ");

        String phone = SCANNER.nextLine();

        System.out.println("Email:");

        String email = SCANNER.nextLine();

        User user = User.builder()
                .id(userFromDatabase.getId())
                .name(name)
                .phone(phone)
                .email(email)
                .build();

        UserRepository.update(user);
    }


    private void validId(Integer id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id invalid. Must be > 0");
        }
    }
}

package domain;


import lombok.Builder;
import lombok.Value;

@Value
@Builder

public class User {

    Integer id;
    String name;
    String phone;
    String email;
}

package net.gazpromkitchen.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import net.gazpromkitchen.server.model.User;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequestDto {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;

    public User toUser(){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);

        return user;
    }
}

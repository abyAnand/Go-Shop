package io.ecommerce.GoShop.DTO;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


    @NotNull(message = "firstName is Required")
    @Size(min = 2)
    private String firstName;

    private String lastName;

    @NotNull(message = "email is Required")
    @Email
    private String email;

    @NotNull(message = "phoneNumber is Required")
//    @Range(min = 10, max = 10, message = "Mobile number must be 10 digits long")
    private long phoneNumber;

    @NotNull(message = "username is Required")
    @Size(min = 2)
    private String username;

    @NotNull(message = "password must be at least 8 characters long")
//    @Size(min = 8)
    private String password;

    private String role;


}

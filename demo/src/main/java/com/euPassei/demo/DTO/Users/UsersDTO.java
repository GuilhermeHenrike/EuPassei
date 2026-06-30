package com.euPassei.demo.DTO.Users;

import com.euPassei.demo.Entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsersDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório!")
    @Size(min = 4, max = 25, message = "O nome deve ter entre 4 e 25 letras!")
    private String username;

    @NotBlank(message = "A senha é obrigatória!")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres!")
    private String password;

    public UsersDTO(Users user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public Users toEntity() {
        Users user = new Users();
        user.setId(this.id);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

}

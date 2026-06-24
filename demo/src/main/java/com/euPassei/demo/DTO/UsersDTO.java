package com.euPassei.demo.DTO;

import com.euPassei.demo.Entity.Users;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
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

}

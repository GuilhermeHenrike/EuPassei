package com.euPassei.demo.Service;

import com.euPassei.demo.DTO.Users.UsersDTO;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Users registrarUser(Users user) {
        boolean existe = usersRepository.existsByUsername(user.getUsername());

        if (existe) {
           throw new IllegalArgumentException("O usuario já existe");
        }

        return usersRepository.save(user);
    }

    public Users logarUser(UsersDTO user) {
        Users usuarioAchado = usersRepository.findByUsername(user.getUsername());

        if (usuarioAchado == null) {
            throw new IllegalArgumentException("Nome ou senha incorretos");
        }

        if  (!usuarioAchado.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Nome ou senha incorretos");
        }

        return usuarioAchado;
    }
}

package com.euPassei.demo.Service;

import com.euPassei.demo.DTO.Users.UsersDTO;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    public Users registrarUser(Users user) {
        boolean existe = usersRepository.existsByUsername(user.getUsername());

        if (existe) {
           throw new IllegalArgumentException("O usuario já existe");
        }

        String senhaCriptografada = passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCriptografada);
        return usersRepository.save(user);
    }

    public Users logarUser(UsersDTO user) {
        Users usuarioAchado = usersRepository.findByUsername(user.getUsername());

        if (usuarioAchado == null) {
            throw new IllegalArgumentException("Nome ou senha incorretos");
        }

        // se a senha que o usuario passou quando criptografada não for igual a senha do usuario
        // que está no banco (já criptografada), ele entra nesse erro
        if (!passwordEncoder.matches(user.getPassword(), usuarioAchado.getPassword())) {
            throw new IllegalArgumentException("nome ou senha incorretos");
        }

        return usuarioAchado;
    }
}

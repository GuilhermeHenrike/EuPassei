package com.euPassei.demo.Controller.UsersController;

import com.euPassei.demo.DTO.Users.UsersDTO;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Service.UsersService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsersDTO user, BindingResult result) {

        if (result.hasErrors()) {
            // getFieldError() = Erro nos campos preenchidos
            // getDefaultMessage() mensagem do erro (validation entidade)
            String mensagemErro = result.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
        }

        try {
          usersService.registrarUser(user.toEntity()); // transforma o DTO em Users com o toEntity
          return ResponseEntity.status(HttpStatus.CREATED).body("Registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UsersDTO user, BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            String mensagemErro = result.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
        }

        try {
            Users usuario = usersService.logarUser(user);
            // manda o user no logar e se der certo tras o usuario equivalente e seta na variavel
            session.setAttribute("userLogado", usuario); // seta o usuario como logado
            return ResponseEntity.ok(new UsersDTO(usuario));
            // 200, cria um novo DTO com os dados passados
            // retorna o usuario pro frontend
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {

        try {
            if (session != null) {
                session.invalidate();
            }
            return ResponseEntity.status(HttpStatus.OK).body("Logout com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao realizar logout: " + e.getMessage());        }
    }
}

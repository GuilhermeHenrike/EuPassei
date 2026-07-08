package com.euPassei.demo.Controller.ProvasController;

import com.euPassei.demo.DTO.Provas.ProvasCreateDTO;
import com.euPassei.demo.DTO.Provas.ProvasEditDTO;
import com.euPassei.demo.Entity.Provas;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Service.ProvasService;
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
public class ProvasController {

    @Autowired
    private ProvasService provasService;

    @PostMapping("/provas/{caixaId}")
    public ResponseEntity<?> criarProva(@PathVariable Long caixaId,
                                        @Valid @RequestBody ProvasCreateDTO provas,
                                        HttpSession session, BindingResult result) {

        if (result.hasErrors()) {
            String mensagemErro = result.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
        }

        Users userLogado = (Users) session.getAttribute("userLogado");
        if (userLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você precisa estar logado");
        }

        try {
            Provas provaNova = provas.toEntity();
            provasService.salvar(provaNova, caixaId, userLogado);
            return ResponseEntity.status(HttpStatus.CREATED).body("Prova salva com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/provas/{caixaId}/{provaId}")
    public ResponseEntity<?> excluirProva(@PathVariable Long caixaId, @PathVariable Long provaId,
                                          HttpSession session) {

        Users userLogado = (Users) session.getAttribute("userLogado");

        if (userLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você precisa estar logado");
        }

        try {
            provasService.deletar(provaId, caixaId, userLogado);
            return ResponseEntity.ok("Prova excluida com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/provas/{caixaId}")
    public ResponseEntity<?> editarProva(@PathVariable Long caixaId,
                                         @Valid @RequestBody ProvasEditDTO prova,
                                         BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            String mensagemErro = result.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
        }

        Users userLogado = (Users) session.getAttribute("userLogado");
        if (userLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você precisa estar logado");
        }

        try {
            provasService.mudarProva(prova, caixaId, userLogado);
            return ResponseEntity.ok("Prova editada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

package com.euPassei.demo.Controller.CaixaProvasController;
import com.euPassei.demo.DTO.CaixaProvas.CaixaProvasEditDTO;
import com.euPassei.demo.DTO.CaixaProvas.CaixaProvasRequestDTO;
import com.euPassei.demo.Entity.CaixaProvas;
import com.euPassei.demo.Entity.Users;
import com.euPassei.demo.Service.CaixaProvasService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api")
public class CaixaProvasController {

    @Autowired
    private CaixaProvasService caixaProvasService;

    @PostMapping("/caixaProvas")
    public ResponseEntity<?> criarCaixaProvas(@Valid @RequestBody CaixaProvasRequestDTO caixaDTO,
                                              BindingResult result, HttpSession session){

        if (result.hasErrors()){
            String mensagemErro = result.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
        }

        Users userLogado = (Users) session.getAttribute("userLogado");
        if (userLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você precisa estar logado!");
        }

        try {
            CaixaProvas caixa = caixaDTO.toEntity();
            caixaProvasService.criarCaixaProvas(caixa, userLogado);
            return ResponseEntity.status(HttpStatus.CREATED).body("Caixa de provas criada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/caixaProvas/{caixaId}")
    public ResponseEntity<?> deletarCaixaProvas(@PathVariable Long caixaId, HttpSession session) {

        Users userLogado = (Users) session.getAttribute("userLogado");
        if (userLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você  precisa estar logado!");
        }

        try {
            caixaProvasService.excluirCaixaProvas(caixaId, userLogado);
            return ResponseEntity.ok().body("Caixa de provas deletada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/caixaProvas/{caixaId}")
    public ResponseEntity<?> editarCaixaProvas(@PathVariable Long caixaId, HttpSession session,
                                          @Valid @RequestBody CaixaProvasEditDTO dto,
                                          BindingResult result) {

        if (result.hasErrors()) {
            String mensagemErro = result.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
        }

        Users userLogado = (Users) session.getAttribute("userLogado");
        if (userLogado == null) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você precisa estar logado!");
        }

        try {
            caixaProvasService.editarCaixa(caixaId, dto, userLogado);
            return ResponseEntity.status(HttpStatus.OK).body("Caixa de provas editada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/caixaProvas")
    public ResponseEntity<?> listarTodasCaixas(HttpSession session) {

        Users userLogado = (Users) session.getAttribute("userLogado");
        if (userLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você precisa estar logado!");
        }

        try {
            // cria uma lista de provas e manda o id do usuario pra listarTodasCaixasDoUsuario
            List<CaixaProvas> lista = caixaProvasService.listarTodasCaixasDoUsuario(userLogado);
            // retorna status de ok e a lista com todas as caixas
            return ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

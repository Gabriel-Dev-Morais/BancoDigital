package br.com.fourcamp.controllers;

import br.com.fourcamp.dto.Login;
import br.com.fourcamp.dto.LoginResponse;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.services.ClienteService;
import br.com.fourcamp.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ClienteService clienteService;
    private final JwtService jwtService;

    public AuthController(ClienteService clienteService, JwtService jwtService) {
        this.clienteService = clienteService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login request) {
        try {
            Cliente cliente = clienteService.autenticar(request.cpf(), request.password());
            String token = jwtService.gerarToken(cliente.getCpf());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("CPF ou senha inválidos");
        }
    }
}
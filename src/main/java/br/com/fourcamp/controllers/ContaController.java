package br.com.fourcamp.controllers;

import br.com.fourcamp.dto.CartaoDto;
import br.com.fourcamp.dto.OperacaoBancariaDto;
import br.com.fourcamp.exceptions.ClienteNaoEncontradoException;
import br.com.fourcamp.exceptions.ContaNaoEncontradaException;
import br.com.fourcamp.exceptions.SaldoInsuficienteException;
import br.com.fourcamp.models.Cartao;
import br.com.fourcamp.models.Cliente;
import br.com.fourcamp.models.Conta;
import br.com.fourcamp.services.CartaoService;
import br.com.fourcamp.services.ClienteService;
import br.com.fourcamp.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private CartaoService cartaoService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas(){

        return ResponseEntity.ok(contaService.listarContas());
    }

    @GetMapping("/find/{numeroEAgencia}")
    public ResponseEntity<Conta> pesquisarConta(@PathVariable String numeroEAgencia) throws ContaNaoEncontradaException {
        Conta contaEncontrada = contaService.buscarPorNumeroEAgencia(numeroEAgencia);

        return ResponseEntity.ok(contaEncontrada);
    }

    @PostMapping("/{cpf}/cartoes")
    public ResponseEntity<?> cadastrarCartao(
            @PathVariable String cpf,
            @RequestBody CartaoDto dto) {
        try {
            Cliente cliente = clienteService.buscarCliente(cpf);
            String numeroEAgencia = cliente.getConta().getNumeroEAgencia();

            Cartao novo = contaService.criarCartao(numeroEAgencia, dto.senha(), dto.tipoCartao());

            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (ContaNaoEncontradaException | ClienteNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @GetMapping("/{cpf}/cartoes")
    public ResponseEntity<List<Cartao>> listarCartoesAtivos(@PathVariable String cpf){
        List<Cartao> cartoesAtivos = contaService.listarCartoesAtivos(cpf);
        return ResponseEntity.ok(cartoesAtivos);
    }

    @PatchMapping("/{numeroEAgencia}/depositar")
    public ResponseEntity<String> depositar(@PathVariable String numeroEAgencia, @RequestBody OperacaoBancariaDto dto) throws ContaNaoEncontradaException {
        contaService.depositar(numeroEAgencia, dto.valor());
        return ResponseEntity.ok("Depósito de R$ "+dto.valor()+" feito com sucesso!");
    }

    @PatchMapping("/{numeroEAgencia}/sacar")
    public ResponseEntity<String> sacar(@PathVariable String numeroEAgencia, @RequestBody OperacaoBancariaDto dto) throws SaldoInsuficienteException, ContaNaoEncontradaException {
        contaService.sacar(numeroEAgencia, dto.valor());
        return ResponseEntity.ok("Saque de R$ "+dto.valor()+" feito com sucesso!");
    }


}

package br.com.fourcamp.dto;

import br.com.fourcamp.enums.Estado;

public record AtualizarEnderecoDto(
        String nomeRua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String cep,
        Estado estado

) {
}

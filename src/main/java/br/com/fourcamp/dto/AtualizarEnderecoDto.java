package br.com.fourcamp.dto;

public record AtualizarEnderecoDto(
        String nomeRua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String cep

) {
}

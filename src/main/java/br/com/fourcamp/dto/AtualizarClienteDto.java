package br.com.fourcamp.dto;

import br.com.fourcamp.enums.TipoCliente;
import br.com.fourcamp.enums.TipoConta;

public record AtualizarClienteDto(
        String nome,
        String cpf,
        TipoCliente tipoCliente,
        String senhaConta
) {
}

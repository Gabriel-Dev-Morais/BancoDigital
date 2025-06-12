package br.com.fourcamp.models;

import br.com.fourcamp.enums.Estado;
import br.com.fourcamp.exceptions.CepInvalidoException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Embeddable
public class Endereco {

    @Column(name = "nome_da_rua")
    @NotBlank(message = "Endereço precisa ter rua!")
    private String nomeRua;

    @Column(name = "numero")
    @NotBlank(message = "Endereço precisa ter número!")
    private String numero;

    @Column(name = "complemento")
    @NotNull
    private String complemento;

    @Column(name = "bairro")
    @NotBlank(message = "Endereço precisa ter bairro!")
    private String bairro;

    @Column(name = "cidade")
    @NotBlank(message = "Endereço precisa ter cidade!")
    private String cidade;

    @Column(name = "cep")
    @NotBlank(message = "Endereço precisa ter CEP!")
    @Size(min = 8, max = 8, message = "CEP precisa ter, exatamente, 8 dígitos!")
    private String cep;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "estado")
    private Estado estado;

    public Endereco() {
    }

    public Endereco(String nomeRua, String numero, String complemento, String bairro, String cidade, String cep, Estado estado) {
        this.nomeRua = nomeRua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.cep = cep;
    }

    public String getNomeRua() {
        return nomeRua;
    }

    public void setNomeRua(String nomeRua) {
        this.nomeRua = nomeRua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCepFormatado() {
        return String.format("%s-%s",
                cep.substring(0, 5),
                cep.substring(5, 8)
        );
    }




    public void setCep(String cep) throws CepInvalidoException {
        if (cep.length() != 8){
            throw new CepInvalidoException("Cep Inválido");
        }
        this.cep = cep;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}

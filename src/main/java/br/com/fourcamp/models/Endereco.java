package br.com.fourcamp.models;

import br.com.fourcamp.exceptions.CepInvalidoException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Endereco {

    @Column(name = "nome da rua")
    private final String nomeRua;

    @Column(name = "numero")
    private final String numero;

    @Column(name = "complemento")
    private final String complemento;

    @Column(name = "cidade")
    private final String cidade;

    @Column(name = "cep")
    private final String cep;

    public Endereco(String nomeRua, String numero, String complemento, String cidade, String cep) throws CepInvalidoException {
        this.nomeRua = nomeRua;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.cep = validarCep(cep);
    }

    public String validarCep(String cep) throws CepInvalidoException {
        if (cep.length() != 8){
            throw new CepInvalidoException("Cep Inválido");
        }
        else {
            cep = cep.substring(0,5) + "-"+
                    cep.substring(5,8);

            return cep;
        }
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "Nome da Rua: " + nomeRua + "\n" +
                "Número: " + numero + "\n" +
                "Complemento: " + complemento + "\n" +
                "Cidade: " + cidade + "\n" +
                "CEP: " + cep + "\n" +
                '}';
    }

}

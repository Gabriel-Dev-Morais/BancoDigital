package br.com.fourcamp.models;

import br.com.fourcamp.enums.TipoCartao;
import br.com.fourcamp.enums.TipoCliente;
import br.com.fourcamp.exceptions.LimiteAtingidoException;
import br.com.fourcamp.exceptions.SeguroFraudeInativoException;
import br.com.fourcamp.interfaces.Seguro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode
public class CartaoCredito extends Cartao implements Seguro {

    @OneToMany(mappedBy = "cartaoCredito")
    private List<Transacao> fatura;

    @Column(name = "limite")
    private Double limite;

    @Column(name = "total_da_fatura")
    private Double totalFatura;

    @Column(name = "status_seguro_viagem")
    private Boolean seguroViagem;

    @Column(name = "status_seguro_fraude")
    private Boolean seguroFraude;


    public CartaoCredito() {
    }

    public CartaoCredito(Long id, Conta conta, String senha, TipoCartao tipoCartao, Boolean seguroViagem, Boolean seguroFraude){
        super(id, conta, senha, tipoCartao);
        this.fatura = new ArrayList<>();
        this.totalFatura = 0.0;
        this.seguroViagem = seguroViagem;
        acionarSeguroViagem();
        this.seguroFraude = seguroFraude;
    }

    public List<Transacao> getFatura() {
        return fatura;
    }

    public void setFatura(List<Transacao> fatura) {
        this.fatura = fatura;
    }



    public Double getTotalFatura() {
        return totalFatura;
    }

    public void setTotalFatura(Double totalFatura) {
        this.totalFatura = totalFatura;
    }

    public Boolean getSeguroViagem() {
        return seguroViagem;
    }

    public void setSeguroViagem(Boolean seguroViagem) {
        this.seguroViagem = seguroViagem;
    }

    public Boolean getSeguroFraude() {
        return seguroFraude;
    }

    public void setSeguroFraude(Boolean seguroFraude) {
        this.seguroFraude = seguroFraude;
    }

    public void adicionarFatura(Transacao transacao){
        this.getFatura().add(transacao);
        this.setTotalFatura(this.getTotalFatura() + transacao.getValor());
    }





    public void pagarFatura(){

        Double valorFatura = this.getTotalFatura();

        for (Transacao transacao : this.getFatura()){
            Conta contaDestino = transacao.getContaDestino();
            contaDestino.setSaldo(contaDestino.getSaldo() + transacao.getValor());
        }

        if (valorFatura >= (0.8 * this.getLimite())){
            valorFatura += (0.05 * valorFatura);

        }
        this.getConta().setSaldo(this.getConta().getSaldo() - valorFatura);
        this.setTotalFatura(0.0);
        this.acionarSeguroViagem();

    }

    public Boolean permitirPagamento(Transacao transacao) throws LimiteAtingidoException {
        if ((this.getTotalFatura() < this.getLimite()) && this.getAtivo()){
            if (transacao.getValor() <= (this.getLimite() - this.getTotalFatura())){
                adicionarFatura(transacao);
                transacao.setCartaoCredito(this);
                return true;
            }
            else if (( this.getTotalFatura() == this.getLimite()) || !this.getAtivo()){
                throw new LimiteAtingidoException("Limite atingido ou cartão desativado!");
            }
        }
        return false;
    }



    @Override
    public void acionarSeguroViagem() {
        if ((this.getSeguroViagem() && (this.getConta().getCliente().getTipoCliente() == TipoCliente.SUPER || this.getConta().getCliente().getTipoCliente() == TipoCliente.COMUM)) && this.getAtivo()){
            this.setTotalFatura(this.getTotalFatura() + 50.0);
        }
    }

    @Override
    public void acionarSeguroFraude(Double valorFraude) throws SeguroFraudeInativoException {
        if ((this.getSeguroFraude() && valorFraude <= 5000.0) && this.getAtivo()){
            this.getConta().setSaldo(this.getConta().getSaldo() + valorFraude);
        }
        else {
            if (this.getSeguroFraude() && valorFraude > 5000.0){
                this.getConta().setSaldo(this.getConta().getSaldo() - (valorFraude - 5000) + 5000);
            }
            else {
                throw new SeguroFraudeInativoException("Lamentamos, mas você não possui Seguro Fraude.");
            }
        }
    }

    @Override
    public void ativarDesativarSeguroViagem() {
        this.setSeguroViagem(!this.getSeguroViagem());
    }

    @Override
    public void ativarDesativarSeguroFraude() {
        this.setSeguroFraude(!this.getSeguroFraude());
    }

}

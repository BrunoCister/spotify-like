package com.infnet.br.SpotifyLike.domain.transacao;

import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import com.infnet.br.SpotifyLike.domain.exceptions.*;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Boolean ativo;
    private Double limite;
    private String numero;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @OneToMany (mappedBy = "cartao", fetch = FetchType.EAGER, cascade =  CascadeType.ALL)
    private List<Transacao> transacoes = new ArrayList<>();

    public void criarTransacao(String vendedor, String descricao, Double valor) throws Exception{

        this.isCartaoAtivo();

        Transacao transacao = new Transacao();
        transacao.setDtTransicao(Date.from(Instant.now()));
        transacao.setValor(valor);
        transacao.setVendedor(vendedor);
        transacao.setDescricao(descricao);
        transacao.setCartao(this);

        autorizarTransacao(transacao, transacoes);

        transacao.setId(UUID.randomUUID());

        this.limite = this.limite - transacao.getValor();

        this.transacoes.add(transacao);
    }

    private boolean isCartaoAtivo() {
        return this.getAtivo();
    }

    private boolean isLimiteDisponivel(Transacao transacao) {
        return limite.compareTo(transacao.getValor()) >= 0;
    }

    private boolean isAteDoisMinutos(Date momentoTransacao) {

        Long DOIS_MINUTOS_EM_MILISSEGUNDOS = 2L * 60 * 1000;
        Date momentoAtual = new Date();

        Long diferencaTempo = momentoAtual.getTime() - momentoTransacao.getTime();
        return diferencaTempo <= DOIS_MINUTOS_EM_MILISSEGUNDOS;
    }

    private boolean isTransacaoSemelhante(Transacao transacaoAtual, Transacao transacaoPassada) {

        boolean isMesmoVendedor = transacaoAtual.getVendedor().equals(transacaoPassada.getVendedor());
        boolean isMesmoValor = transacaoAtual.getValor().equals(transacaoPassada.getValor());

        return isMesmoVendedor && isMesmoValor;
    }

    private boolean isTransacaoDuplicada(List<Transacao> transacoes, Transacao transacaoAtual) {

        int contador = 0;

        for (Transacao transacaoPassada : transacoes) {
            if (isAteDoisMinutos(transacaoPassada.getDtTransicao()) && isTransacaoSemelhante(transacaoAtual, transacaoPassada)) {
                contador += 1;
                if (contador >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAltaFrequencia(List<Transacao> transacoes) {

        int contador = 0;

        for (Transacao transacaoPassada : transacoes) {
            if (isAteDoisMinutos(transacaoPassada.getDtTransicao())) {
                contador += 1;
                if (contador >= 3 ) {
                    return true;
                }
            }
        }
        return false;
    }

    private void autorizarTransacao(Transacao transacao, List<Transacao> transacoes) throws Exception {

        if (!isCartaoAtivo()) {
            throw new CardNotActiveException(ExceptionMessages.CARD_NOT_ACTIVE);
        }

        if (!isLimiteDisponivel(transacao)) {
            throw new ExceedsCardLimitException(ExceptionMessages.EXCEEDS_CARD_LIMIT);
        }

        if (isTransacaoDuplicada(transacoes, transacao)) {
            throw new DoubleTransactionException(ExceptionMessages.DOUBLE_TRANSACTION);
        }

        if (isAltaFrequencia(transacoes)) {
            throw new HighFrequencyException(ExceptionMessages.HIGH_FREQUENCY);
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Double getLimite() {
        return limite;
    }

    public void setLimite(Double limite) {
        this.limite = limite;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }
}

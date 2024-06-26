package com.infnet.br.SpotifyLike.domain.transacao;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Date dtTransicao;
    private Double valor;
    private String vendedor;
    private String descricao;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDtTransicao() {
        return dtTransicao;
    }

    public void setDtTransicao(Date dtTransicao) {
        this.dtTransicao = dtTransicao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cartao getCartao() { return cartao; }

    public void setCartao(Cartao cartao) { this.cartao = cartao; }
}

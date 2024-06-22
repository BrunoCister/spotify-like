package com.infnet.br.SpotifyLike.domain.transacao;

import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "plano_id")
    private Plano plano;
    private Boolean ativo;
    private Date dtAssinatura;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Date getDtAssinatura() {
        return dtAssinatura;
    }

    public void setDtAssinatura(Date dtAssinatura) {
        this.dtAssinatura = dtAssinatura;
    }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}

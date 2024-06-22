package com.infnet.br.SpotifyLike.domain.conta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infnet.br.SpotifyLike.domain.exceptions.ExceptionMessages;
import com.infnet.br.SpotifyLike.domain.exceptions.MusicaNotFoundException;
import com.infnet.br.SpotifyLike.domain.exceptions.PlaylistNotFoundException;
import com.infnet.br.SpotifyLike.domain.streaming.Musica;
import com.infnet.br.SpotifyLike.domain.transacao.Assinatura;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.domain.transacao.Plano;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cartao> cartoes = new ArrayList<>();
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Playlist> playlists = new ArrayList<>();
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Assinatura> assinaturas = new ArrayList<>();

    public void criarUsuario(String nome, Plano plano, Cartao cartao) throws Exception {

        this.nome = nome;
        this.assinarPlano(plano, cartao);
        this.adicionarCartao(cartao);
        this.criarPlaylist();
    }

    public void criarPlaylist() {

        Playlist playlist = new Playlist();
        playlist.setId(UUID.randomUUID());
        playlist.setNome("Favoritas");
        playlist.setPublica(false);
        playlist.setUsuario(this);
        List<Musica> musicas = new ArrayList<>();
        playlist.setMusicas(musicas);
        this.playlists.add(playlist);
    }

    private void adicionarCartao(Cartao cartao) {

        this.cartoes.add(cartao);
    }

    private void assinarPlano(Plano plano, Cartao cartao) throws Exception {

        cartao.criarTransacao(plano.getNome(),plano.getDescricao(), plano.getValor());

        if (this.assinaturas.stream().count() > 0 && this.assinaturas.stream().anyMatch(assinatura -> assinatura.getAtivo())) {
            var planoAtivo = this.assinaturas.stream().filter(assinatura -> assinatura.getAtivo()).toList();
            planoAtivo.forEach(assinatura -> assinatura.setAtivo(false));
        }

        Assinatura assinatura = new Assinatura();
        assinatura.setId(UUID.randomUUID());
        assinatura.setPlano(plano);
        assinatura.setAtivo(true);
        assinatura.setDtAssinatura(Date.from(Instant.now()));
        assinatura.setUsuario(this);

        this.assinaturas.add(assinatura);
    }

    public void favoritarMusica(Musica musica) throws PlaylistNotFoundException {

        Playlist playlist = this.playlists.stream().filter(p -> p.getNome().equals("Favoritas")).findFirst().get();
        if (!playlist.getNome().equals("Favoritas")) {
            throw new PlaylistNotFoundException(ExceptionMessages.PLAYLIST_NOT_FOUND);
        }
        playlist.getMusicas().add(musica);
    }

    public void desfavoritarMusica(Musica musica) throws PlaylistNotFoundException, MusicaNotFoundException {

        Playlist playlist = this.playlists.stream().filter(p -> p.getNome().equals("Favoritas")).findFirst().get();
        if (!playlist.getNome().equals("Favoritas")) {
            throw new PlaylistNotFoundException(ExceptionMessages.PLAYLIST_NOT_FOUND);
        }

        /*Musica musicaFav = null;
        try {
            musicaFav = playlist.getMusicas().stream().filter(m -> m.getId().equals(musica.getId())).findFirst().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

        Musica musicaFav = playlist.getMusicas().stream().filter(m -> m.getId().equals(musica.getId())).findFirst().get();
        if (musicaFav == null) {
            throw  new MusicaNotFoundException(ExceptionMessages.MUSICA_NOT_FOUND);
        }

        //playlist.getMusicas().remove(musicaFav);
        playlist.getMusicas().removeIf(m -> m.getId().equals(musicaFav.getId()));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Assinatura> getAssinaturas() {
        return assinaturas;
    }

    public void setAssinaturas(List<Assinatura> assinaturas) {
        this.assinaturas = assinaturas;
    }
}

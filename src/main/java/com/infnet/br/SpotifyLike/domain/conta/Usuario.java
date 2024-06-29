package com.infnet.br.SpotifyLike.domain.conta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infnet.br.SpotifyLike.domain.exceptions.ExceptionMessages;
import com.infnet.br.SpotifyLike.domain.exceptions.PlaylistNotFoundException;
import com.infnet.br.SpotifyLike.domain.streaming.Musica;
import com.infnet.br.SpotifyLike.domain.transacao.Assinatura;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.domain.transacao.Plano;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.*;

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

    String nomePlaylistFavoritas = "Favoritas";

    public void criarUsuario(String nome, Plano plano, Cartao cartao) throws Exception {

        this.nome = nome;
        this.assinarPlano(plano, cartao);
        this.adicionarCartao(cartao);
        this.criarPlaylist();
    }

    public void criarPlaylist() {

        Playlist playlist = new Playlist();
        playlist.setId(UUID.randomUUID());
        playlist.setNome(nomePlaylistFavoritas);
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

        if ((long) this.assinaturas.size() > 0 && this.assinaturas.stream().anyMatch(Assinatura::getAtivo)) {
            var planoAtivo = this.assinaturas.stream().filter(Assinatura::getAtivo).toList();
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

        Optional<Playlist> playlist = this.playlists.stream().filter(p -> p.getNome().equals(nomePlaylistFavoritas)).findFirst();
        if (playlist.isPresent()){
            playlist.get().getMusicas().add(musica);
        } else {
            throw new PlaylistNotFoundException(ExceptionMessages.PLAYLIST_NOT_FOUND);
        }
    }

    public void desfavoritarMusica(Musica musica) throws PlaylistNotFoundException {

        Optional<Playlist> playlist = this.playlists.stream().filter(p -> p.getNome().equals(nomePlaylistFavoritas)).findFirst();
        if (playlist.isPresent()){
            Optional<Musica> musicaFav = playlist.get().getMusicas().stream().filter(m -> m.getId().equals(musica.getId())).findFirst();
            musicaFav.ifPresent(value -> playlist.get().getMusicas().removeIf(m -> m.getId().equals(value.getId())));
        } else {
            throw new PlaylistNotFoundException(ExceptionMessages.PLAYLIST_NOT_FOUND);
        }
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

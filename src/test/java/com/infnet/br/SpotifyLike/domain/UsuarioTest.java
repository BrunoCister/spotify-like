package com.infnet.br.SpotifyLike.domain;

import com.infnet.br.SpotifyLike.domain.conta.Playlist;
import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import com.infnet.br.SpotifyLike.domain.exceptions.AssinaturaNotActiveException;
import com.infnet.br.SpotifyLike.domain.exceptions.CardNotActiveException;
import com.infnet.br.SpotifyLike.domain.exceptions.ExceedsCardLimitException;
import com.infnet.br.SpotifyLike.domain.exceptions.PlaylistNotFoundException;
import com.infnet.br.SpotifyLike.domain.streaming.Musica;
import com.infnet.br.SpotifyLike.domain.transacao.Assinatura;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.domain.transacao.Plano;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @InjectMocks
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }


    @Test
    public void deveCriarUmUsuarioComSucesso() throws Exception {

        Cartao cartao = new Cartao();
        cartao.setAtivo(true);
        cartao.setNumero("12345");
        cartao.setLimite(500.00);

        Plano plano = new Plano();
        plano.setValor(29.99);
        plano.setNome("Plano Teste");

        usuario.criarUsuario("Usuario Teste", plano, cartao);

        assertNotNull(usuario);
        assertEquals("Usuario Teste", usuario.getNome());
        assertEquals("12345", usuario.getCartoes().getFirst().getNumero());
        assertEquals("Favoritas", usuario.getPlaylists().getFirst().getNome());
        assertEquals("Plano Teste", usuario.getAssinaturas().getFirst().getPlano().getNome());
    }

    @Test
    public void naoDeveCriarUsuarioCasoLimiteCartaoForMenorValorPlano()
    {
        Cartao cartao = new Cartao();
        cartao.setAtivo(true);
        cartao.setNumero("12345");
        cartao.setLimite(10.00);

        Plano plano = new Plano();
        plano.setValor(29.99);
        plano.setNome("Plano Teste");

        assertThrows(ExceedsCardLimitException.class, () ->
            usuario.criarUsuario("Usuario Teste", plano, cartao));
    }


    @Test
    public void naoDeveCriarUsuarioCasoCartaoEstejaInativo()
    {
        Cartao cartao = new Cartao();
        cartao.setAtivo(false);
        cartao.setNumero("12345");
        cartao.setLimite(500.00);

        Plano plano = new Plano();
        plano.setValor(29.99);
        plano.setNome("Plano Teste");

        assertThrows(CardNotActiveException.class, () ->
                usuario.criarUsuario("Usuario Teste", plano, cartao));
    }

    @Test
    public void deveFavoritarMusica() throws PlaylistNotFoundException {

        UUID usuarioId = UUID.randomUUID();
        usuario.setId(usuarioId);
        usuario.setNome("Nome Teste");

        UUID cartaoId = UUID.randomUUID();
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setAtivo(true);
        cartao.setLimite(500.00);
        cartao.setNumero("12345");
        cartao.setUsuario(usuario);
        List<Cartao> cartoes = new ArrayList<>();
        cartoes.add(cartao);
        usuario.setCartoes(cartoes);

        UUID musicaId = UUID.randomUUID();
        Musica musica = new Musica();
        musica.setId(musicaId);
        musica.setNome("Musica Teste");
        musica.setDuracao(120);

        UUID playlistId = UUID.randomUUID();
        Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        playlist.setNome("Favoritas");
        playlist.setPublica(true);
        playlist.setUsuario(usuario);
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);
        usuario.setPlaylists(playlists);

        usuario.favoritarMusica(musica);

        assertNotNull(usuario.getPlaylists().getFirst().getMusicas().getFirst());
        assertEquals(musica.getId(), usuario.getPlaylists().getFirst().getMusicas().getFirst().getId());
        assertEquals(musica.getNome(), usuario.getPlaylists().getFirst().getMusicas().getFirst().getNome());
    }

    @Test
    public void deveDesfavoritarMusica() throws PlaylistNotFoundException {

        UUID usuarioId = UUID.randomUUID();
        usuario.setId(usuarioId);
        usuario.setNome("Nome Teste");

        UUID cartaoId = UUID.randomUUID();
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setAtivo(true);
        cartao.setLimite(500.00);
        cartao.setNumero("12345");
        cartao.setUsuario(usuario);
        List<Cartao> cartoes = new ArrayList<>();
        cartoes.add(cartao);
        usuario.setCartoes(cartoes);

        UUID musicaId = UUID.randomUUID();
        Musica musica = new Musica();
        musica.setId(musicaId);
        musica.setNome("Musica Teste");
        musica.setDuracao(120);

        UUID playlistId = UUID.randomUUID();
        Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        playlist.setNome("Favoritas");
        playlist.setPublica(true);
        playlist.setUsuario(usuario);
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);
        usuario.setPlaylists(playlists);

        usuario.favoritarMusica(musica);
        usuario.desfavoritarMusica(musica);

        assertFalse(usuario.getPlaylists().getFirst().getMusicas().contains(musica));
    }

    @Test
    public void deveDarPlaylistNotFoundExceptionAoFavoritar() throws PlaylistNotFoundException {

        UUID usuarioId = UUID.randomUUID();
        usuario.setId(usuarioId);
        usuario.setNome("Nome Teste");

        UUID cartaoId = UUID.randomUUID();
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setAtivo(true);
        cartao.setLimite(500.00);
        cartao.setNumero("12345");
        cartao.setUsuario(usuario);
        List<Cartao> cartoes = new ArrayList<>();
        cartoes.add(cartao);
        usuario.setCartoes(cartoes);

        UUID musicaId = UUID.randomUUID();
        Musica musica = new Musica();
        musica.setId(musicaId);
        musica.setNome("Musica Teste");
        musica.setDuracao(120);

        UUID playlistId = UUID.randomUUID();
        Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        playlist.setNome("Playlist Teste");
        playlist.setPublica(true);
        playlist.setUsuario(usuario);
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);
        usuario.setPlaylists(playlists);

        assertThrows(PlaylistNotFoundException.class, () -> {
            usuario.favoritarMusica(musica);
        });
    }

    @Test
    public void deveDarPlaylistNotFoundExceptionAoDesfavoritar() throws PlaylistNotFoundException {

        UUID usuarioId = UUID.randomUUID();
        usuario.setId(usuarioId);
        usuario.setNome("Nome Teste");

        UUID cartaoId = UUID.randomUUID();
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setAtivo(true);
        cartao.setLimite(500.00);
        cartao.setNumero("12345");
        cartao.setUsuario(usuario);
        List<Cartao> cartoes = new ArrayList<>();
        cartoes.add(cartao);
        usuario.setCartoes(cartoes);

        UUID musicaId = UUID.randomUUID();
        Musica musica = new Musica();
        musica.setId(musicaId);
        musica.setNome("Musica Teste");
        musica.setDuracao(120);

        UUID playlistId = UUID.randomUUID();
        Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        playlist.setNome("Favoritas");
        playlist.setPublica(true);
        playlist.setUsuario(usuario);
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);
        usuario.setPlaylists(playlists);
        usuario.favoritarMusica(musica);
        usuario.getPlaylists().getFirst().setNome("Playlist Teste");

        assertThrows(PlaylistNotFoundException.class, () -> {
            usuario.desfavoritarMusica(musica);
        });
    }

    @Test
    public void deveTestarGettersESetters() {

        UUID usuarioId = UUID.randomUUID();
        usuario.setId(usuarioId);
        usuario.setNome("Nome Teste");

        UUID cartaoId = UUID.randomUUID();
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setAtivo(true);
        cartao.setLimite(500.00);
        cartao.setNumero("12345");
        cartao.setUsuario(usuario);
        List<Cartao> cartoes = new ArrayList<>();
        cartoes.add(cartao);
        usuario.setCartoes(cartoes);

        UUID musicaId = UUID.randomUUID();
        Musica musica = new Musica();
        musica.setId(musicaId);
        musica.setNome("Musica Teste");
        musica.setDuracao(120);
        List<Musica> musicas = new ArrayList<>();
        musicas.add(musica);

        UUID playlistId = UUID.randomUUID();
        Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        playlist.setNome("Favoritas");
        playlist.setPublica(true);
        playlist.setUsuario(usuario);
        playlist.setMusicas(musicas);
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);
        usuario.setPlaylists(playlists);

        UUID planoId = UUID.randomUUID();
        Plano plano = new Plano();
        plano.setId(planoId);
        plano.setNome("Plano Teste");
        plano.setDescricao("Descrição Teste");
        plano.setValor(29.99);

        Date data = Date.from(Instant.now());
        UUID assinaturaId = UUID.randomUUID();
        Assinatura assinatura = new Assinatura();
        assinatura.setId(assinaturaId);
        assinatura.setPlano(plano);
        assinatura.setAtivo(true);
        assinatura.setDtAssinatura(data);
        assinatura.setUsuario(usuario);
        List<Assinatura> assinaturas = new ArrayList<>();
        assinaturas.add(assinatura);
        usuario.setAssinaturas(assinaturas);

        assertEquals(usuarioId, usuario.getId());
        assertEquals("Nome Teste", usuario.getNome());
        assertEquals(playlistId, playlist.getId());
        assertEquals(true, playlist.getPublica());
        assertEquals(usuario, playlist.getUsuario());
        assertEquals(musicas, playlist.getMusicas());
        assertEquals(assinaturaId, assinatura.getId());
        assertEquals(data, assinatura.getDtAssinatura());
        assertEquals(usuario, assinatura.getUsuario());
    }
}

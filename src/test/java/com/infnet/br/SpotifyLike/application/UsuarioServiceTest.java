package com.infnet.br.SpotifyLike.application;

import com.infnet.br.SpotifyLike.application.conta.UsuarioService;
import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import com.infnet.br.SpotifyLike.domain.streaming.Musica;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.domain.transacao.Plano;
import com.infnet.br.SpotifyLike.repository.conta.UsuarioRepository;
import com.infnet.br.SpotifyLike.repository.transacao.PlanoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsuarioServiceTest {

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PlanoRepository planoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void deveCriarUsuario() throws Exception {

        UUID planoId = UUID.randomUUID();
        when(planoRepository.getReferenceById(any())).thenReturn(new Plano(planoId, "Plano Basico", "Plano basico com anuncios",29.99));

        Cartao cartao = new Cartao();
        cartao.setId(UUID.randomUUID());
        cartao.setAtivo(true);
        cartao.setNumero("12345");
        cartao.setLimite(500.00);

        Usuario usuario = usuarioService.criarConta("Usuario Teste", planoId, cartao);

        assertNotNull(usuario);
        assertEquals("Usuario Teste", usuario.getNome());
        assertEquals("12345", usuario.getCartoes().get(0).getNumero());
        assertEquals("Favoritas", usuario.getPlaylists().get(0).getNome());
        assertEquals("Plano Basico", usuario.getAssinaturas().get(0).getPlano().getNome());
    }

    @Test
    public void deveBuscarUsuarioPorId() {

        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        Usuario usuarioTeste = usuarioService.obterUsuario(usuarioId);

        assertNotNull(usuarioTeste);
        assertEquals(usuarioId, usuarioTeste.getId());
    }
}

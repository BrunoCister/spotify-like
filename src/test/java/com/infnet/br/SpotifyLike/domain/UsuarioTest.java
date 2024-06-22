package com.infnet.br.SpotifyLike.domain;

import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import com.infnet.br.SpotifyLike.domain.exceptions.CardNotActiveException;
import com.infnet.br.SpotifyLike.domain.exceptions.ExceedsCardLimitException;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.domain.transacao.Plano;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

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
        assertEquals("12345", usuario.getCartoes().get(0).getNumero());
        assertEquals("Favoritas", usuario.getPlaylists().get(0).getNome());
        assertEquals("Plano Teste", usuario.getAssinaturas().get(0).getPlano().getNome());
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
}

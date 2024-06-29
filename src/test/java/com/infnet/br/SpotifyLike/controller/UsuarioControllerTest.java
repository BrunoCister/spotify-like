package com.infnet.br.SpotifyLike.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.br.SpotifyLike.UsuarioController;
import com.infnet.br.SpotifyLike.application.conta.UsuarioService;
import com.infnet.br.SpotifyLike.domain.conta.Usuario;
import com.infnet.br.SpotifyLike.domain.transacao.Assinatura;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.domain.transacao.Plano;
import com.infnet.br.SpotifyLike.request.CartaoRequest;
import com.infnet.br.SpotifyLike.request.UsuarioRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void deveCriarUsuario() throws Exception {

        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Nome Teste");

        UUID cartaoId = UUID.randomUUID();
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setAtivo(true);
        cartao.setLimite(500.00);
        cartao.setNumero("12345");
        cartao.setUsuario(usuario);

        CartaoRequest cartaoRequest = new CartaoRequest();
        cartaoRequest.setAtivo(true);
        cartaoRequest.setLimite(500.00);
        cartaoRequest.setNumero("12345");

        UUID planoId = UUID.randomUUID();
        Plano plano = new Plano();
        plano.setId(planoId);
        plano.setNome("Plano Basico");
        plano.setDescricao("Plano basico com anuncios");
        plano.setValor(29.99);

        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNome("Nome Teste");
        usuarioRequest.setPlanoId(planoId);
        usuarioRequest.setCartao(cartaoRequest);

        Assinatura assinatura = new Assinatura();
        assinatura.setId(UUID.randomUUID());
        assinatura.setAtivo(true);
        assinatura.setPlano(plano);
        assinatura.setUsuario(usuario);

        usuario.getAssinaturas().add(assinatura);

        when(usuarioService.criarConta(any(String.class), any(UUID.class), any(Cartao.class))).thenReturn(usuario);

        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(usuario.getId().toString()))
                .andExpect(jsonPath("$.nome").value("Nome Teste"));
    }

    @Test
    public void deveObterUsuarioPorId() throws Exception {

        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Nome Teste");

        UUID cartaoId = UUID.randomUUID();
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setAtivo(true);
        cartao.setLimite(500.00);
        cartao.setNumero("12345");
        cartao.setUsuario(usuario);

        CartaoRequest cartaoRequest = new CartaoRequest();
        cartaoRequest.setAtivo(true);
        cartaoRequest.setLimite(500.00);
        cartaoRequest.setNumero("12345");

        UUID planoId = UUID.randomUUID();
        Plano plano = new Plano();
        plano.setId(planoId);
        plano.setNome("Plano Basico");
        plano.setDescricao("Plano basico com anuncios");
        plano.setValor(29.99);

        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNome("Nome Teste");
        usuarioRequest.setPlanoId(planoId);
        usuarioRequest.setCartao(cartaoRequest);

        Assinatura assinatura = new Assinatura();
        assinatura.setId(UUID.randomUUID());
        assinatura.setAtivo(true);
        assinatura.setPlano(plano);
        assinatura.setUsuario(usuario);

        usuario.getAssinaturas().add(assinatura);
        usuario.getCartoes().add(cartao);

        given(usuarioService.obterUsuario(usuarioId)).willReturn(usuario);

        mockMvc.perform(get("/usuario/" + usuario.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(usuario.getNome()))
                .andExpect(jsonPath("$.id").value(usuario.getId().toString()));
    }

    @Test
    public void deveObterUsuarioPorIdBadRequest() throws Exception {

        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("Nome Teste");

        UUID cartaoId = UUID.randomUUID();
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setAtivo(true);
        cartao.setLimite(500.00);
        cartao.setNumero("12345");
        cartao.setUsuario(usuario);

        CartaoRequest cartaoRequest = new CartaoRequest();
        cartaoRequest.setAtivo(true);
        cartaoRequest.setLimite(500.00);
        cartaoRequest.setNumero("12345");

        UUID planoId = UUID.randomUUID();
        Plano plano = new Plano();
        plano.setId(planoId);
        plano.setNome("Plano Basico");
        plano.setDescricao("Plano basico com anuncios");
        plano.setValor(29.99);

        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNome("Nome Teste");
        usuarioRequest.setPlanoId(planoId);
        usuarioRequest.setCartao(cartaoRequest);

        Assinatura assinatura = new Assinatura();
        assinatura.setId(UUID.randomUUID());
        assinatura.setAtivo(true);
        assinatura.setPlano(plano);
        assinatura.setUsuario(usuario);

        usuario.getAssinaturas().add(assinatura);
        usuario.getCartoes().add(cartao);

        given(usuarioService.obterUsuario(usuarioId)).willReturn(any());

        mockMvc.perform(get("/usuario/" + usuario.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
    }
}

package com.infnet.br.SpotifyLike.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.br.SpotifyLike.UsuarioController;
import com.infnet.br.SpotifyLike.application.conta.RabbitMQService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

        when(usuarioService.criarConta(any(String.class), any(UUID.class), any(Cartao.class))).thenReturn(usuario);

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

        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(usuarioId.toString()))
                .andExpect(jsonPath("$.nome").value("Nome Teste"));
    }
}

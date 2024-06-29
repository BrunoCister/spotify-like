package com.infnet.br.SpotifyLike.domain;

import com.infnet.br.SpotifyLike.domain.exceptions.DoubleTransactionException;
import com.infnet.br.SpotifyLike.domain.exceptions.HighFrequencyException;
import com.infnet.br.SpotifyLike.domain.transacao.Cartao;
import com.infnet.br.SpotifyLike.domain.transacao.Transacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartaoTest {

    @InjectMocks
    private Cartao cartao;

    @BeforeEach
    void setUp() {
        cartao = new Cartao();
        cartao.setNumero("12345");
        cartao.setLimite(500.00);
        cartao.setAtivo(true);
    }

    @Test
    public void deveRealizarTransacao() throws Exception {

        UUID transacaoId = UUID.randomUUID();
        Transacao transacao = new Transacao();
        transacao.setId(transacaoId);
        transacao.setValor(100.00);
        transacao.setDescricao("Transacao Teste");
        transacao.setVendedor("Vendedor Teste");
        transacao.setDtTransicao(Date.from(Instant.now()));

        cartao.criarTransacao("Vendedor Teste", "Transacao Teste", 100.00);

        assertEquals(1, cartao.getTransacoes().size());
    }

    @Test
    public void deveDarDoubleTransactionException() {

        for (int i = 0; i < 3; i++) {
            Transacao transacao = new Transacao();
            transacao.setValor(100.00);
            transacao.setDtTransicao(Date.from(Instant.now()));
            transacao.setVendedor("Vendedor Teste");
            transacao.setDtTransicao(Date.from(Instant.now()));
            cartao.getTransacoes().add(transacao);
        }

        assertThrows(DoubleTransactionException.class, () -> {
            cartao.criarTransacao("Vendedor Teste", "Descrição Teste", 100.00);
        });
    }

    @Test
    public void deveDarHighFrequencyException() {

        Transacao transacao1 = new Transacao();
        transacao1.setValor(100.00);
        transacao1.setDtTransicao(Date.from(Instant.now()));
        transacao1.setVendedor("Vendedor 1");
        transacao1.setDtTransicao(Date.from(Instant.now()));
        cartao.getTransacoes().add(transacao1);

        Transacao transacao2 = new Transacao();
        transacao2.setValor(100.00);
        transacao2.setDtTransicao(Date.from(Instant.now()));
        transacao2.setVendedor("Vendedor 2");
        transacao2.setDtTransicao(Date.from(Instant.now()));
        cartao.getTransacoes().add(transacao2);

        Transacao transacao3 = new Transacao();
        transacao3.setValor(100.00);
        transacao3.setDtTransicao(Date.from(Instant.now()));
        transacao3.setVendedor("Vendedor 2");
        transacao3.setDtTransicao(Date.from(Instant.now()));
        cartao.getTransacoes().add(transacao3);

        assertThrows(HighFrequencyException.class, () -> {
            cartao.criarTransacao("Vendedor Teste", "Descrição Teste", 100.00);
        });
    }
}

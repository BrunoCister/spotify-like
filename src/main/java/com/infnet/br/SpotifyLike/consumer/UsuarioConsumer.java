/*package com.infnet.br.SpotifyLike.consumer;

import com.infnet.br.SpotifyLike.request.UsuarioRequest;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "usuario")
public class UsuarioConsumer {


    @RabbitHandler
    public void consumidor(UsuarioRequest usuarioRequest) throws AmqpRejectAndDontRequeueException {

        System.out.println("Bem vindo, " + usuarioRequest.getNome());

    }
}*/

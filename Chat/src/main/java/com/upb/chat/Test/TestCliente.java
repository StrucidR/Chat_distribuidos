package com.upb.chat.Test;

import com.upb.chat.cliente.ChatClient;

public class TestCliente {

    public static void main(String[] args) throws Exception {

        ChatClient cliente =
                new ChatClient(
                        "localhost",
                        5000,
                        "Orlando",
                        mensaje ->
                                System.out.println(
                                        mensaje
                                ));

        cliente.enviarBroadcast(
                "Hola desde cliente"
        );
    }
}
package com.upb.chat.servidor;

import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    private static final int PUERTO = 5000;

    public static void main(String[] args) {

        System.out.println(
                "==================================="
        );

        System.out.println(
                "Servidor iniciado..."
        );

        System.out.println(
                "Puerto: " + PUERTO
        );

        System.out.println(
                "==================================="
        );

        try (ServerSocket servidor =
                     new ServerSocket(PUERTO)) {

            while (true) {

                Socket cliente =
                        servidor.accept();

                System.out.println(
                        "Nueva conexión: "
                                + cliente.getInetAddress()
                );

                ClientHandler handler =
                        new ClientHandler(cliente);

                handler.start();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
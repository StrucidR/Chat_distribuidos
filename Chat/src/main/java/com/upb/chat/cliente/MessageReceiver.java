package com.upb.chat.cliente;

public class MessageReceiver extends Thread {

    private ClientConnection conexion;
    private MessageListener listener;

    public MessageReceiver(
            ClientConnection conexion,
            MessageListener listener) {

        this.conexion = conexion;
        this.listener = listener;
    }

    @Override
    public void run() {

        try {

            while (true) {

                String mensaje =
                        conexion.recibir();

                listener.onMessageReceived(
                        mensaje
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Conexión cerrada."
            );
        }
    }
}
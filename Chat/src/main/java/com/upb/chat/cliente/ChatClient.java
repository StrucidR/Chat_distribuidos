package com.upb.chat.cliente;

public class ChatClient {

    private ClientConnection conexion;

    public ChatClient(
            String host,
            int puerto,
            String usuario,
            MessageListener listener)
            throws Exception {

        conexion =
                new ClientConnection(
                        host,
                        puerto);

        conexion.enviar(
                "LOGIN|" + usuario
        );

        MessageReceiver receiver =
                new MessageReceiver(
                        conexion,
                        listener);

        receiver.start();
    }

    public void enviarBroadcast(
            String mensaje) {

        conexion.enviar(
                "MSG|" + mensaje
        );
    }

    public void enviarPrivado(
            String usuario,
            String mensaje) {

        conexion.enviar(
                "PRIVATE|"
                        + usuario
                        + "|"
                        + mensaje
        );
    }

    public void salir() {

        conexion.enviar("EXIT");

        conexion.cerrar();
    }
}

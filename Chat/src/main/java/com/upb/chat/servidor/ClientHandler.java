package com.upb.chat.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientHandler extends Thread {

    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;

    private String nombreUsuario;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());

            while (true) {

                String mensaje = entrada.readUTF();

                procesarMensaje(mensaje);
            }

        } catch (Exception e) {

            System.out.println(
                    "Cliente desconectado: " + nombreUsuario
            );

        } finally {

            desconectar();
        }
    }

    private void procesarMensaje(String mensaje) {

        String[] partes = mensaje.split("\\|");

        if (partes.length == 0) {
            return;
        }

        switch (partes[0]) {

            case "LOGIN":

                if (partes.length < 2)
                    return;

                nombreUsuario = partes[1];

                if (ClientManager.existeUsuario(nombreUsuario)) {

                    enviarMensaje(
                            "ERROR|Nombre de usuario ya existe"
                    );

                    return;
                }

                ClientManager.agregarCliente(
                        nombreUsuario,
                        this
                );

                System.out.println(
                        nombreUsuario + " conectado."
                );

                ClientManager.enviarBroadcast(
                        "MSG|SERVIDOR|" +
                                nombreUsuario +
                                " se ha conectado."
                );

                break;

            case "MSG":

                if (partes.length < 2)
                    return;

                ClientManager.enviarBroadcast(
                        "MSG|" +
                                nombreUsuario +
                                "|" +
                                partes[1]
                );

                break;

            case "PRIVATE":

                if (partes.length < 3)
                    return;

                String destinatario = partes[1];
                String contenido = partes[2];

                ClientManager.enviarPrivado(
                        nombreUsuario,
                        destinatario,
                        contenido
                );

                break;

            case "EXIT":

                desconectar();

                break;
        }
    }

    public void enviarMensaje(String mensaje) {

        try {

            salida.writeUTF(mensaje);
            salida.flush();

        } catch (IOException e) {

            System.out.println(
                    "Error enviando mensaje"
            );
        }
    }

    private void desconectar() {

        try {

            if (nombreUsuario != null) {

                ClientManager.removerCliente(
                        nombreUsuario
                );

                ClientManager.enviarBroadcast(
                        "MSG|SERVIDOR|"
                                + nombreUsuario
                                + " salió del chat."
                );
            }

            if (entrada != null)
                entrada.close();

            if (salida != null)
                salida.close();

            if (socket != null)
                socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
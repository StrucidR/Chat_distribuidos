package com.upb.chat.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection {

    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream salida;

    public ClientConnection(String host, int puerto) throws IOException {

        socket = new Socket(host, puerto);

        entrada = new DataInputStream(
                socket.getInputStream());

        salida = new DataOutputStream(
                socket.getOutputStream());
    }

    public void enviar(String mensaje) {

        try {

            salida.writeUTF(mensaje);
            salida.flush();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public String recibir() throws IOException {

        return entrada.readUTF();
    }

    public void cerrar() {

        try {

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

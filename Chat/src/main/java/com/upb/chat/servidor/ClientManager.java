package com.upb.chat.servidor;

import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {

    private static final ConcurrentHashMap<String, ClientHandler> clientes =
            new ConcurrentHashMap<>();

    public static void agregarCliente(String usuario, ClientHandler handler) {
        clientes.put(usuario, handler);
        enviarListaUsuarios();
    }

    public static void removerCliente(String usuario) {
        clientes.remove(usuario);
        enviarListaUsuarios();
    }

    public static boolean existeUsuario(String usuario) {
        return clientes.containsKey(usuario);
    }

    public static void enviarBroadcast(String mensaje) {

        for (ClientHandler cliente : clientes.values()) {
            cliente.enviarMensaje(mensaje);
        }
    }

    public static void enviarPrivado(
            String remitente,
            String destinatario,
            String mensaje) {

        ClientHandler destino = clientes.get(destinatario);

        if (destino != null) {
            destino.enviarMensaje(
                    "PRIVATE|" + remitente + "|" + mensaje
            );
        }
    }

    public static void enviarListaUsuarios() {

        StringBuilder sb = new StringBuilder("USERS|");

        for (String usuario : clientes.keySet()) {
            sb.append(usuario).append(",");
        }

        String lista = sb.toString();

        if (lista.endsWith(",")) {
            lista = lista.substring(0, lista.length() - 1);
        }

        for (ClientHandler cliente : clientes.values()) {
            cliente.enviarMensaje(lista);
        }
    }
}
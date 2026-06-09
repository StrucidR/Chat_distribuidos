package com.upb.chat.gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PrivateChatFrame extends JFrame {

    private JTextArea areaMensajes;

    private JTextField txtMensaje;

    private JButton btnEnviar;

    private String usuarioDestino;

    private MainChatFrame principal;

    public PrivateChatFrame(
            String usuarioDestino,
            MainChatFrame principal) {

        this.usuarioDestino = usuarioDestino;

        this.principal = principal;

        setTitle(
                "Chat Privado - "
                        + usuarioDestino
        );

        setSize(400, 400);

        setLocationRelativeTo(null);

        inicializar();

        setVisible(true);
    }

    private void inicializar() {

        setLayout(new BorderLayout());

        areaMensajes = new JTextArea();

        areaMensajes.setEditable(false);

        add(
                new JScrollPane(areaMensajes),
                BorderLayout.CENTER
        );

        JPanel inferior =
                new JPanel(
                        new BorderLayout()
                );

        txtMensaje =
                new JTextField();

        btnEnviar =
                new JButton("Enviar");

        inferior.add(
                txtMensaje,
                BorderLayout.CENTER
        );

        inferior.add(
                btnEnviar,
                BorderLayout.EAST
        );

        add(
                inferior,
                BorderLayout.SOUTH
        );

        btnEnviar.addActionListener(
                e -> enviar()
        );
    }

    private void enviar() {

        String mensaje =
                txtMensaje.getText().trim();

        if(mensaje.isEmpty()) {
            return;
        }

        principal.enviarPrivado(
                usuarioDestino,
                mensaje
        );

        areaMensajes.append(
                "Yo: "
                        + mensaje
                        + "\n"
        );

        txtMensaje.setText("");
    }

    public void agregarMensaje(
            String remitente,
            String mensaje) {

        areaMensajes.append(
                remitente
                        + ": "
                        + mensaje
                        + "\n"
        );
    }
}   
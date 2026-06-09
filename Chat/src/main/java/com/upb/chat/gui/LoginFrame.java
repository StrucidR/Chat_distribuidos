package com.upb.chat.gui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtNombre;
    private JTextField txtServidor;
    private JButton btnConectar;

    public LoginFrame() {

        setTitle("Ingreso al Chat");

        setSize(350, 180);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();

        setVisible(true);
    }

    private void inicializarComponentes() {

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Usuario:"));

        txtNombre = new JTextField();

        panel.add(txtNombre);

        panel.add(new JLabel("Servidor:"));

        txtServidor = new JTextField("localhost");

        panel.add(txtServidor);

        panel.add(new JLabel());

        btnConectar = new JButton("Conectar");

        panel.add(btnConectar);

        add(panel);

        btnConectar.addActionListener(e -> conectar());
    }

    private void conectar() {

        String usuario = txtNombre.getText().trim();

        String servidor = txtServidor.getText().trim();

        if(usuario.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un nombre"
            );

            return;
        }

        new MainChatFrame(
                usuario,
                servidor
        );

        dispose();
    }
}
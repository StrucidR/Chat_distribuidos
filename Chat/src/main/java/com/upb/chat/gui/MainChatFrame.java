package com.upb.chat.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.upb.chat.cliente.ChatClient;
import com.upb.chat.cliente.MessageListener;

public class MainChatFrame
        extends JFrame
        implements MessageListener {

    private JTextArea areaChat;

    private JTextField txtMensaje;

    private JButton btnEnviar;

    private DefaultListModel<String> modeloUsuarios;

    private JList<String> listaUsuarios;

    private ChatClient cliente;

    private String usuario;

    private HashMap<String,
            PrivateChatFrame> chatsPrivados =
            new HashMap<>();

    public MainChatFrame(
            String usuario,
            String servidor) {

        this.usuario = usuario;

        setTitle(
                "Sala General - "
                        + usuario
        );

        setSize(700, 500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        inicializarComponentes();

        conectarServidor(
                servidor
        );

        setVisible(true);
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        areaChat = new JTextArea();

        areaChat.setEditable(false);

        add(
                new JScrollPane(areaChat),
                BorderLayout.CENTER
        );

        JPanel derecha =
                new JPanel(
                        new BorderLayout()
                );

        derecha.add(
                new JLabel("Usuarios"),
                BorderLayout.NORTH
        );

        modeloUsuarios =
                new DefaultListModel<>();

        listaUsuarios =
                new JList<>(modeloUsuarios);

        derecha.add(
                new JScrollPane(listaUsuarios),
                BorderLayout.CENTER
        );

        add(
                derecha,
                BorderLayout.EAST
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
                e -> enviarBroadcast()
        );

        listaUsuarios.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e) {

                        if(e.getClickCount() == 2) {

                            abrirChatPrivado();
                        }
                    }
                });
    }

    private void conectarServidor(
            String servidor) {

        try {

            cliente =
                    new ChatClient(
                            servidor,
                            5000,
                            usuario,
                            this
                    );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible conectar"
            );

            System.exit(0);
        }
    }

    private void enviarBroadcast() {

        String mensaje =
                txtMensaje.getText().trim();

        if(mensaje.isEmpty()) {
            return;
        }

        cliente.enviarBroadcast(
                mensaje
        );

        txtMensaje.setText("");
    }

    public void enviarPrivado(
            String destino,
            String mensaje) {

        cliente.enviarPrivado(
                destino,
                mensaje
        );
    }

    private void abrirChatPrivado() {

        String seleccionado =
                listaUsuarios.getSelectedValue();

        if(seleccionado == null
                || seleccionado.equals(usuario)) {

            return;
        }

        if(!chatsPrivados.containsKey(
                seleccionado)) {

            chatsPrivados.put(
                    seleccionado,
                    new PrivateChatFrame(
                            seleccionado,
                            this
                    )
            );
        }

        chatsPrivados.get(
                seleccionado
        ).setVisible(true);
    }

    @Override
    public void onMessageReceived(
            String mensaje) {

        SwingUtilities.invokeLater(() -> {

            procesarMensaje(
                    mensaje
            );

        });
    }

    private void procesarMensaje(
            String mensaje) {

        String[] partes =
                mensaje.split("\\|");

        if(partes.length == 0) {
            return;
        }

        switch(partes[0]) {

            case "MSG":

                areaChat.append(
                        partes[1]
                                + ": "
                                + partes[2]
                                + "\n"
                );

                break;

            case "USERS":

                modeloUsuarios.clear();

                if(partes.length > 1) {

                    String[] usuarios =
                            partes[1].split(",");

                    for(String u : usuarios) {

                        modeloUsuarios.addElement(u);
                    }
                }

                break;

            case "PRIVATE":

                String remitente =
                        partes[1];

                String contenido =
                        partes[2];

                if(!chatsPrivados.containsKey(
                        remitente)) {

                    chatsPrivados.put(
                            remitente,
                            new PrivateChatFrame(
                                    remitente,
                                    this
                            )
                    );
                }

                chatsPrivados.get(
                        remitente
                ).agregarMensaje(
                        remitente,
                        contenido
                );

                break;

            case "ERROR":

                JOptionPane.showMessageDialog(
                        this,
                        partes[1]
                );

                break;
        }
    }
}
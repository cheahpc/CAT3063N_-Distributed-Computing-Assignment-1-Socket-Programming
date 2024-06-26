import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class ClientUI extends JFrame {
    private Socket socket;
    private Client client;

    private JFrame jFrame;
    private JPanel jPanel;
    private JLabel lblTargetIP, lblTargetPort, lblUserName, lblMessages;
    private static JLabel lblMyAddress, lblServerStatus;
    private static JTextField txtFieldTargetIP, txtFieldTargetPort, txtFieldUsername, txtFieldMessageBox;
    private JButton btnClear;
    private static JButton btnConnection, btnSend;
    private static JTextArea txtAreaMessage;
    private JScrollPane scroll;

    private static boolean isServerConnected = false;

    private GridBagConstraints setGBC(int x, int y, int px, int py, int gWidth, int gHeight, int to, int le, int bo,
            int ri) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.ipadx = px;
        gbc.ipady = py;
        gbc.gridwidth = gWidth;
        gbc.gridheight = gHeight;
        gbc.insets.set(to, le, bo, ri);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void uiCreate() {
        jFrame = new JFrame("PC.Chat (Client) Developed by: @Pin Chee Ver1.0");
        jPanel = new JPanel();

        lblTargetIP = new JLabel("Target IP:");
        lblTargetPort = new JLabel("Target Port:");
        lblUserName = new JLabel("User Name:");
        lblMyAddress = new JLabel("My Address: " + Client.getClientIP() + ":?");
        lblServerStatus = new JLabel("Server: Disconnected");

        lblMessages = new JLabel("Messages:");

        txtFieldTargetIP = new JTextField("", 20);
        txtFieldTargetPort = new JTextField("", 10);
        txtFieldUsername = new JTextField("", 20);
        txtFieldMessageBox = new JTextField();

        txtAreaMessage = new JTextArea(16, 60);

        btnConnection = new JButton("Connect");
        btnSend = new JButton("Send");
        btnClear = new JButton("Clear");

        scroll = new JScrollPane(txtAreaMessage);
    }

    private void uiSettings() {
        jFrame.setSize(950, 650);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jPanel.setLayout(new GridBagLayout());
        jPanel.setBackground(java.awt.Color.lightGray);

        lblServerStatus.setForeground(java.awt.Color.RED);

        btnConnection.setBackground(java.awt.Color.blue);
        btnConnection.setForeground(java.awt.Color.white);

        txtAreaMessage.setEditable(false);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Set font size
        lblTargetIP.setFont(lblTargetIP.getFont().deriveFont(16.0f));
        lblTargetPort.setFont(lblTargetPort.getFont().deriveFont(16.0f));
        lblUserName.setFont(lblUserName.getFont().deriveFont(16.0f));
        lblMyAddress.setFont(lblMyAddress.getFont().deriveFont(16.0f));
        lblServerStatus.setFont(lblServerStatus.getFont().deriveFont(16.0f));
        lblMessages.setFont(lblMessages.getFont().deriveFont(16.0f));

        txtFieldTargetIP.setFont(txtFieldTargetIP.getFont().deriveFont(16.0f));
        txtFieldTargetPort.setFont(txtFieldTargetPort.getFont().deriveFont(16.0f));
        txtFieldUsername.setFont(txtFieldUsername.getFont().deriveFont(16.0f));
        txtFieldMessageBox.setFont(txtFieldMessageBox.getFont().deriveFont(16.0f));

        txtAreaMessage.setFont(txtAreaMessage.getFont().deriveFont(16.0f));

        btnClear.setFont(btnClear.getFont().deriveFont(16.0f));
        btnConnection.setFont(btnConnection.getFont().deriveFont(16.0f));
        btnSend.setFont(btnSend.getFont().deriveFont(16.0f));

    }

    private void uiAdd() {
        // Add components to panel
        jPanel.add(lblTargetIP, setGBC(0, 0, 0, 5, 1, 1, 0, 5, 0, 0));
        jPanel.add(lblTargetPort, setGBC(1, 0, 0, 5, 1, 1, 0, 5, 0, 0));
        jPanel.add(lblUserName, setGBC(2, 0, 0, 5, 1, 1, 0, 5, 0, 0));

        jPanel.add(txtFieldTargetIP, setGBC(0, 1, 0, 5, 1, 1, 0, 5, 0, 0));
        jPanel.add(txtFieldTargetPort, setGBC(1, 1, 0, 5, 1, 1, 0, 5, 0, 0));
        jPanel.add(txtFieldUsername, setGBC(2, 1, 0, 5, 1, 1, 0, 5, 0, 0));
        jPanel.add(btnConnection, setGBC(3, 1, 0, 5, 1, 1, 0, 5, 0, 0));

        jPanel.add(lblMyAddress, setGBC(0, 2, 0, 5, 1, 1, 0, 5, 0, 0));
        jPanel.add(lblServerStatus, setGBC(2, 2, 0, 5, 1, 1, 0, 5, 0, 0));

        jPanel.add(lblMessages, setGBC(0, 3, 0, 5, 1, 1, 0, 5, 0, 0));
        jPanel.add(btnClear, setGBC(3, 3, 0, 5, 1, 1, 0, 5, 0, 0));

        jPanel.add(scroll, setGBC(0, 4, 0, 5, 4, 1, 10, 5, 10, 0));

        jPanel.add(txtFieldMessageBox, setGBC(0, 5, 0, 5, 3, 1, 0, 5, 0, 0));
        jPanel.add(btnSend, setGBC(3, 5, 0, 5, 1, 1, 0, 5, 0, 0));

        jFrame.add(jPanel);
    }

    private void actSend() {
        String message = txtFieldMessageBox.getText();
        if (!message.isEmpty()) {
            logAppend("@You: " + message);
            client.sendMessage(message);
            txtFieldMessageBox.setText("");
        }
    }

    private void checkFields() {
        if (txtFieldTargetIP.getText().isEmpty()) {
            logAppend("#-System: Target IP is set to default Localhost IP (127.0.0.1)");
            txtFieldTargetIP.setText("127.0.0.1");
        }

        if (txtFieldTargetPort.getText().isEmpty()) {
            logAppend("#-System: Target Port is set to default port (8888)");
            txtFieldTargetPort.setText("8888");
        }

        if (txtFieldUsername.getText().isEmpty()) {
            logAppend("#-System: Username is set to default (Anonymous)");
            txtFieldUsername.setText("Anonymous");
        }
    }

    public ClientUI() {
        uiCreate();
        uiSettings();
        uiAdd();

        jFrame.setVisible(true);
        // Add action listeners
        btnConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get latest local IP address
                lblMyAddress.setText("My Address: " + Client.getClientIP() + ":?");

                if (isServerConnected) {
                    logAppend("#-System: Disconnecting from server...");
                    client.closeConnection();
                    setServerConnected(false);
                } else {
                    // Step 1: Check for empty fields
                    checkFields();
                    // Step 2: Get values from text fields
                    String username = txtFieldUsername.getText();
                    int port = Integer.parseInt(txtFieldTargetPort.getText());
                    String targetIP = txtFieldTargetIP.getText();
                    // Step 3: Connect to server
                    logAppend("#-System: Connecting to server...");
                    try {
                        socket = new Socket(targetIP, port);
                        setServerConnected(true);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        logAppend("#-System: Connection failed. Server not found.");
                        setServerConnected(false);
                        return;
                    }
                    client = new Client(socket);
                    String userID = Client.getClientIP() + ":" + client.getClientPort() + " @" + username;
                    client.sendMessage(userID);

                    // Step 4: Listen for messages
                    client.listenForMessage();

                    // Step 5: Update status
                    logAppend("#-System: Connected to server. Enjoy chatting!");
                    lblMyAddress.setText("My Address: " + Client.getClientIP() + ":" + client.getClientPort());
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logClear();
            }
        });

        txtFieldMessageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actSend();
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actSend();
            }
        });
    }

    public static void logClear() {
        txtAreaMessage.setText("");
    }

    public static void logAppend(String message) {
        txtAreaMessage.append(message + "\n");
    }

    public static void setServerConnected(boolean isOnline) {
        if (isOnline) {
            isServerConnected = true;
            lblServerStatus.setText("Server: Connected");
            lblServerStatus.setForeground(java.awt.Color.GREEN);
            btnConnection.setText("Disconnect");
            btnConnection.setBackground(java.awt.Color.red);
            btnSend.setEnabled(true);
            txtFieldMessageBox.setEnabled(true);
            txtFieldTargetIP.setEnabled(false);
            txtFieldTargetPort.setEnabled(false);
            txtFieldUsername.setEnabled(false);
        } else {
            isServerConnected = false;
            lblServerStatus.setText("Server: Disconnected");
            lblServerStatus.setForeground(java.awt.Color.RED);
            btnConnection.setText("Connect");
            btnConnection.setBackground(java.awt.Color.blue);
            btnConnection.setForeground(java.awt.Color.white);
            btnSend.setEnabled(false);
            txtFieldMessageBox.setEnabled(false);
            txtFieldTargetIP.setEnabled(true);
            txtFieldTargetPort.setEnabled(true);
            txtFieldUsername.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        new ClientUI();
    }
}
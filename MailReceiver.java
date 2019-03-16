import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class MailReceiver extends JFrame {

    private JTextField server;
    private JTextField username;
    private JTextField password;
    private JTextField from;
    private JTextField to;
    private JTextField subject;
    private JButton viewMail;
    private JTextArea display;
    private Session session;

    public MailReceiver(){
        setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        setSize(600, 400);
        JPanel north = new JPanel();
        GridLayout gl = new GridLayout(5, 2);
        north.setMaximumSize(new Dimension(500, 100));
        north.setLayout(gl);
        add(north);

        JLabel serverLabel = new JLabel("Server");
        north.add(serverLabel);
        server = new JTextField("pop.gmail.com",2);
        north.add(server);
        JLabel usernameLabel = new JLabel("Username");
        north.add(usernameLabel);
        username = new JTextField(2);
        north.add(username);
        JLabel pwLabel = new JLabel("Password");
        north.add(pwLabel);
        password = new JTextField( 2);
        north.add(password);


        display = new JTextArea();
        JScrollPane scroll = new JScrollPane(display);
        scroll.setMinimumSize(new Dimension(500, 350));
        add(scroll, BorderLayout.SOUTH);
        display.setEditable(true);
        display.setVisible(true);

        viewMail = new JButton("View mail");
        viewMail.addActionListener(new viewMailListener());
        add(viewMail);



        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);


    }


    class viewMailListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Properties props = new Properties();
            props.put("mail.pop3.host", server.getText());
            props.put("mail.pop3.port", "995");
            props.put("mail.pop3.starttls.enable", "true");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username.getText(), password.getText());
                        }
                    });
            try {
                Store store = session.getStore("pop3s");
                store.connect(server.getText(), username.getText(), password.getText());
                Folder inbox = store.getFolder("Inbox");
                inbox.open(Folder.READ_ONLY);
                Message[] messages = inbox.getMessages();
                printEmails(messages);
            } catch (MessagingException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void printEmails(Message[] messages) throws MessagingException {
        for(int i = 0; i < messages.length; i++){
            Message message = messages[i];
            System.out.println("Message: " + (i+1));
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println(message.getSubject());
        }
    }

    public static void main(String[] args) {
        MailReceiver mailReceiver = new MailReceiver();
    }
}


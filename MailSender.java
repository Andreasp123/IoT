import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Properties;

public class MailSender extends JFrame{

    private JTextField server;
    private JTextField username;
    private JTextField password;
    private JTextField from;
    private JTextField to;
    private JTextField subject;
    private JButton sendBtn;
    private JTextArea display;
    private Session session;

    public MailSender(){
        setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        setSize(600, 400);
        JPanel north = new JPanel();
        GridLayout gl = new GridLayout(5, 2);
        north.setMaximumSize(new Dimension(500, 100));
        north.setLayout(gl);
        add(north);

        server = new JTextField("Server",2);
        north.add(server);
        username = new JTextField("Username",2);
        north.add(username);
        password = new JTextField("Password", 2);
        north.add(password);
        from = new JTextField("From");
        north.add(from);
        to = new JTextField("To");
        north.add(to);
        subject = new JTextField("Subject");
        north.add(subject);

        display = new JTextArea();
        JScrollPane scroll = new JScrollPane(display);
        scroll.setMinimumSize(new Dimension(500, 350));
        add(scroll, BorderLayout.SOUTH);
        display.setEditable(true);
        display.setVisible(true);

        sendBtn = new JButton("Send");
        sendBtn.addActionListener(new AddListener());
        add(sendBtn);



        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);





    }
    /*

     */

    class AddListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", server.getText());
            props.put("mail.smtp.port", "587");
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username.getText(), password.getText());
                        }
                    });
            MimeMessage message = new MimeMessage(session);
            try {
                message.setText(display.getText());
                message.setSubject(subject.getText());
                Address address = new InternetAddress(from.getText());
                message.setFrom(address);
                Address toAddress = new InternetAddress(to.getText());
                message.addRecipient(Message.RecipientType.TO, toAddress);
                message.saveChanges();


                Transport.send(message);
            } catch (MessagingException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MailSender ms = new MailSender();
    }
}

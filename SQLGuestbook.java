package Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Not a GUI expert as is probably apparent
public class SQLGuestbook extends JFrame{
    private Connection connection;
    private String server = "localhost:3306";
    private String db = "iot";
    private String username = "root";
    private String password = "";
    private String url = "jdbc:mysql://" + server + "/" + db;



    private JTextField name ;
    private JTextField email;
    private JTextField homepage;
    private JTextField comment;
    private JButton addButton;
    private JTextArea display;

    public SQLGuestbook() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        setSize(500, 500);
        JPanel north = new JPanel();
        GridLayout gl = new GridLayout(5,2);
        north.setMaximumSize(new Dimension(500, 100));
        north.setLayout(gl);
        add(north);

        name = new JTextField("Name",2);
        name.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(name.getText().equals("Name")){
                    name.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(name.getText().equals("")){
                    name.setText("Name");
                }
            }
        });
        north.add(name);
        email = new JTextField("Email",2);
        email.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(email.getText().equals("Email")){
                    email.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(email.getText().equals("")){
                    email.setText("Email");
                }
            }
        });
        north.add(email);

        homepage = new JTextField("Homepage", 2);
        homepage.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(homepage.getText().equals("Homepage")){
                    homepage.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(homepage.getText().equals("")){
                    homepage.setText("Homepage");
                }
            }
        });
        north.add(homepage);

        comment = new JTextField("Comment");
        comment.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(comment.getText().equals("Comment")){
                    comment.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(comment.getText().equals("")){
                    comment.setText("Comment");
                }
            }
        });
        north.add(comment);
        addButton = new JButton("Add");
        addButton.addActionListener(new AddListener());
        north.add(addButton);

        display = new JTextArea(20,60);
        JScrollPane scroll = new JScrollPane(display);
        scroll.setMinimumSize(new Dimension(500, 350));
        add(scroll, BorderLayout.SOUTH);
        display.setEditable(false);
        display.setVisible(true);



        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(url, username, password);
        if(connection == null){
            System.out.println("unfortunately");
        }
        readComments();

    }

    class AddListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String namee = name.getText();
            String mail = email.getText();
            String homep = homepage.getText();
            String com = comment.getText();
            String query = "INSERT INTO iot (name, email, homepage, comment) VALUES ('" + namee + "', '" + mail + "', '" + homep + "', '" + com + "')";
            try (
                Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public String checkForHtml(String var){
        Pattern p = Pattern.compile("<.*>");
        Matcher m = p.matcher("");
        if(m.reset(var).matches()){
            return "censur";
        }
        return var;
    }



    public void readComments() throws SQLException {
        display.setText("");
        String query = "SELECT * FROM iot;";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        System.out.println(rs);
        System.out.println(rs.getFetchSize());
        while(rs.next()){
            String name = rs.getString("name");
            name = checkForHtml(name);
            String email = rs.getString("email");
            String homepage = rs.getString("homepage");
            String comment = rs.getString("comment");
            display.append("NAME: ");
            display.append(name);
            display.append(" EMAIL: ");
            display.append(email);
            display.append(" HOMEPAGE: ");
            display.append(homepage + "\n");
            display.append("COMMENT: ");
            display.append(comment + "\n");
        }

    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        SQLGuestbook sqlGuestbook = new SQLGuestbook();

    }
}

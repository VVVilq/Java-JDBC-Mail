package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class EmailHandler {

    @FXML
    private TextArea emailArea;

    @FXML
    private PasswordField emailPassowrd;

    @FXML
    private TextField receiverEmail;
    @FXML
    private TextField email;
    @FXML
    private TextField emailTopic;
    @FXML
    private ChoiceBox ddMenu;

    private ObservableList<String> choices;

    @FXML
    public void initialize(){

        generateDropDownMenu();
    }

    public  void goBack (ActionEvent event) throws IOException {
        emailArea.clear();
        email.clear();
        receiverEmail.clear();
        emailTopic.clear();
        receiverEmail.clear();

        Parent tableViewParent = FXMLLoader.load(getClass().getResource("TableViewScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    public void generateDropDownMenu(){
        choices=FXCollections.observableArrayList();
        try {
            Statement st = Controller.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT email FROM dziekanat.osobiste WHERE email IS NOT NULL ");
            while(rs.next()){

                choices.add(rs.getString(1));
            }
            ddMenu.setItems(choices);
            ddMenu.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        receiverEmail.setText(choices.get(observable.getValue().intValue()));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setOnPick(){
       // ddMenu.get;
    }

    public void sendEmail(){

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email.getText(),emailPassowrd.getText());
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getText()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receiverEmail.getText()));
            message.setSubject(emailTopic.getText());
            message.setText(emailArea.getText());

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}

package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {
    @FXML
    private TextField dbName;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private GridPane menu;

    private static Connection connection;






    public void Connect () {
        this.connection = makeConnection();
    }

    public static Connection getConnection() {
        return(connection);
    }

    public Connection makeConnection() {
        String errorMessage;
        String url = "jdbc:postgresql://" + host.getText() +":"+port.getText()+ "/" + dbName.getText();

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url, userName.getText(), password.getText());


            return(con);
        }
        catch(ClassNotFoundException cnfe)
        {
            System.err.println("Blad ladowania sterownika: " + cnfe);
            errorMessage="Blad ladowania sterownika";
            AlertBox.display(errorMessage);

            return(null);
        }
        catch(SQLException sqle)
        {

            System.err.println("Blad przy nawiązywaniu polaczenia: " + sqle);
            errorMessage="Blad przy nawiązywaniu polaczenia";
            AlertBox.display(errorMessage);

            return(null);
        }
    }

    public void logToDatabase(ActionEvent event) throws IOException, SQLException {
        System.out.println("Connected");

        this.Connect();
        if(connection.isValid(10)) {
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("TableViewScene.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }

    }
}

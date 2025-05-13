package fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author cupom
 */
public class AppViewController implements Initializable {

    @FXML
    private AnchorPane LoginAnchorPane;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button accediButton;
    @FXML
    private Button passaARegistratiButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void accedi(ActionEvent event) {
    }

    @FXML
    private void passaARegistrati(ActionEvent event) {
    }
    
}

package uam.com.ni.clientesolicitud.components;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class CustomInput extends VBox {

    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtInput;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnTogglePassword;

    @FXML
    private FontIcon iconEye;

    private boolean isPassword = false;
    private boolean passwordVisible = false;

    public CustomInput() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("custom-input.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Error al cargar custom-input.fxml", exception);
        }

        txtInput.textProperty().bindBidirectional(txtPassword.textProperty());
    }

    @FXML
    private void onTogglePassword() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            txtInput.setVisible(true);
            txtInput.setManaged(true);
            iconEye.setIconLiteral("fas-eye-slash");
            txtInput.requestFocus();
            txtInput.positionCaret(txtInput.getText().length());
        } else {
            txtInput.setVisible(false);
            txtInput.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            iconEye.setIconLiteral("fas-eye");
            txtPassword.requestFocus();
            txtPassword.positionCaret(txtPassword.getText().length());
        }
    }

    public void setIsPassword(boolean value) {
        this.isPassword = value;
        if (value) {
            txtInput.setVisible(false);
            txtInput.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            if (btnTogglePassword != null) {
                btnTogglePassword.setVisible(true);
                btnTogglePassword.setManaged(true);
            }
        } else {
            txtInput.setVisible(true);
            txtInput.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            if (btnTogglePassword != null) {
                btnTogglePassword.setVisible(false);
                btnTogglePassword.setManaged(false);
            }
        }
    }

    public boolean getIsPassword() {
        return isPassword;
    }

    public void setLabelText(String value) {
        lblTitle.setText(value);
    }

    public String getLabelText() {
        return lblTitle.getText();
    }

    public void setPromptText(String value) {
        txtInput.setPromptText(value);
        txtPassword.setPromptText(value);
    }

    public String getPromptText() {
        return txtInput.getPromptText();
    }

    public String getText() {
        return txtInput.getText() != null ? txtInput.getText() : "";
    }

    public void setText(String value) {
        txtInput.setText(value);
    }

    public void clear() {
        txtInput.clear();
    }

    public void selectAll() {
        if (isPassword && !passwordVisible) {
            txtPassword.selectAll();
        } else {
            txtInput.selectAll();
        }
    }

    public void requestFocusInput() {
        if (isPassword && !passwordVisible) {
            txtPassword.requestFocus();
        } else {
            txtInput.requestFocus();
        }
    }

    public StringProperty textProperty() {
        return txtInput.textProperty();
    }

    public TextField getTextField() {
        return txtInput;
    }

    public PasswordField getPasswordField() {
        return txtPassword;
    }
}

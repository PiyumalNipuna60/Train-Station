package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.SignIn;
import util.CrudUtil;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SignInFromController {
    public TextField txtFName;
    public TextField txtLName;
    public TextField txtAddress;
    public TextField txtContact;
    public TextField txtAge;
    public ComboBox cmbGender;
    public TextField txtUserName;
    public TextField txtPassword;
    public Button btnCancel;
    public Button btnSignIn;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        ObservableList<String> obList= FXCollections.observableArrayList();
        obList.add("Male");
        obList.add("Female");
        obList.add("Custom");
        cmbGender.setItems(obList);

        btnSignIn.setDisable(true);

        Pattern pattenFName = Pattern.compile("^[A-z ]{3,}$");
        Pattern pattenLName = Pattern.compile("^[A-z ]{3,}$");
        Pattern pattenAddress = Pattern.compile("^[A-z0-9 ,/]{5,}$");
        Pattern pattenTel = Pattern.compile("^(071|072|077|076|078|075)[0-9]{7}$");
        Pattern pattenAge = Pattern.compile("^[0-9]{2}$");
        Pattern pattenUserName = Pattern.compile("^[A-z0-9]{8}$");
        Pattern pattenPassword = Pattern.compile("^[A-z0-9]{5}$");

        map.put(txtFName,pattenFName);
        map.put(txtLName,pattenLName);
        map.put(txtAddress,pattenAddress);
        map.put(txtContact,pattenTel);
        map.put(txtAge,pattenAge);
        map.put(txtUserName,pattenUserName);
        map.put(txtPassword,pattenPassword);

    }

    public void btnSignInOnAction() {
        SignIn s=new SignIn(
                txtFName.getText(),txtLName.getText(),txtAddress.getText(),txtContact.getText(),txtAge.getText(),
                (String) cmbGender.getValue(),txtUserName.getText(),txtPassword.getText()
        );
        try {
            if (CrudUtil.executeUpdate("INSERT INTO `admin` VALUE(?,?,?,?,?,?,?,?)",s.getfName(),s.getlName(),
                    s.getAddress(),s.getContact(),s.getAge(),s.getGender(),s.getUserName(),s.getPassword())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
                clear();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        Stage window = (Stage) btnCancel.getScene().getWindow();
        window.close();
    }

    public void signOnKeyReleased(KeyEvent keyEvent) {
        Validation();


        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object responds = Validation();

            if (responds instanceof TextField) {
                TextField textField = (TextField) responds;
                textField.requestFocus();
            } else {
                btnSignInOnAction();
            }
        }
    }

    private Object Validation() {
        for (TextField key: map.keySet()
             ) {
            Pattern value=map.get(key);
            if (!value.matcher(key.getText()).matches()){
                addError(key);
                return key;
            }else
                removeError(key);
        }
     return true;
    }

    private void removeError(TextField key) {
        key.setStyle("-fx-border-color: blue");
        btnSignIn.setDisable(false);
    }

    private void addError(TextField key) {
        if (key.getText().length()>0){
            key.setStyle("-fx-border-color: red");
        }
        btnSignIn.setDisable(true);
    }

    public void clear(){
        txtFName.clear();
        txtLName.clear();
        txtAddress.clear();
        txtContact.clear();
        txtAge.clear();
        cmbGender.getSelectionModel().clearSelection();
        txtUserName.clear();
        txtPassword.clear();
    }
}

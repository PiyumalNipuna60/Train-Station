package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Train;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ModifyTrainFromController {
    public Button btnRemoveTrain1;
    public Button btnBack;
    public ComboBox cmbTrainTo;
    public ComboBox cmbTrainFrom;
    public TextField txtTrainId;
    public TextField txtTrainName;
    public Button btnUpdateTrain;
    public TextField txtStartTime;
    public TextField txtEndTime;
    public AnchorPane trainAnchorPane;
    public TableView tblAllTrain;
    public TableColumn colTrainTo;
    public TableColumn colTrainFrom;
    public TableColumn colTrainId;
    public TableColumn colTrainName;
    public TableColumn colTrainStartTime;
    public TableColumn colTrainEndTime;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {

        trainFrom();
        trainTo();

        btnUpdateTrain.setDisable(true);
        btnRemoveTrain1.setDisable(true);

        Pattern pattenId = Pattern.compile("^(T00-)[0-9]{3,5}$");
        Pattern pattenName = Pattern.compile("^[A-z ]{3,}$");
        Pattern patternStartTime = Pattern.compile("^([01]?[0-9]|2[0-3]).[0-5][0-9]$");
        Pattern pattenEndTime = Pattern.compile("^([01]?[0-9]|2[0-3]).[0-5][0-9]$");

        map.put(txtTrainId, pattenId);
        map.put(txtTrainName, pattenName);
        map.put(txtStartTime, patternStartTime);
        map.put(txtEndTime, pattenEndTime);

        colTrainId.setCellValueFactory(new PropertyValueFactory("trainId"));
        colTrainName.setCellValueFactory(new PropertyValueFactory("trainName"));
        colTrainStartTime.setCellValueFactory(new PropertyValueFactory("startTime"));
        colTrainEndTime.setCellValueFactory(new PropertyValueFactory("endTime"));
        colTrainFrom.setCellValueFactory(new PropertyValueFactory("trainFrom"));
        colTrainTo.setCellValueFactory(new PropertyValueFactory("trainTo"));


        try {
            loadAllTrain();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllTrain() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.executeQuery("SELECT * FROM train");
        ObservableList<Train> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Train(
                            result.getString("trainId"),
                            result.getString("trainName"),
                            result.getString("startTime"),
                            result.getString("endTime"),
                            result.getString("trainFrom"),
                            result.getString("trainTo")
                    )
            );
        }
        tblAllTrain.setItems(obList);
    }


    public void btnUpdateTrainOnAction() {
        Train c = new Train(
                txtTrainId.getText(), txtTrainName.getText(), txtStartTime.getText(), txtEndTime.getText(), cmbTrainFrom.getValue(), cmbTrainTo.getValue());

        try {
            if (CrudUtil.executeUpdate("UPDATE train SET trainName=? ,startTime=? ,EndTime=? ,trainFrom=? ,trainTo=? WHERE trainId=? ",
                    c.getTrainName(),c.getStartTime(),c.getEndTime(),c.getTrainFrom(),c.getTrainTo(),c.getTrainId())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
                loadAllTrain();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        btnUpdateTrain.setDisable(true);
        btnRemoveTrain1.setDisable(true);
    }

    public void btnRemoveTrainOnAction(ActionEvent actionEvent) {
        try {
            if (CrudUtil.executeUpdate("DELETE FROM train WHERE trainId=?",txtTrainId.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
                loadAllTrain();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        btnUpdateTrain.setDisable(true);
        btnRemoveTrain1.setDisable(true);
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.executeQuery("SELECT * FROM train WHERE trainId=?",txtTrainId.getText());
            if (result.next()) {
                txtTrainName.setText(result.getString(2));
                txtStartTime.setText(result.getString(3));
                txtEndTime.setText(result.getString(4));
                cmbTrainFrom.setValue(result.getString(5));
                cmbTrainTo.setValue(result.getString(6));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        trainAnchorPane.getChildren().clear();
    }

    public void textFields_Key_Releaseed(KeyEvent keyEvent) {
        Validation();
    }

    private Object Validation() {
        for (TextField key : map.keySet()) {
            Pattern pattern = map.get(key);
            if (!pattern.matcher(key.getText()).matches()) {
                addError(key);
                return key;
            } else
                removeError(key);
        }
        return true;
    }

    private void removeError(TextField text) {
        text.getParent().setStyle("-fx-border-color: green");
        btnUpdateTrain.setDisable(false);
        btnRemoveTrain1.setDisable(false);
    }

    private void addError(TextField text) {
        if (text.getText().length() > 0) {
            text.getParent().setStyle("-fx-border-color: red");
        }
        btnUpdateTrain.setDisable(true);
    }

    public void trainFrom() {
        try {
            ResultSet result = CrudUtil.executeQuery("SELECT * FROM station ORDER BY name ASC");
            ObservableList obList = FXCollections.observableArrayList();
            while (result.next()) {
                obList.add(result.getString(2));
            }
            cmbTrainFrom.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void trainTo() {
        try {
            ResultSet result = CrudUtil.executeQuery("SELECT * FROM station ORDER BY name ASC");
            ObservableList obList = FXCollections.observableArrayList();
            while (result.next()) {
                obList.add(result.getString(2));
            }
            cmbTrainTo.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        txtTrainId.clear();
        txtTrainName.clear();
        txtEndTime.clear();
        txtStartTime.clear();
        cmbTrainFrom.getSelectionModel().clearSelection();
        cmbTrainTo.getSelectionModel().clearSelection();
    }

    public void btnclearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void btnEmployeeReportOnAction(ActionEvent actionEvent) {
        String trainID = txtTrainId.getText();
        String trainName = txtTrainName.getText();
        String startTime = txtStartTime.getText();
        String endTime=txtEndTime.getText();
        String from= (String) cmbTrainFrom.getValue();
        String to= (String) cmbTrainTo.getValue();

        HashMap map=new HashMap();

        map.put("trainId",trainID);
        map.put("trainName",trainName);
        map.put("startTime",startTime);
        map.put("EndTime",endTime);
        map.put("trainFrom",from);
        map.put("trainTo",to);

        try {
            JasperDesign load= JRXmlLoader.load(this.getClass().getResourceAsStream("/views/reports/TrainReport.jrxml")) ;
            JasperReport compileReport = JasperCompileManager.compileReport(load);
            // JasperReport compileReport= (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/TrainReport.jasper"));
            JasperPrint jasperPrint= JasperFillManager.fillReport(compileReport,map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint,false);

        }catch (JRException e){
            e.printStackTrace();
        }
    }
}

package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Train;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AddTrainFromController {
    public Button btnBack;

    public ComboBox cmbTrainTo;
    public ComboBox cmbTrainFrom;
    public TextField txtTrainId;
    public TextField txtTrainName;
    public Button btnAddTrain;
    public TextField txtStartTime;
    public TextField txtEndTime;
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

        btnAddTrain.setDisable(true);

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

    public void textFields_Key_Released(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        Validation();

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object responds = Validation();

            if (responds instanceof TextField) {
                TextField textField = (TextField) responds;
                textField.requestFocus();
            } else {
                btnAddTrainOnAction();
            }
        }
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
        btnAddTrain.setDisable(false);
    }

    private void addError(TextField text) {
        if (text.getText().length() > 0) {
            text.getParent().setStyle("-fx-border-color: red");
        }
        btnAddTrain.setDisable(true);
    }

    public void btnAddTrainOnAction() throws SQLException, ClassNotFoundException {
        Train c = new Train(
                txtTrainId.getText(), txtTrainName.getText(), txtStartTime.getText(), txtEndTime.getText(), cmbTrainFrom.getValue(), cmbTrainTo.getValue());
        try {
            if (CrudUtil.executeUpdate("INSERT INTO train VALUES (?,?,?,?,?,?)",c.getTrainId(),c.getTrainName(),c.getStartTime(),
                    c.getEndTime(),c.getTrainFrom(),c.getTrainTo())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
                loadAllTrain();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        btnAddTrain.setDisable(true);
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

    //#############serch vada na
    public void txtSearchOnAction(ActionEvent actionEvent) {
//        try {
//            String sql = "SELECT * FROM train WHERE trainId=?";
//            PreparedStatement sta = DBConnection.getInstance().getConnection().prepareStatement(sql);
//            if (sta.executeQuery()!=null) {
//                new Alert(Alert.AlertType.WARNING, "Id Already Used").show();
//                btnAddTrain.setDisable(true);
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
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

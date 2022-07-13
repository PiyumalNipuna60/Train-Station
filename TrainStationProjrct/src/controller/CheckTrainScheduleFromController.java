package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.TrainSchedulCheck;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;

import java.sql.*;

public class CheckTrainScheduleFromController {
    public Button btnSearch;
    public TableColumn tblTrainTo;
    public TableColumn tblTrainFrom;
    public TableColumn tblTrainName;
    public Button btnBack;
    public TableColumn tblTrainId;
    public AnchorPane ScheduleAnchorPane;
    public ComboBox cmbTrainTo;
    public ComboBox cmbTrainFrom;
    public TableView tblTrainLoad;
    public TableColumn tblTrainStartTime;
    public TableColumn tblTrainStopTime;
    public TableColumn tblTrainStartStation;
    public TableColumn tblTrainEndStation;
    public TableColumn tblEndTime;

    public void initialize() {
        uploadComboBox();

        tblTrainFrom.setCellValueFactory(new PropertyValueFactory("From"));
        tblTrainTo.setCellValueFactory(new PropertyValueFactory("To"));
        tblTrainId.setCellValueFactory(new PropertyValueFactory("TrainId"));
        tblTrainName.setCellValueFactory(new PropertyValueFactory("TrainName"));
        tblTrainStartTime.setCellValueFactory(new PropertyValueFactory("StartTrainTime"));
        tblEndTime.setCellValueFactory(new PropertyValueFactory("EndStationTime"));
        tblTrainStopTime.setCellValueFactory(new PropertyValueFactory("TrainStopTime"));
        tblTrainStartStation.setCellValueFactory(new PropertyValueFactory("TrainStartStation"));
        tblTrainEndStation.setCellValueFactory(new PropertyValueFactory("TrainEndStation"));

        try {
            loadAllTrain();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TrainStationProject", "root", "1234");
            String sql = "SELECT * FROM  stationSchedule where cusFrom='" + cmbTrainFrom.getValue() + "' && cusTo='" + cmbTrainTo.getValue() + "'";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            ObservableList<TrainSchedulCheck> obList1 = FXCollections.observableArrayList();
            while (result.next()) {
                obList1.add(
                        new TrainSchedulCheck(
                                result.getString("cusFrom"),
                                result.getString("cusTo"),
                                result.getString("TrainId"),
                                result.getString("TrainName"),
                                result.getString("StartStationTime"),
                                result.getString("EndStationTime"),
                                result.getString("TrainStopTime"),
                                result.getString("TrainStartStation"),
                                result.getString("TrainStopStation")
                        ));
            }
            tblTrainLoad.setItems(obList1);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        ScheduleAnchorPane.getChildren().clear();
    }

    private void loadAllTrain() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.executeQuery("SELECT * FROM stationSchedule");
        ObservableList<TrainSchedulCheck> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new TrainSchedulCheck(
                            result.getString("cusFrom"),
                            result.getString("cusTo"),
                            result.getString("TrainId"),
                            result.getString("TrainName"),
                            result.getString("StartStationTime"),
                            result.getString("EndStationTime"),
                            result.getString("TrainStopTime"),
                            result.getString("TrainStartStation"),
                            result.getString("TrainStopStation")
                    ));
        }
        tblTrainLoad.setItems(obList);
    }

    public void uploadComboBox() {
        comboFrom();
        comboTo();
    }

    public void comboFrom() {
        try {
            ResultSet result = CrudUtil.executeQuery("SELECT * FROM station");
            ObservableList obList1 = FXCollections.observableArrayList();
            while (result.next()) {
                obList1.add(new String(result.getString(2)));
            }
            cmbTrainFrom.setItems(obList1);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void comboTo() {
        try {
            ResultSet result = CrudUtil.executeQuery("SELECT * FROM station");
            ObservableList obList = FXCollections.observableArrayList();
            while (result.next()) {
                obList.add(new String(result.getString(2)));
            }
            cmbTrainTo.setItems(obList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnPrintOnAction(ActionEvent actionEvent)throws JRException  {
        try {

            JasperDesign load=JRXmlLoader.load(this.getClass().getResourceAsStream("/views/reports/TrainScheduleReport.jrxml")) ;
            JasperReport compileReport = JasperCompileManager.compileReport(load);

            //JasperReport compileReport=(JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/TrainScheduleReport.jasper"));
            Connection connection= DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint=JasperFillManager.fillReport(compileReport,null,connection);
            JasperViewer.viewReport(jasperPrint,false);
        }catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Employee;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;

import java.io.IOException;
import java.sql.*;

public class EmployeeSalaryFromController {
    public Button btnBack;
    public TableColumn tblEmpId;
    public TableColumn tblEmpName;
    public TableColumn tblEmpAddress;
    public TableColumn tblEmpAge;
    public TableColumn tblEmpTel;
    public TableColumn tblEmpSalary;
    public Button btnUpdate;
    public TableView tblEmployee;
    public AnchorPane salaryAnchorPane;

    public void initialize() {
        tblEmpId.setCellValueFactory(new PropertyValueFactory("id"));
        tblEmpName.setCellValueFactory(new PropertyValueFactory("name"));
        tblEmpAddress.setCellValueFactory(new PropertyValueFactory("address"));
        tblEmpAge.setCellValueFactory(new PropertyValueFactory("age"));
        tblEmpTel.setCellValueFactory(new PropertyValueFactory("contact"));
        tblEmpSalary.setCellValueFactory(new PropertyValueFactory("salary"));

        try {
            loadAllEmployee();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllEmployee() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.executeQuery("SELECT * FROM employee");
        ObservableList<Employee> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new Employee(
                            result.getString("id"),
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("age"),
                            result.getString("contact"),
                            result.getString("salary")
                    ));
        }
        tblEmployee.setItems(obList);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../views/ModifyEmployeeFrom.fxml"));
        salaryAnchorPane.getChildren().setAll(pane);
    }


    public void btnBackOnAction(ActionEvent actionEvent) {
        salaryAnchorPane.getChildren().clear();
    }

    public void btnPrintOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign load = JRXmlLoader.load(this.getClass().getResourceAsStream("/views/reports/EmployeeSalaryReport.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(load);

            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, connection);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Employee;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ModifyEmployeeFromController {
    public Button btnDeleteEmployee1;
    public Button btnBack;
    public TextField txtEmpSalary;
    public TextField txtEmpAge;
    public TextField txtEmpTel;
    public TextField txtEmpAddress;
    public TextField txtEmpId;
    public TextField txtEmpName;
    public Button btnUpdateEmployee;
    public TableView tblEmployee;
    public TableColumn colEmpId;
    public TableColumn colEmpName;
    public TableColumn colEmpAddress;
    public TableColumn colEmpAge;
    public TableColumn colEmpTel;
    public TableColumn colEmpSalary;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        btnUpdateEmployee.setDisable(true);
        btnDeleteEmployee1.setDisable(true);

        Pattern pattenId = Pattern.compile("^(E00-)[0-9]{3,5}$");
        Pattern pattenName = Pattern.compile("^[A-z ]{3,}$");
        Pattern pattenAddress = Pattern.compile("^[A-z0-9 ,/]{5,}$");
        Pattern pattenAge = Pattern.compile("^[0-9]{2}$");
        Pattern pattenTel = Pattern.compile("^(071|072|077|076|078|075)[0-9]{7}$");
        Pattern pattenSalary = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");

        map.put(txtEmpId, pattenId);
        map.put(txtEmpName, pattenName);
        map.put(txtEmpAddress, pattenAddress);
        map.put(txtEmpAge, pattenAge);
        map.put(txtEmpTel, pattenTel);
        map.put(txtEmpSalary, pattenSalary);

        colEmpId.setCellValueFactory(new PropertyValueFactory("id"));
        colEmpName.setCellValueFactory(new PropertyValueFactory("name"));
        colEmpAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colEmpAge.setCellValueFactory(new PropertyValueFactory("age"));
        colEmpTel.setCellValueFactory(new PropertyValueFactory("contact"));
        colEmpSalary.setCellValueFactory(new PropertyValueFactory("salary"));

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


    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.executeQuery("SELECT * FROM employee WHERE id=?",txtEmpId.getText());
            if (result.next()) {
                txtEmpName.setText(result.getString(2));
                txtEmpAddress.setText(result.getString(3));
                txtEmpAge.setText(result.getString(4));
                txtEmpTel.setText(result.getString(5));
                txtEmpSalary.setText(result.getString(6));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Employee c = new Employee(
                txtEmpId.getText(), txtEmpName.getText(), txtEmpAddress.getText(), txtEmpAge.getText(),
                txtEmpTel.getText(), txtEmpSalary.getText()
        );
        try {
            if (CrudUtil.executeUpdate("UPDATE employee SET name=? ,address=? ,age=? ,contact=? ,salary=?  WHERE id= ?",
                    c.getName(),c.getAddress(),c.getAge(),c.getContact(),c.getSalary(),c.getId())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadAllEmployee();
    }

    public void btnDeleteEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            if (CrudUtil.executeUpdate("DELETE FROM employee WHERE id=?",txtEmpId.getText())){
                new Alert(Alert.AlertType.CONFIRMATION,"Deleted!").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        loadAllEmployee();
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

    public void addError(TextField txtCus) {
        if (txtCus.getText().length() > 0) {
            txtCus.getParent().setStyle("-fx-border-color: red");
        }
        btnUpdateEmployee.setDisable(true);
    }

    public void removeError(TextField txtCus) {
        txtCus.getParent().setStyle("-fx-border-color: green");
        btnUpdateEmployee.setDisable(false);
        btnDeleteEmployee1.setDisable(false);
    }

    public void clear() {
        txtEmpId.clear();
        txtEmpName.clear();
        txtEmpAddress.clear();
        txtEmpAge.clear();
        txtEmpTel.clear();
        txtEmpSalary.clear();
    }

    public void btnclearOnAction(ActionEvent actionEvent) {
        clear();
    }

    public void btnEmployeeReportOnAction(ActionEvent actionEvent) {
        String EmployeeID = txtEmpId.getText();
        String EmployeeName = txtEmpName.getText();
        String EmployeeAddress = txtEmpAddress.getText();
        String EmployeeAge=txtEmpAge.getText();
        String EmployeeContact=txtEmpTel.getText();
        String EmployeeSalary=txtEmpSalary.getText();

        HashMap map=new HashMap();

        map.put("id",EmployeeID);
        map.put("name",EmployeeName);
        map.put("address",EmployeeAddress);
        map.put("age",EmployeeAge);
        map.put("contact",EmployeeContact);
        map.put("salary",EmployeeSalary);

        try {
            JasperDesign load= JRXmlLoader.load(this.getClass().getResourceAsStream("/views/reports/EmployeeReport.jrxml")) ;
            JasperReport compileReport = JasperCompileManager.compileReport(load);
            // JasperReport compileReport= (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/EmployeeReport.jasper"));
            JasperPrint jasperPrint= JasperFillManager.fillReport(compileReport,map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint,false);

        }catch (JRException e){
            e.printStackTrace();
        }
    }
}

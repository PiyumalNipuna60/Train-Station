package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.BookingCustomer;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IncomeReportFromController {
    public Button btnBack;
    public TableColumn tblIncomeDate;
    public TableColumn tblIncome;
    public TableColumn tblOption;
    public TextField txtMonthlyIncome;
    public AnchorPane incomeAnchorPane;
    public TableView<BookingCustomer> tblReport;

    public void initialize() throws SQLException, ClassNotFoundException {
        tblIncomeDate.setCellValueFactory(new PropertyValueFactory("date"));
        tblIncome.setCellValueFactory(new PropertyValueFactory("price"));
        tblOption.setCellValueFactory((param) -> {
            ImageView edit = new ImageView("/assets/draw.png");
            ImageView delete = new ImageView("/assets/trash.png");
            return new ReadOnlyObjectWrapper<>(new HBox(100, edit, delete));
});
        loadAllIncome();
    }

    private void loadAllIncome() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.executeQuery("SELECT * FROM booking");
        ObservableList<BookingCustomer> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new BookingCustomer(
                            result.getString("id"),
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("contact"),
                            result.getString("trainFrom"),
                            result.getString("trainTo"),
                            result.getString("time"),
                            result.getString("train"),
                            result.getString("seatNo"),
                            result.getString("class"),
                            result.getString("price"),
                            result.getDate("Date")
                    ));
        }
        tblReport.setItems(obList);
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        incomeAnchorPane.getChildren().clear();
    }
}

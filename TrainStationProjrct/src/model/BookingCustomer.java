package model;

import java.util.Date;

public class BookingCustomer {
    private String id;
    private String name;
    private String address;
    private String contact;
    private String trainFrom;
    private String trainTo;
    private String time;
    private String train;
    private String seatNo;
    private String trainClass;
    private String price;
    private Date date;

    public BookingCustomer(){}

    public BookingCustomer(String text, String txtCusNameText, String txtCusAddressText, String txtCusContactText, Object value, Object cmbCusToValue, String txtTrainTimeText, Object cmbCusTrainValue, Object cmbCusSeatNoValue, Object cmbCusClassValue, String txtCusPriceText, String txtCusBookDateText) {
        this.id = text;
        this.name = txtCusNameText;
        this.address = txtCusAddressText;
        this.contact = txtCusContactText;
        this.trainFrom = (String) value;
        this.trainTo = (String)cmbCusToValue;
        this.time = txtTrainTimeText;
        this.train =(String) cmbCusTrainValue;
        this.seatNo = (String)cmbCusSeatNoValue;
        this.trainClass =(String) cmbCusClassValue;
        this.price = txtCusPriceText;
        //this.date=DatxtCusBookDateText;
    }

    public BookingCustomer(String text, String txtCusNameText, String txtCusAddressText, String txtCusContactText, Object value, Object cmbCusToValue, String txtTrainTimeText, Object cmbCusTrainValue, Object cmbCusSeatNoValue, Object cmbCusClassValue, String txtCusPriceText, Date date) {
        this.id = text;
        this.name = txtCusNameText;
        this.address = txtCusAddressText;
        this.contact = txtCusContactText;
        this.trainFrom = (String) value;
        this.trainTo = (String)cmbCusToValue;
        this.time = txtTrainTimeText;
        this.train =(String) cmbCusTrainValue;
        this.seatNo = (String)cmbCusSeatNoValue;
        this.trainClass =(String) cmbCusClassValue;
        this.price = txtCusPriceText;
        this.date=date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTrainFrom() {
        return trainFrom;
    }

    public void setTrainFrom(String trainFrom) {
        this.trainFrom = trainFrom;
    }

    public String getTrainTo() {
        return trainTo;
    }

    public void setTrainTo(String trainTo) {
        this.trainTo = trainTo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(String trainClass) {
        this.trainClass = trainClass;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BookingCustomer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact=" + contact +
                ", from='" + trainFrom + '\'' +
                ", to='" + trainTo + '\'' +
                ", time='" + time + '\'' +
                ", train='" + train + '\'' +
                ", seatNo=" + seatNo +
                ", trainClass=" + trainClass +
                ", price='" + price + '\'' +
                '}';
    }
}

package model;

public class SignIn {
    private String fName;
    private String lName;
    private String address;
    private String contact;
    private String age;
    private String gender;
    private String userName;
    private String password;

    public SignIn() {
    }

    public SignIn(String fName, String lName, String address, String contact, String age, String gender, String userName, String password) {
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.contact = contact;
        this.age = age;
        this.gender = gender;
        this.userName = userName;
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SignIn{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", address='" + address + '\'' +
                ", contact=" + contact +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

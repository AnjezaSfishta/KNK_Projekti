package models;

import javafx.beans.property.SimpleStringProperty;

public class Member {

    private final SimpleStringProperty memberID;
    private final SimpleStringProperty name;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;

    public Member(String id, String name, String phone, String email) {
        this.memberID = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }

    public String getMemberID() {
        return memberID.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail() {
        return email.get();
    }

}


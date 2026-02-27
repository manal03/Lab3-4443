package com.example.lab3_4443;
//Model with data used for seperation of concerns
public class Contact {
    public String name;
    public String phone;
    public String dateAdded;
    public String description;

    public Contact(String name, String phone, String dateAdded, String description) {
        this.name = name;
        this.phone = phone;
        this.dateAdded = dateAdded;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getDescription() {
        return description;
    }
}

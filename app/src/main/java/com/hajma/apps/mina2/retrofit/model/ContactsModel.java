package com.hajma.apps.mina2.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ContactsModel {
    @SerializedName("contacts")
    @Expose
    public ArrayList<Contact> contacts;
    @SerializedName("device_id")
    @Expose
    public String deviceID;

    @SerializedName("device_type")
    @Expose
    public int device_type;

    public static class Contact {

        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("phone_numbers")
        @Expose
        private List<String> phoneNumbers = null;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("identifier")
        @Expose
        private String identifier;
        @SerializedName("first_name")
        @Expose
        private String firstName;

        public Contact(String companyName, List<String> phoneNumbers, String lastName, String identifier, String firstName) {
            this.companyName = companyName;
            this.phoneNumbers = phoneNumbers;
            this.lastName = lastName;
            this.identifier = identifier;
            this.firstName = firstName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public List<String> getPhoneNumbers() {
            return phoneNumbers;
        }

        public void setPhoneNumbers(List<String> phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }


        @Override
        public String toString() {
            return "Contact{" +
                    "companyName='" + companyName + '\'' +
                    ", phoneNumbers=" + phoneNumbers +
                    ", lastName='" + lastName + '\'' +
                    ", identifier='" + identifier + '\'' +
                    ", firstName='" + firstName + '\'' +
                    '}';
        }
    }
}



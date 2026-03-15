package com.matiasae.springdemo.mvc;

import com.matiasae.springdemo.mvc.validation.CourseCode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class Customer {
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String firstName;
    
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String lastName = "";

    @NotNull(message = "is required")
    @Min(value = 0, message = "must be greater than or equal to zero")
    @Max(value = 10, message = "must be less than or equal to 10")
    private Integer freePasses;

    @CourseCode(value = "LUV", message = "must start with LUV")
    private String courseCode;

    @Pattern(regexp = "^[a-zA-Z0-9]{5}$", message = "only 5 chars/digits")
    private String postalCode;

    public Customer() { }

    public Customer(String firstName, String lastName, Integer freePasses, String courseCode, String postalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.freePasses = freePasses;
        this.courseCode = courseCode;
        this.postalCode = postalCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getFreePasses() {
        return freePasses;
    }

    public void setFreePasses(Integer freePasses) {
        this.freePasses = freePasses;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", freePasses=" + freePasses +
                ", courseCode='" + courseCode + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}

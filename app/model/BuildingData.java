package model;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "building")
public class BuildingData {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(name = "street_name")
    private String streetName;
    @Column(name = "postal_code")
    private int postalCode;
    private String city;
    private String country;
    private String description;

    public BuildingData() {
    }

    public BuildingData(String name, String streetName, int postalCode, String city, String country, String description) {
        this.name = name;
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

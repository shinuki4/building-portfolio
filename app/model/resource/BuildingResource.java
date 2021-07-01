package model.resource;

import model.BuildingData;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class BuildingResource {
    private String id;
    private String link;
    private String name;
    private String streetName;
    private int number;
    private int postalCode;
    private String city;
    private String country;
    private String description;
    private String coordinates;

    public BuildingResource() {
    }

    public BuildingResource(String id, String link, String name, String streetName, int number, int postalCode, String city, String country, String description, String coordinates) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.streetName = streetName;
        this.number = number;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.description = description;
        this.coordinates = coordinates;
    }

    public BuildingResource(BuildingResource resource, String coordinates) {
        this.name = resource.getName();
        this.streetName = resource.getStreetName();
        this.number = resource.getNumber();
        this.postalCode = resource.getPostalCode();
        this.city = resource.getCity();
        this.country = resource.getCountry();
        this.description = resource.getDescription();
        this.coordinates = coordinates;
    }

    public BuildingResource(BuildingData data, String link) {
        this.id = data.getId().toString();
        this.link = link;
        this.name = data.getName();
        this.streetName = data.getStreetName();
        this.number = data.getNumber();
        this.postalCode = data.getPostalCode();
        this.city = data.getCity();
        this.country = data.getCountry();
        this.description = data.getDescription();
        this.coordinates = data.getCoordinates();
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getNumber() {
        return number;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getDescription() {
        return description;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates( String coordinates) {
        this.coordinates = coordinates;
    }
}

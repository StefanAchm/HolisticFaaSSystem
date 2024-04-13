package com.asi.hms.enums;

public class RegionGlobal {

    private String continent;
    private String country;
    private String city;

    public RegionGlobal(String continent, String country, String city) {
        this.continent = continent;
        this.country = country;
        this.city = city;
    }

    public RegionGlobal() {
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

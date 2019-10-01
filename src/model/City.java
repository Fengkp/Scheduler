package model;

public class City {
    private String city;
    private int cityId;
    private int countryId;
    private String postalCode;

    public City(String city) {
        this.city = city;
        setCity(city);
    }

    private void setCity(String city) {
        switch (city) {
            case "New York":
                postalCode = "10001";
                cityId = 1;
                countryId = 1;
                break;
            case "Phoenix":
                postalCode = "85001";
                cityId = 2;
                countryId = 1;
                break;
            case "London":
                postalCode = "25126";
                cityId = 3;
                countryId = 2;
                break;
        }
    }

    public String getCity() {
        return city;
    }

    public int getCityId() {
        return cityId;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String toString() {
        return this.city;
    }
}

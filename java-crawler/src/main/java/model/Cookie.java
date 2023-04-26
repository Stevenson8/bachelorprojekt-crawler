package model;

public class Cookie {
    private String name;
    private String value;
    private String expiryDate;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}

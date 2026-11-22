package Toolshop.Checkout.Data;

public class BillingData {

    public String Country;
    public String postal_code;
    public String house_number;
    public String street;
    public String city;
    public String state;

    public BillingData(String Country, String postal_code, String house_number, String street, String city, String state){
        this.city = city;
        this.Country = Country;
        this.house_number = house_number;
        this.postal_code = postal_code;
        this.street = street;
        this.state = state;
    }

}

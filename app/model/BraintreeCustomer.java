package model;

import com.braintreegateway.*;

/**
 * Created by clkaiser on 4/12/2014.
 */
public class BraintreeCustomer {

    private String customerID;
    private String firstname;
    private String email;
    private String lastname;
    private String address;
    private String suburb;
    private String postcode;
    private String state;
    private PaymentMethod paymentMethod;

    public void setFirstName (String firstname){
        this.firstname = firstname;
    }

    public String getFirstName (){
        return this.firstname;
    }

    public void setLastName (String lastName){
        this.lastname = lastName;
    }

    public String getLastname(){
        return this.lastname;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public String getEmail (){
        return this.email;
    }

    public void setAddress (String address){
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public void setSuburb (String suburb){
        this.suburb = suburb;
    }

    public String getSuburb(){
        return this.suburb;
    }

    public void setPostcode (String postcode){
        this.postcode = postcode;
    }

    public String getPostcode(){
        return this.postcode;
    }

    public void setState (String state){
        this.state = state;
    }

    public String getState(){
        return this.state;
    }

    public void setCustomerID (String customerID){
        this.customerID = customerID;
    }

    public String getCustomerID (){
        return this.customerID;
    }

    public PaymentMethod getPaymentMethod(){
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}

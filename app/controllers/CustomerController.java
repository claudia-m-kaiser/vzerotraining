package controllers;

import com.braintreegateway.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.BraintreeCustomer;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import views.html.customerupdate;

import java.util.Map;

/**
 * Created by clkaiser on 24/02/15.
 */
public class CustomerController extends Application {

    public static Result createCustomer() {

        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        String nonce = values.get("payment_method_nonce")[0];

        BraintreeCustomer braintreeCustomer = new BraintreeCustomer();

        braintreeCustomer.setFirstName(values.get("InputFirstName")[0]);
        braintreeCustomer.setLastName(values.get("InputLastName")[0]);
        braintreeCustomer.setEmail(values.get("InputEmail")[0]);
        braintreeCustomer.setAddress(values.get("InputAddress")[0]);
        braintreeCustomer.setSuburb(values.get("InputSuburb")[0]);
        braintreeCustomer.setPostcode(values.get("InputPostcode")[0]);
        braintreeCustomer.setState(values.get("InputState")[0]);


        currService.addCustomerToVault(nonce, braintreeCustomer);

        return ok(braintreeCustomer.getCustomerID());
    }

    public static Result updateCustomer(){

        //Using the customer ID from an existing customer

        String customerId = "64771009";
        String token = sandboxBtService.GetToken(customerId);

        Customer customer = currService.customerSearch(customerId);

        //PaymentMethod pm = customer.getDefaultPaymentMethod().;

        PaymentMethod paymentMethod = customer.getDefaultPaymentMethod();

        switch(paymentMethod.getClass().getSimpleName()){

            case "PayPalAccount":{
                PayPalAccount paypalAccount = ((PayPalAccount) paymentMethod);
                paypalAccount.getEmail();
                break;
            }
            case "CreditCard":{
                CreditCard creditCard = ((CreditCard) paymentMethod);
                creditCard.getMaskedNumber();
                break;
            }
            default:{
                Logger.debug("No action defined for type " + paymentMethod.getClass().getSimpleName());
                break;
            }
        }


        return ok(customerupdate.render(token,customer));
    }

    public static Result showCustomerList(){

        ObjectNode customerList = Json.newObject();

        int index = 0;
        ResourceCollection<Customer> collection = currService.getCustomerList();
        for (Customer customer: collection){
            index++;
            Logger.debug("Customer " + index + ": " + customer.getId());
        }

        return ok();
    }

}

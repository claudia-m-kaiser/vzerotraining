package controllers;
import play.mvc.Result;

import java.util.Map;

/**
 * Created by clkaiser on 25/02/15.
 */
public class PaymentMethodController extends Application {

    public static Result createPaymentMethod(){

        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String nonce = values.get("payment_method_nonce")[0].toString();
        String customerId = values.get("customer_id")[0].toString();

        // Storing the payment method for the specified customer Id

        currService.createPaymentMethodForExistingCustomer(customerId,nonce);

        return ok();
    }
}

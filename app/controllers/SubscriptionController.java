package controllers;
import com.braintreegateway.Subscription;
import play.mvc.Result;


/**
 * Created by clkaiser on 2/03/15.
 */
public class SubscriptionController extends Application {

    public static Result subscribe(){

        String planId = "p001";
        String token = "ght9ng";

        Subscription newSubscription = currService.createSubscription(token,planId);
        return ok("Subscription Id: " + newSubscription.getId());
    }



}

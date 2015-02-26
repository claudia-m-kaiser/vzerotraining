package controllers;

import play.Logger;
import play.mvc.Result;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Created by clkaiser on 25/02/15.
 */
public class WebhooksController extends Application {

    public static Result initialSetup(String bt_challenge){

        if(bt_challenge.isEmpty()){
            Logger.error("Could not get bt challenge");
        }else{
            Logger.info("BT Challenge is: " + bt_challenge);
        }

        String verification = currService.getWebhookVerificationResponse(bt_challenge);

        return ok(verification);
    }
}

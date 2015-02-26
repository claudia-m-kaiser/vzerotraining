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

    public static Result listener(){

        final Map<String, String[]> params = request().body().asFormUrlEncoded();

        Logger.debug("Request :" + request().toString());

        String bt_signature = params.get("bt_signature")[0].toString();
        String bt_payload = params.get("bt_payload")[0].toString();

        Logger.debug("bt_signature: " + bt_signature + "bt_payload: " + bt_payload);

        return ok();
    }
}

package controllers;

import play.Logger;
import play.mvc.Result;

import java.util.Map;

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

        String bt_signature = request().body().asJson().findValue("bt_signature").asText();
        String bt_payload = request().body().asJson().findValue("bt_payload").asText();

        Logger.debug("bt_signature: " + bt_signature + "bt_payload: " + bt_payload);

        return ok();
    }
}

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

    public static Result initialSetup(){

        String bTChallenge="";

        final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
        for (Map.Entry<String,String[]> entry : entries) {
            final String key = entry.getKey();
            final String value = Arrays.toString(entry.getValue());

            if (key.equals("bt_challenge")){bTChallenge = key;}
        }

        return ok(currService.getWebhookVerificationResponse(bTChallenge));
    }
}

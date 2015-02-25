package controllers;

import play.mvc.Result;

/**
 * Created by clkaiser on 25/02/15.
 */
public class WebhooksController extends Application {

    public static Result initialSetup(String bTChallenge){

        return ok(currService.getWebhookVerificationResponse(bTChallenge));
    }
}

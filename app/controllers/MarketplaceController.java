package controllers;

import play.mvc.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by clkaiser on 26/02/15.
 */
public class MarketplaceController extends Application {

    public static Result createSubmerchantAccount(){

        currService.onboardSubmerchantAccount();
        return ok();
    }
}

package controllers;

import play.mvc.Result;
import views.html.onboarding;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by clkaiser on 26/02/15.
 */
public class MarketplaceController extends Application {

    public static Result createSubmerchantAccount(){

        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Map submerchant = new HashMap();

        submerchant.put("FirstName", values.get("FirstName")[0].toString());
        submerchant.put("LastName", values.get("LastName")[0].toString());
        submerchant.put("Email", values.get("Email")[0].toString());
        submerchant.put("Phone", values.get("Phone")[0].toString());
        submerchant.put("DOB", values.get("DOB")[0].toString());
        submerchant.put("StreetAddress", values.get("StreetAddress")[0].toString());
        submerchant.put("Locality", values.get("Locality")[0].toString());
        submerchant.put("Region", values.get("Region")[0].toString());
        submerchant.put("PostCode", values.get("PostCode")[0].toString());
        submerchant.put("Phone", values.get("Phone")[0].toString());
        submerchant.put("FundingDescriptor", "Email");
        submerchant.put("FundingEmail", values.get("Email")[0].toString());
        submerchant.put("MerchantId", values.get("MerchantId")[0].toString());

        currService.onboardSubmerchantAccount(submerchant);

        return ok();
    }
}

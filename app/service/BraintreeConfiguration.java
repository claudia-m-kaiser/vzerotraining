package service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.ClientTokenRequest;
import com.braintreegateway.Environment;
import play.Logger;

/**
 * Created by clkaiser on 4/12/2014.
 */
public class BraintreeConfiguration {

    public BraintreeGateway getGateway() {
        return gateway;
    }

    public static enum BraintreeEnvironment {
        Sandbox,Production,Development
    }

    private BraintreeEnvironment currEnvironment;

    // Update your Sandbox and Production merchant credentials
    private String sandboxMerchant_ID = "fdxt2jvb3fxt537f";
    private String sandboxPublic_Key = "fwfd7s4vnjd4v76x";
    private String sandboxPrivate_Key = "69da086845118cb5d5f1ca3601ec8ce5";

    private String productionMerchant_ID = "";
    private String productionPublic_Key = "";
    private String productionPrivate_Key = "";

    private BraintreeGateway gateway;

    public BraintreeConfiguration(BraintreeEnvironment environment) {
        this.setCurrEnvironment(environment);
    }

    public void setCurrEnvironment (BraintreeEnvironment environment){

        this.currEnvironment = environment;
        switch(currEnvironment){

            case Sandbox:
                this.gateway = new BraintreeGateway(Environment.SANDBOX,this.sandboxMerchant_ID,this.sandboxPublic_Key,this.sandboxPrivate_Key);
                break;

            case Production:
                this.gateway = new BraintreeGateway(Environment.PRODUCTION,this.productionMerchant_ID,this.productionPublic_Key,this.productionPrivate_Key);
                break;

            case Development:
                ///Do Something

            default:
                Logger.info("Could not initiate Braintree");
        }
    }
}


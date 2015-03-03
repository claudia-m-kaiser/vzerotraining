package controllers;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Transaction;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import service.BraintreeService;
import views.html.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by clkaiser on 25/02/15.
 */
public class TransactionController extends Application{

    public static enum TransactionType {
        Sale,Authorisation,Capture,Void,Refund;
    }


////////////////////////////// Web Transaction Processing //////////////////////////////////

    public static Result create(){

        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        String nonce = values.get("payment_method_nonce")[0].toString();
        boolean storeInVault = Boolean.parseBoolean(values.get("store_in_vault")[0].toString());

        // Capturing the funds using the nonce received from the client

        Transaction transaction = currService.createTransactionWithNonce(nonce, storeInVault);

        return ok("Transaction ID: " + transaction.getId());
    }

    public static Result newAuthorisation(){

        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String nonce = values.get("payment_method_nonce")[0].toString();

        // Capturing the funds using the nonce received from the client
        Transaction transaction = currService.createTransactionWithNonce(nonce, true);

        return ok(transaction.getId());
    }

////////////////////////////// Mobile Transaction Processing //////////////////////////////////


    public static Result sandboxProcessMobilePayment(){
        return processMobilePayment(sandboxBtService);
    }

    public static Result productionProcessMobilePayment() {
        return processMobilePayment(productionBtService);
    }

    private static Result processMobilePayment(BraintreeService service){

        String nonce = request().body().asJson().findValue("payment_method_nonce").asText();

        //Charging a new customer and storing their payment method in the vault

        Transaction transaction = service.createTransactionWithNonce(nonce, true);

        ObjectNode result = Json.newObject();

        if(transaction == null) {
            result.put("status", "KO");
            result.put("message", "Could not process payment");
            return badRequest(result);
        } else {
            result.put("status", "OK");
            result.put("transaction_id", transaction.getId());
            return ok(result);
        }
    }



}

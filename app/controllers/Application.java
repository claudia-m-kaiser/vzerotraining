package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import model.BraintreeCustomer;
import service.BraintreeConfiguration;
import service.BraintreeConfiguration.BraintreeEnvironment;
import model.BraintreePayment;
import service.BraintreeService;
import play.Routes;
import play.libs.Json;
import play.mvc.*;

import views.html.index;
import views.html.single;
import views.html.future;
import views.html.pponly;
import views.html.shortcut;
import views.html.thankyou;
import views.html.existingclient;

import java.util.Map;

public class Application extends Controller {

    private static BraintreeService sandboxBtService = new BraintreeService(new BraintreeConfiguration(BraintreeEnvironment.Sandbox));
    private static BraintreeService productionBtService = new BraintreeService(new BraintreeConfiguration(BraintreeEnvironment.Production));

    public static Result index() {
        String token = sandboxBtService.GetToken();
        session("token",token);
        return ok(index.render());
    }

    public static Result SinglePaymentPage() {
        String token = session("token");
        return ok(single.render(token));
    };

    public static Result FuturePaymentPage (){
        String token = session("token");
        return ok(future.render(token));

    }

    public static Result PayPalOnly(){
        String token = session("token");
        return ok(pponly.render(token));

    }

    public static Result Shortcut(){
        String token = session("token");
        return ok(shortcut.render(token));
    }

    public static Result SandboxClientToken(String customerId) {
        return clientToken(customerId, sandboxBtService);
    }

    public static Result ProductionClientToken(String customerId){
        return clientToken(customerId, productionBtService);
    }

    private static Result clientToken(String customerId, BraintreeService service) {
        String token;

        if (customerId.isEmpty()) {
            token = service.GetToken();
        } else {
            token = service.GetToken(customerId);
        }

        ObjectNode result = Json.newObject();

        if(token == null) {
            result.put("status", "KO");
            result.put("message", "Could not get token");
            return badRequest(result);
        } else {
            result.put("status", "OK");
            result.put("client_token", token);
            return ok(result);
        }
    }

    public static Result ExistingClient(){

        BraintreeCustomer braintreeCustomer = new BraintreeCustomer();

        //Using the customer ID from an existing customer
        String token = sandboxBtService.GetToken("71423461");

        return ok(existingclient.render(token));

    }

    public static Result NewSinglePayment(){

        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        String nonce = values.get("payment_method_nonce")[0].toString();

        // Capturing the funds using the nonce received from the client

        BraintreePayment braintreePayment = sandboxBtService.CreatePaymentWithNonce(nonce);

        return ok(thankyou.render(braintreePayment.getTransactionID()));
    }

    public static Result NewFuturePayment(){

        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        String nonce = values.get("payment_method_nonce")[0].toString();
        String firstName = "Dani";
        String lastName = "Rivera";
        String email = "dani.rivera@example.com";

        //Charging a new customer and storing their payment method in the vault

        BraintreePayment braintreePayment = sandboxBtService.CreatePaymentForNewCustomerAndAddToVault(nonce, firstName, lastName, email);

        return ok(thankyou.render(braintreePayment.getTransactionID()));
    }

    public static Result SandboxProcessMobilePayment(){

        String nonce = request().body().asJson().findValue("payment_method_nonce").asText();

        //Charging a new customer and storing their payment method in the vault

        BraintreePayment braintreePayment = sandboxBtService.CreatePaymentWithNonce(nonce);

        String transactionID;

        ObjectNode result = Json.newObject();

        if(braintreePayment == null) {
            result.put("status", "KO");
            result.put("message", "Could not process payment");
            return badRequest(result);
        } else {
            transactionID  = braintreePayment.getTransactionID();
            result.put("status", "OK");
            result.put("transaction_id", transactionID);
            return ok(result);
        }
    }

    public static Result ProductionProcessMobilePayment(){

        String nonce = request().body().asJson().findValue("payment_method_nonce").asText();

        //Charging a new customer and storing their payment method in the vault

        BraintreePayment braintreePayment = productionBtService.CreatePaymentWithNonce(nonce);

        String transactionID;

        ObjectNode result = Json.newObject();

        if(braintreePayment == null) {
            result.put("status", "KO");
            result.put("message", "Could not process payment");
            return badRequest(result);
        } else {
            transactionID  = braintreePayment.getTransactionID();
            result.put("status", "OK");
            result.put("transaction_id", transactionID);
            return ok(result);
        }
    }

    public static Result CreateCustomer() {

        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        String nonce = values.get("payment_method_nonce")[0];

        BraintreeCustomer braintreeCustomer = new BraintreeCustomer();

        braintreeCustomer.setFirstName(values.get("InputFirstName")[0]);
        braintreeCustomer.setLastName(values.get("InputLastName")[0]);
        braintreeCustomer.setEmail(values.get("InputEmail")[0]);
        braintreeCustomer.setAddress(values.get("InputAddress")[0]);
        braintreeCustomer.setSuburb(values.get("InputSuburb")[0]);
        braintreeCustomer.setPostcode(values.get("InputPostcode")[0]);
        braintreeCustomer.setState(values.get("InputState")[0]);


        sandboxBtService.addToVault(nonce, braintreeCustomer);

        return ok("FUTURE_ID");
    }


    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        // Routes
                        controllers.routes.javascript.Application.CreateCustomer()
                )
        );
    }


}

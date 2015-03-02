package controllers;

import com.braintreegateway.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.Result;
import service.BraintreeConfiguration;
import service.BraintreeConfiguration.BraintreeEnvironment;
import model.BraintreePayment;
import service.BraintreeService;
import play.Routes;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import java.util.Map;

public class Application extends Controller {

    protected static BraintreeService sandboxBtService = new BraintreeService(new BraintreeConfiguration(BraintreeEnvironment.Sandbox));
    protected static BraintreeService productionBtService = new BraintreeService(new BraintreeConfiguration(BraintreeEnvironment.Production));

    protected static BraintreeService currService = sandboxBtService;

    public static Result index() {
        String token = currService.GetToken();
        session("token",token);
        return ok(index.render());
    }

    public static Result hermesShortcut() {
        String token = session("token");
        return ok(hermesshortcut.render(token));
    };

    public static Result futurePaymentPage (){
        String token = session("token");
        return ok(future.render(token));

    }

    public static Result payPalOnly(){
        String token = session("token");
        return ok(pponly.render(token));

    }

    public static Result nonHermesShortcut(){
        String token = session("token");
        return ok(shortcut.render(token));
    }

    public static Result authorisationAndCapture(){
        String token = session("token");
        return ok(authcap.render(token));
    }

    public static Result sandboxClientToken(String customerId) {
        return clientToken(customerId, sandboxBtService);
    }

    public static Result productionClientToken(String customerId){
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

    public static Result existingClient(){

        String customerId = "64771009";

        //Using the customer ID from an existing customer
        String token = sandboxBtService.GetToken(customerId);

        return ok(existingclient.render(token));

    }

    public static Result renderCreatePaymentMethodPage(){

        String token = session("token");
        return ok(paymentmethodcreate.render(token));
    }

    public static Result captureAuthorisation(){

        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String authorisation_id = values.get("authorisation_id")[0].toString();

        // Capturing the authorisation
        Transaction transaction = sandboxBtService.submitPaymentForSettlement(authorisation_id);

        return ok(transaction.getId());
    }

    public static Result voidAuthorisation(){

        String authorisation_id = "938xty";

        // Voiding the authorisation
        if(sandboxBtService.voidAuthorisation(authorisation_id)){
            return ok();
        }else{
            return badRequest();
        }

    }

    public static Result cloneTransaction(){

        String transaction_id = "bydqb4";

        // Voiding the authorisation
        sandboxBtService.cloneTransaction(transaction_id);

        return ok();

    }

    public static Result transactionSearch(){

        return ok();
    }

    public static Result dropIn(){
        String token = session("token");
        return ok(dropin.render(token));
    }

    public static Result submerchantOnboardingPage(){
        return ok(onboarding.render());
    }

    public static Result customCreditCardForm(){
        String token = session("token");
        return ok(custom.render(token));
    }

    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        // Routes
                        controllers.routes.javascript.TransactionController.newAuthorisation(),
                        controllers.routes.javascript.Application.captureAuthorisation(),
                        controllers.routes.javascript.Application.voidAuthorisation(),
                        controllers.routes.javascript.CustomerController.createCustomer()


                )
        );
    }


}

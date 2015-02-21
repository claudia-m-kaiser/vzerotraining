package service;

import com.braintreegateway.*;
import model.BraintreeCustomer;
import model.BraintreePayment;
import play.Logger;

import java.math.BigDecimal;

/**
 * Created by clkaiser on 21/02/15.
 */
public class BraintreeService {

    private final BraintreeConfiguration configuration;

    public BraintreeService(BraintreeConfiguration configuration) {
        this.configuration = configuration;
    }

    public String GetToken(){
        String clientToken = configuration.getGateway().clientToken().generate();
        return clientToken;
    }

    public String GetToken(String customerID){
        ClientTokenRequest clientTokenRequest = new ClientTokenRequest()
                .customerId(customerID);
        String clientToken = configuration.getGateway().clientToken().generate(clientTokenRequest);
        return clientToken;
    }

    public Result<Address> addToVault(String nonce, BraintreeCustomer btCustomer) {

        CustomerRequest customerRequest = new CustomerRequest()
                .paymentMethodNonce(nonce)
                .firstName(btCustomer.getFirstName())
                .lastName(btCustomer.getLastname())
                .email(btCustomer.getEmail());

        Result<Customer> customerResult = configuration.getGateway().customer().create(customerRequest);

        Customer customer = customerResult.getTarget();

        btCustomer.setCustomerID(customer.getId());
        btCustomer.setPaymentMethod(customer.getDefaultPaymentMethod());

        AddressRequest addressRequest = new AddressRequest()
                .firstName(btCustomer.getFirstName())
                .lastName(btCustomer.getLastname())
                .streetAddress(btCustomer.getAddress())
                .locality(btCustomer.getSuburb())
                .region(btCustomer.getState())
                .postalCode(btCustomer.getPostcode());

        Result<Address> result = configuration.getGateway().address().create(btCustomer.getCustomerID(), addressRequest);

        return result;
    }


    public BraintreePayment CreatePaymentWithNonce(String nonce) {

        TransactionRequest transactionRequest = new TransactionRequest()
                .amount(new BigDecimal("1.00"))
                .paymentMethodNonce(nonce)
                .options()
                .storeInVault(true)
                .done();

        Result<Transaction> transactionResult = configuration.getGateway().transaction().sale(transactionRequest);

        if (transactionResult.isSuccess()) {

            BraintreePayment btPayment = new BraintreePayment(transactionResult.getTarget());
            Logger.info("Success!: ", btPayment.getTransactionID());
            return btPayment;

        } else {
            Logger.info(transactionResult.getMessage());
            return null;
        }
    }


    public BraintreePayment CreatePaymentForNewCustomerAndAddToVault(String nonce, String firstName, String lastName, String email) {

        TransactionRequest transactionRequest = new TransactionRequest()
                .paymentMethodNonce(nonce)
                .customer()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .done()
                .amount(new BigDecimal("20.00"))
                .options()
                .storeInVault(true)
                .done();

        Result<Transaction> transactionResult = configuration.getGateway().transaction().sale(transactionRequest);

        if (transactionResult.isSuccess()) {
            BraintreePayment btPayment = new BraintreePayment(transactionResult.getTarget());
            Logger.info("Success!: ", btPayment.getTransactionID());
            return btPayment;

        } else {
            Logger.info(transactionResult.getMessage());
            return null;
        }
    }


    public void CreatePaymentWithSavedPaymentMethod(String token){
        TransactionRequest transactionRequest = new TransactionRequest()
                .paymentMethodToken(token)
                .amount(new BigDecimal("20.00"));

        Result<Transaction> result = configuration.getGateway().transaction().sale(transactionRequest);
    }

}

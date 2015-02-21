package model;

import com.braintreegateway.Transaction;

/**
 * Created by clkaiser on 4/12/2014.
 */
public class BraintreePayment {

    private final Transaction transaction;

    public BraintreePayment(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getTransactionID() {
        return this.transaction.getId();
    }

}

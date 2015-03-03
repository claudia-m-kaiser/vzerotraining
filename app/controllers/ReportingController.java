package controllers;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Transaction;
import play.Logger;
import play.mvc.Result;
import views.html.settlement;
import views.html.transactionsearch;

import java.util.*;


/**
 * Created by clkaiser on 2/03/15.
 */
public class ReportingController extends Application {

    ////////////////////////////// Transaction Search //////////////////////////////////

    public static Result transactionSummary(){

        ResourceCollection<Transaction> collection = currService.getTransactionList();

        List<Map<String,String>> records = new ArrayList();

        for (Transaction transaction : collection) {
            Map<String,String> record = new HashMap<String,String>();
            record.put("id", transaction.getId());
            record.put("type", transaction.getType().toString());
            record.put("status", transaction.getStatus().name());
            record.put("amount",transaction.getAmount().toString());
            record.put("currency",transaction.getCurrencyIsoCode());
            record.put("created_date",transaction.getCreatedAt().toInstant().toString());
            records.add(record);
        }

        return ok(transactionsearch.render(records));
    }

    public static Result settlementBatch(){

        Calendar date = Calendar.getInstance();
        date.set(2015,Calendar.FEBRUARY,28);

        List<Map<String,String>> records = currService.generateSettlementBatch(date);

        return ok(settlement.render(records));
    }
/*
    public static Result declineAnalysis(){


    }*/

}

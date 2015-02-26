package controllers;

import play.Logger;
import play.mvc.Result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by clkaiser on 25/02/15.
 */
public class WebhooksController extends Application {

    public static Result initialSetup(String bt_challenge){

        if(bt_challenge.isEmpty()){
            Logger.error("Could not get bt challenge");
        }else{
            Logger.info("BT Challenge is: " + bt_challenge);
        }

        String verification = currService.getWebhookVerificationResponse(bt_challenge);

        return ok(verification);
    }

    public static Result listener(){

        final Map<String, String[]> webhook = request().body().asFormUrlEncoded();
        String bt_signature = webhook.get("bt_signature")[0].toString();
        String bt_payload = webhook.get("bt_payload")[0].toString();

        String parsedWebhook = currService.parseWebhookNotification(bt_signature,bt_payload);

        writeToWebhookLogFile(parsedWebhook, "webhooks.log");

        return ok();
    }

    public static void writeToWebhookLogFile(String notification, String filename){

        try{
            String fileSeparator = System.getProperty("file.separator");
            //Specify the file name and path here
            File file =new File("logs" + fileSeparator + filename);

    	/* This logic is to create the file if the
    	 * file is not already present
    	 */
            if(!file.exists()){
                file.createNewFile();
            }

            //Here true is to append the content to file
            FileWriter fw = new FileWriter(file,true);
            //BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(notification);
            bw.newLine();
            //Closing BufferedWriter Stream
            bw.close();

            Logger.info("Data successfully appended at the end of file");

        }catch(IOException ioe){
            Logger.error("Exception occurred:");
        }
    }
}

package me.federicomaggi.suggestme;

import android.util.Log;

/**
 * Created by federicomaggi on 07/05/15.
 */
public class HTTPHandler {

    private SuggestItem item_to_send;

    public HTTPHandler(){

    }

    void setSuggestItemToSend( SuggestItem item_to_send ){
        this.item_to_send = item_to_send;
    }

    Boolean sendSuggestRequest( ){

        String suggest_item_JSON = item_to_send.JSONEncode();

        if( suggest_item_JSON == null ) {
            Log.e("SUGGEST_ITEM_ENCODING","NULL RETURNED");
            return false;
        }

        //HTTPUrlConnection in an AsyncTask



        return false;
    }








}

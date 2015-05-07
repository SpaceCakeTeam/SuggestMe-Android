package me.federicomaggi.suggestme;

import org.json.JSONObject;

/**
 * Created by federicomaggi on 07/05/15.
 */
public class SuggestItem {

    public static final String SOCIAL_TAG = "social";
    public static final String GOODS_TAG = "goods";
    public static enum SuggestCategory {
        SOCIAL, GOODS
    }

    private String subcategory_title;
    private String content;
    private SuggestCategory social_or_goods;

    public SuggestItem( String title, String content, SuggestCategory social_or_goods ){

        this.subcategory_title = title;
        this.content = content;
        this.social_or_goods = social_or_goods;
    }

    void setSuggestItemTitle( String title ){
        this.subcategory_title = title;
    }
    void setSuggestItemContent( String content){
        this.content = content;
    }
    void setSuggestItemCategory( SuggestCategory social_or_goods ){
        this.social_or_goods = social_or_goods;
    }

    String getSuggestItemTitle(){
        return this.subcategory_title;
    }
    String getSuggestItemContent(){
        return this.content;
    }
    SuggestCategory getSuggestItemCategory(){
        return this.social_or_goods;
    }

    JSONObject JSONencode(){

        JSONObject encoded_item = new JSONObject();

        encoded_item.put()



    }
}

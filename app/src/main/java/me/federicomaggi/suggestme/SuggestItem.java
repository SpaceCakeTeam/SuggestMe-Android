package me.federicomaggi.suggestme;

import org.json.JSONObject;

import java.util.Objects;

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
    private Boolean replied;

    public SuggestItem( String title, String content, SuggestCategory social_or_goods ){

        this.subcategory_title = title;
        this.content = content;
        this.social_or_goods = social_or_goods;
        this.replied = false;
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

    String JSONEncode(){

        JSONObject encoded_item = new JSONObject();

        try{
            encoded_item.put("title",(Object) this.subcategory_title);
            encoded_item.put("content",(Object) this.content);

            if( this.social_or_goods == SuggestCategory.SOCIAL )
                encoded_item.put("category","social");
            else
                encoded_item.put("category","goods");

            return encoded_item.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

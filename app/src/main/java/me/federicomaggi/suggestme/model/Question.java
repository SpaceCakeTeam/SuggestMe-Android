package me.federicomaggi.suggestme.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.federicomaggi.suggestme.controller.DBHelper;
import me.federicomaggi.suggestme.controller.DatabaseContract;
import me.federicomaggi.suggestme.services.CommunicationHandler;

/**
 * Created by federicomaggi on 20/05/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class Question {

    private int id = -1;
    private int categoryid;
    private int subcategoryid;
    private String text;
    private int timestamp;
    private Boolean anonflag;
    private Context context;

    public Question( String text, int categoryid, int subcategoryid, Boolean anon ) {

        this.categoryid    = categoryid;
        this.subcategoryid = subcategoryid;
        this.text          = text;
        this.anonflag      = anon;
    }

    private Question( int id, String text, int categoryid, int subcategoryid, Boolean anon, int timestamp ) {

        this.id = id;
        this.text = text;
        this.categoryid = categoryid;
        this.subcategoryid = subcategoryid;
        this.anonflag = anon;
        this.timestamp = timestamp;
    }
    /**
     * Send question to server.
     * When a reply is received Question data are updated and stored in SQLite DB
     */
    public void commitQuestionToServer() throws JSONException {

        CommunicationHandler comm = CommunicationHandler.getCommunicationHandler();
        JSONObject questionData = comm.askQuestion(this);
        JSONObject replyData;

        if( questionData == null || !questionData.has("data") )
            return;

        replyData = questionData.getJSONObject("data");

        this.setQuestionID(replyData.getInt("questionid"));
        this.setTimestamp(replyData.getInt("timestamp"));

        storeQuestionInDatabase();
    }

    /**
     *
     * @return question id
     */
    public int getID() {
        return this.id;
    }

    /**
     *
     * @return question timestamp
     */
    public int getTimestamp() {
        return this.timestamp;
    }

    /**
     *
     * @return question content
     */
    public String getContent() {
        return this.text;
    }

    /**
     *
     * @return question category id
     */
    public int getCategory() {
        return this.categoryid;
    }

    /**
     *
     * @return question subcategory id
     */
    public int getSubcategoryid() {
        return this.subcategoryid;
    }

    /**
     *
     * @return anonymous flag
     */
    public Boolean getAnonflag() {
        return this.anonflag;
    }

    /**
     *
     * @param id set the id of the question returned from the server
     */
    private void setQuestionID( int id ) {
        this.id = id;
    }

    /**
     *
     * @param timestamp set the timestamp returned from the server
     */
    private void setTimestamp( int timestamp ) {
        this.timestamp = timestamp;
    }


    public String toJSONString() {

        JSONObject question = new JSONObject();

        try {
            question.put("text",this.text);
            question.put("category",this.categoryid);
            question.put("subcategory",this.subcategoryid);
            question.put("anon",this.getAnonflag());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return question.toString();
    }

    public void setContext( Context c ) {
        this.context = c;
    }
    /**
     * Store question in SQLite Database.
     * This is fired when the server replies with QuestionID and Timestamp
     */
    private void storeQuestionInDatabase() {

        // TODO -- store the question data in local SQLite DB
        Log.d("QUESTION_COMMIT_DB", "Hell yeah here we are");

        DBHelper mHelper = new DBHelper(context, "DB", new SQLiteDatabase.CursorFactory() {
            @Override
            public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String s, SQLiteQuery sqLiteQuery) {
                Log.d("CURSOR","String: "+s);
                return null;
            }
        },DBHelper.getVersion());

        SQLiteDatabase mDB = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.QuestionTable.ID,        this.getID());
        values.put(DatabaseContract.QuestionTable.CATEGORY,  this.getCategory());
        values.put(DatabaseContract.QuestionTable.TITLE,     this.getSubcategoryid());
        values.put(DatabaseContract.QuestionTable.ANONYMOUS, this.getAnonflag());
        values.put(DatabaseContract.QuestionTable.CONTENT,   this.getContent());
        values.put(DatabaseContract.QuestionTable.TIMESTAMP, this.getTimestamp());

        long newRowID = mDB.insert(DatabaseContract.QuestionTable.TABLE_NAME,null,values);
        Log.d("DB_INSERT","ID: ["+ ((Long)newRowID).toString() +"]" );
    }

    public static Question getQuestionDataFromID( int ID ) {

        String theText = "";
        int theCategory = 1;
        int theSubcategory = 1;
        Boolean theAnon = false;
        int theTimestamp = 1110000111;

        // TODO -- get question data from local SQLite DB


        return new Question(ID,theText,theCategory,theSubcategory,theAnon,theTimestamp);
    }
    /**
     * Retrieve an ArrayList containing QuestionID
     * stored in SQLite database
     *
     * @return ArrayList<Integer>
     */
    public static ArrayList<Integer> retrieveMyQuestionsID() {

        // TODO -- retrieve question ID from local SQLite DB
        ArrayList<Integer> mylist = new ArrayList<Integer>();

        mylist.add(1);
        mylist.add(2);

        return mylist;
    }

    /**
     * Retrieve an ArrayList containing Question data
     * stored in SQLite database
     *
     * @return ArrayList<Question>
     */
    public static ArrayList<Question> retrieveMyQuestions() {

        // TODO -- retrieve question data from local SQLite DB
        ArrayList<Question> mylist = new ArrayList<>();

        mylist.add(new Question("pippo",1,1,true));
        mylist.add(new Question("pluto",1,2,true));
        mylist.add(new Question("lorem",1,3,false));

        mylist.get(0).setQuestionID(1);
        mylist.get(1).setQuestionID(2);
        mylist.get(2).setQuestionID(3);
        return mylist;
    }
}

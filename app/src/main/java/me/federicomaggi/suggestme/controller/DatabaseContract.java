package me.federicomaggi.suggestme.controller;

import android.provider.BaseColumns;

/**
 * Created by federicomaggi on 24/06/15.
 * © 2015 Federico Maggi. All rights reserved
 */
public class DatabaseContract {

    public DatabaseContract() {}

    public static abstract class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME = "question";
        public static final String ID         = "id";
        public static final String CONTENT    = "text";
        public static final String CATEGORY   = "category";
        public static final String TITLE      = "title";
        public static final String ANONYMOUS  = "anonflag";
        public static final String TIMESTAMP  = "timestamp";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + QuestionTable.TABLE_NAME + " (" +
                        QuestionTable._ID       + " INTEGER PRIMARY KEY," +
                        QuestionTable.ID        + " INTEGER, " +
                        QuestionTable.CONTENT   + " VARCHAR, " +
                        QuestionTable.CATEGORY  + " INTEGER, " +
                        QuestionTable.TITLE     + " INTEGER, " +
                        QuestionTable.ANONYMOUS + " TINYINT, " +
                        QuestionTable.TIMESTAMP + " INTEGER  " +
                        ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME;

    }

    public static abstract class SuggestTable implements BaseColumns {
        public static final String TABLE_NAME = "suggest";
        public static final String ID         = "id";
        public static final String QUESTIONID = "question_id";
        public static final String CONTENT    = "text";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + SuggestTable.TABLE_NAME + " (" +
                        SuggestTable._ID        + " INTEGER PRIMARY KEY, " +
                        SuggestTable.ID         + " INTEGER, " +
                        SuggestTable.QUESTIONID + " INTEGER, " +
                        SuggestTable.CONTENT    + " VARCHAR " +
                        ")";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + SuggestTable.TABLE_NAME;
    }
}

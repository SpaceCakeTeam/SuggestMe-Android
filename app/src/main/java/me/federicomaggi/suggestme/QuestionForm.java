package me.federicomaggi.suggestme;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class QuestionForm extends ActionBarActivity {

    public static SuggestItem.SuggestCategory new_question_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form);

        Intent i = getIntent();

        /* TEST */
        TextView m_txtview_TEST = (TextView) findViewById(R.id.testlbl);
        /********/

        if( i.getBooleanExtra(SuggestItem.SOCIAL_TAG, false) ){

            /* Set SOCIAL flag to request object */
            new_question_category = SuggestItem.SuggestCategory.SOCIAL;

            m_txtview_TEST.setText(m_txtview_TEST.getText().toString().concat(" SOCIAL"));

        }else{

            /* Set GOODS flag to request object */
            new_question_category = SuggestItem.SuggestCategory.GOODS;

            m_txtview_TEST.setText( m_txtview_TEST.getText().toString().concat(" GOODS") );
        }


        Button m_test_button = (Button) findViewById(R.id.testbtn);

        /* This should be done on keyboard "send" button */
        m_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SuggestItem si = new SuggestItem("TITOLO","CONTENUTO", new_question_category);

                HTTPHandler m_http_handler = new HTTPHandler();

                m_http_handler.setSuggestItemToSend(si);
                m_http_handler.sendSuggestRequest();





            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

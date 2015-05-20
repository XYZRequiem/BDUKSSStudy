package com.example.requiem.bdukssstudy;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends ActionBarActivity {


    // Initializes variables
    private final static String STORETEXT = "storetext.txt";
    private final static String STOREID = "storeid.txt";
    public EditText newNumber, newPass;
    private TextView idt, idn;
    Context context;
    private PopupWindow CID, RS, AP;


    // Sets parameters on instance create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton  = (Button) this.findViewById(R.id.start_button);
        Button aboutButton = (Button) this.findViewById(R.id.about_button);
        idt = (TextView) this.findViewById(R.id.idnumber);
        idn = (TextView) this.findViewById(R.id.numberid);
        context = getApplicationContext();

        try {
            InputStream inp = openFileInput(STOREID);

            if (inp != null) {
                InputStreamReader temp = new InputStreamReader(inp);

                BufferedReader read = new BufferedReader(temp);

                String str;

                StringBuilder buf = new StringBuilder();

                while ((str = read.readLine()) != null) {

                    buf.append(str+"\n");

                }
                inp.close();

                idn.setText(buf.toString());
            }
        } catch (IOException t) {
            Toast.makeText( this, "Exception: " + context.toString(), Toast.LENGTH_LONG).show();


        }


        startButton.setOnClickListener(onClickListener);
        aboutButton.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // Sets what happens when menu items are pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.view_results) {
           initiateVRPopupWindow();

            return true;
        } else if (id == R.id.change_id_number) {
            initiateCIDPopupWindow();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Initializes the Change ID Popup Window
    private void initiateVRPopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.result_screen,
             (ViewGroup) findViewById(R.id.result_screen_popup));

            RS = new PopupWindow(layout, 700, 940, true);
            RS.showAtLocation(layout, Gravity.CENTER, 0, 0);

            Button Exit = (Button) layout.findViewById(R.id.RSExit);
            TextView FinalResult = (TextView) layout.findViewById(R.id.RSResultText);

            try {
                InputStream in = openFileInput(STORETEXT);

                if (in != null) {
                    InputStreamReader tmp = new InputStreamReader(in);

                    BufferedReader reader = new BufferedReader(tmp);

                    String str;

                    StringBuilder buf = new StringBuilder();

                    while ((str = reader.readLine()) != null) {

                        buf.append(str+"\n");

                    }
                    in.close();

                    FinalResult.setText(buf.toString());
                }
            } catch (java.io.FileNotFoundException e) {
                Toast.makeText( this, "Exception: " + context.toString(), Toast.LENGTH_LONG).show();
            }


            // Create onClickListener to exit the View Result Screen
            Exit.setOnClickListener(onClickListener);
        } catch (Exception c) {
            c.printStackTrace();
        }
    }

    // Initializes the About Page Popup Window
    private void initiateAPPopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.about_study,
                    (ViewGroup) findViewById(R.id.about_page));

            AP = new PopupWindow(layout, 700, 940, true);
            AP.showAtLocation(layout, Gravity.CENTER, 0, 0);

            Button APExit = (Button) layout.findViewById(R.id.KSSExit);

            APExit.setOnClickListener(onClickListener);
        } catch (Exception c) {
            c.printStackTrace();
        }
    }


    // Initializes the Change ID Popup Window
    private void initiateCIDPopupWindow() {

        try {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.id_change_popup,
            (ViewGroup) findViewById(R.id.CIDpopup_element));

            CID = new PopupWindow(layout, 700, 940, true);

            CID.showAtLocation(layout, Gravity.CENTER, 0, 0);

            newPass = (EditText) layout.findViewById(R.id.CIDPassword);
            newNumber = (EditText) layout.findViewById(R.id.CIDNewNumber);
            Button CIDSubmit = (Button) layout.findViewById(R.id.CIDSubmit);
            Button CIDCancel = (Button) layout.findViewById(R.id.CIDCancel);

            CIDCancel.setOnClickListener(onClickListener);
            CIDSubmit.setOnClickListener(onClickListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // What happens when buttons are pressed
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.start_button:
                    Log.e("BDUKSSS", "Starting Shift");

                    String IDText = String.valueOf(idt.getText());
                    String IDNumber = String.valueOf(idn.getText());
                    String popup = "Hello " + IDText + " " + IDNumber;

                    Toast toast = Toast.makeText(context, popup, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();

                    Intent questionScreenGet = new Intent(MainActivity.this, QuestionScreen.class);
                    startActivity(questionScreenGet);

                    break;
                case R.id.about_button:
                        Log.e("BDUKSSS", "Open About Screen popup");
                        initiateAPPopupWindow();

                    break;

                case R.id.KSSExit:
                       Log.e("BDUKSSS", "Exit About Screen popup");

                    AP.dismiss();

                    break;

                case R.id.CIDSubmit:
                      Log.e("BDUKSSS", "Submitting New ID#");

                    String plusPass = getResources().getString(R.string.correctPass);
                    String minusPass = getResources().getString(R.string.wrongPass);
                    String newID;


                    if (newPass.getText().toString().equals("BronsonDu")) {

                        newID = newNumber.getText().toString();

                        plusPass += newID;

                        Toast correctPass = Toast.makeText(context, plusPass, Toast.LENGTH_LONG);
                        correctPass.show();

                        try {
                            OutputStreamWriter output =
                                    new OutputStreamWriter(openFileOutput(STOREID, 0));

                            output.write(newID);

                            output.close();
                        } catch (Throwable t) {
                            Toast.makeText( MainActivity.this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
                        }

                        try {
                            OutputStreamWriter clear =
                                    new OutputStreamWriter(openFileOutput(STORETEXT, 0));

                            clear.write("");

                            clear.close();
                        } catch (Throwable t) {
                            Toast.makeText( MainActivity.this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast wrongPass = Toast.makeText(context, minusPass, Toast.LENGTH_SHORT);
                        wrongPass.show();
                    }

                    break;

                case R.id.CIDCancel:
                        Log.e("BDUKSSS", "Close Change ID popup");
                    CID.dismiss();

                    finish();
                    startActivity(getIntent());

                    break;

                case R.id.RSExit:
                        Log.e("BDUKSSS", "Close Result Screen popup");
                    RS.dismiss();

                    break;
            }

        }
    };

}

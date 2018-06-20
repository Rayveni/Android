package com.kart.track24;



import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    TextView txtString;
    Button asynchronousGet, synchronousGet, asynchronousPOST;
    private String request_constructor,key,request_url,domain,track_code,create_table_sql;

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    public String table_name="contacts";
    public String url ;//"https://reqres.in/api/users/2";
    public String postUrl = "https://reqres.in/api/users/";
    public String postBody = "{\n" +
            "    \"name\": \"morpheus\",\n" +
            "    \"job\": \"leader\"\n" +
            "}";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //String table_name="contacts";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //~~~~~~~~~~~~~~~~~~~table view~~~~~~~~~~~~~~~~~

        mProgressBar = new ProgressDialog(this);

        // setup the table
        mTableLayout = (TableLayout) findViewById(R.id.main_table);
        mTableLayout.setStretchAllColumns(true);
        startLoadData();
        //~~~~~~~~~~~~~~~~~~~Requests~~~~~~~~~~~~~~~~~
        request_constructor=getResources().getString(R.string.request_constructor);
        key=getResources().getString(R.string.key);
        request_url=getResources().getString(R.string.url);
        domain=getResources().getString(R.string.domain);
        track_code=getResources().getString(R.string.track_code);

        url=String.format(request_constructor,request_url,key,domain,track_code);

        create_table_sql= String.format(getResources().getString(R.string.create_table_tracks),table_name);
       // Log.d("TAG12",getResources().getString(R.string.create_table_tracks2));
        //create_table_sql=create_table_sql.format(table_name);

        Log.d("TAG12",create_table_sql);
        Log.d("TAG123","start");

        database_handler db = new database_handler(this,create_table_sql,table_name);

        db.execSQL(true,create_table_sql);
        //db.execSQL(true,"drop table "+table_name);

        Log.d("TAG1234","finish");
      //  String printT=db.print_table(table_name);
       // Log.d("db123",printT);

        String insert_table="INSERT INTO contacts (invoiceAmount,amountDue,invoiceAmount,invoiceDate,customerName,customerAddress)\n" +
                "VALUES\n" +
                " (2,122,33,'2000-03-11','Thomas John Beckett','1112, Hash Avenue, NYC');";
       // db.deleteAll();

        db.execSQL(true,insert_table);

        //db.addContact(new track("Empty and One contact", "806800000"));
        //row.invoiceNumber = row.id;
        // row.amountDue = BigDecimal.valueOf(20.00 * i);
        // row.invoiceAmount = BigDecimal.valueOf(120.00 * (i+1));
        // row.invoiceDate = new Date();
        // row.customerName =  "Thomas John Beckett";
        // row.customerAddress = "1112, Hash Avenue, NYC";


        Log.d("TAG1235", String.valueOf(db.getContactsCount()));

        System.out.println("Reading all contacts..");
        List <track> contacts = db.getAllContacts();
        for (track cn : contacts) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            System.out.print("Name: ");
            System.out.println(log);
        }

        //Log.d("TAG123", utils.getUserName().get("Monday").toString());
        asynchronousGet = (Button) findViewById(R.id.asynchronousGet);
        synchronousGet = (Button) findViewById(R.id.synchronousGet);
        asynchronousPOST = (Button) findViewById(R.id.asynchronousPost);

        asynchronousGet.setOnClickListener(this);
        synchronousGet.setOnClickListener(this);
        asynchronousPOST.setOnClickListener(this);

        txtString = (TextView) findViewById(R.id.txtString);
    }

   // void postRequest(String postUrl, String postBody) throws IOException {
//
    //    OkHttpClient client = new OkHttpClient();

    //    RequestBody body = RequestBody.create(JSON, postBody);

   //     Request request = new Request.Builder()
      //          .url(postUrl)
      //          .post(body)
      //          .build();
//
      //  client.newCall(request).enqueue(new Callback() {
      //      @Override
      //      public void onFailure(Call call, IOException e) {
     //          call.cancel();
      //      }

        //    @Override
       //     public void onResponse(Call call, Response response) throws IOException {

         //       Log.d("TAG", response.body().string());
         //   }
       // });
  //  }
         public static void postRequest(String postUrl, String postBody) throws IOException {

             OkHttpClient client = new OkHttpClient();

             RequestBody body = RequestBody.create(JSON, postBody);

             Request request = new Request.Builder()
                     .url(postUrl)
                     .post(body)
                     .build();

             client.newCall(request).enqueue(new Callback() {
                 @Override
                 public void onFailure(Call call, IOException e) {
                     call.cancel();
                 }

                 @Override
                 public void onResponse(Call call, Response response) throws IOException {

                     Log.d("TAG", response.body().string());
                 }
             });
         }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                           // txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));
                            txtString.setText(myResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.asynchronousGet:
                try {
                    run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.synchronousGet:
                OkHttpHandler okHttpHandler = new OkHttpHandler();
                okHttpHandler.execute(url);
                break;
            case R.id.asynchronousPost:
                try {
                    utils.postRequest(postUrl, postBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }







    public void startLoadData() {

        mProgressBar.setCancelable(false);
        mProgressBar.setMessage("Fetching Invoices..");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
        new LoadDataTask().execute(0);

    }

    public void loadData() {

        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize =0, mediumTextSize = 0;

        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

      //  Invoices invoices = new Invoices();
       // InvoiceData[] data = invoices.getInvoices();
        database_handler db = new database_handler(this,create_table_sql,table_name);

        //JSONArray jsonArray = db.jsonTable(table_name);
       // List<Map> df=db.dataframe(table_name);

       // Log.d("json123",jsonArray.toString());
       // Log.d("json1234",Integer.toString(df.size()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

       // int rows = data.length;


        List header=Arrays.asList(db.column_names(table_name));
        String[][]  matrix=db.dataframe2(table_name);


        int invoiceNumber_id= header.indexOf("invoiceNumber");
        int invoiceDate_id= header.indexOf("invoiceDate");
        int customerName_id= header.indexOf("customerName");
        int customerAddress_id= header.indexOf("customerAddress");
        int invoiceAmount_id= header.indexOf("invoiceAmount");
        int amountDue_id= header.indexOf("amountDue");

        int rows_num=matrix.length;
        Log.d("json5",Integer.toString(rows_num));
        //getSupportActionBar().setTitle("Invoices (" + String.valueOf(rows_num) + ")");
        getSupportActionBar().setTitle(String.format("%s (%d)",table_name, rows_num));
        TextView textSpacer = null;

        mTableLayout.removeAllViews();

        // -1 means heading row
        for(int i = -1; i < rows_num; i ++) {
          //  InvoiceData row = null;
           // Map row2;
            if (i > -1)
            {// row = data[i];
           // row2=matrix[i][i];
             //   Log.d("json1235",Integer.toString(row2.size()));

            }
            else {
                textSpacer = new TextView(this);
                textSpacer.setText("");

            }
            // data columns
            final TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.LEFT);

            tv.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv.setText("Inv.#");
                tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(matrix[i][invoiceNumber_id]);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            final TextView tv2 = new TextView(this);
            if (i == -1) {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            tv2.setGravity(Gravity.LEFT);

            tv2.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv2.setText("Date");
                tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
            }else {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                //tv2.setText(dateFormat.format(matrix[i][invoiceDate_id]));
                tv2.setText(matrix[i][invoiceDate_id]);
            }


            final LinearLayout layCustomer = new LinearLayout(this);
            layCustomer.setOrientation(LinearLayout.VERTICAL);
            layCustomer.setPadding(0, 10, 0, 10);
            layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"));

            final TextView tv3 = new TextView(this);
            if (i == -1) {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 5, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv3.setPadding(5, 0, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            tv3.setGravity(Gravity.TOP);


            if (i == -1) {
                tv3.setText("Customer");
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv3.setText(matrix[i][customerName_id]);
            }
            layCustomer.addView(tv3);


            if (i > -1) {
                final TextView tv3b = new TextView(this);
                tv3b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tv3b.setGravity(Gravity.RIGHT);
                tv3b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3b.setPadding(5, 1, 0, 5);
                tv3b.setTextColor(Color.parseColor("#aaaaaa"));
                tv3b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3b.setText(matrix[i][customerAddress_id]);
                layCustomer.addView(tv3b);
            }

            final LinearLayout layAmounts = new LinearLayout(this);
            layAmounts.setOrientation(LinearLayout.VERTICAL);
            layAmounts.setGravity(Gravity.RIGHT);
            layAmounts.setPadding(0, 10, 0, 10);
            layAmounts.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));



            final TextView tv4 = new TextView(this);
            if (i == -1) {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
                tv4.setPadding(5, 5, 1, 5);
                layAmounts.setBackgroundColor(Color.parseColor("#f7f7f7"));
            } else {
                tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv4.setPadding(5, 0, 1, 5);
                layAmounts.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            tv4.setGravity(Gravity.RIGHT);

            if (i == -1) {
                tv4.setText("Inv.Amount");
                tv4.setBackgroundColor(Color.parseColor("#f7f7f7"));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv4.setBackgroundColor(Color.parseColor("#ffffff"));
                tv4.setTextColor(Color.parseColor("#000000"));
               //tv4.setText(decimalFormat.format(matrix[i][invoiceAmount_id]));
                tv4.setText(matrix[i][invoiceAmount_id]);
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            layAmounts.addView(tv4);


            if (i > -1) {
                final TextView tv4b = new TextView(this);
                tv4b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                tv4b.setGravity(Gravity.RIGHT);
                tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv4b.setPadding(2, 2, 1, 5);
                tv4b.setTextColor(Color.parseColor("#00afff"));
                tv4b.setBackgroundColor(Color.parseColor("#ffffff"));

                String due = "";//Integer.parseInt
                if (new BigDecimal(matrix[i][amountDue_id]).compareTo(new BigDecimal(0.01)) == 1) {
                    due = "Due:" + decimalFormat.format(new BigDecimal(matrix[i][amountDue_id]));
                    due = due.trim();
                }
                tv4b.setText(matrix[i][amountDue_id]);
                layAmounts.addView(tv4b);
            }


            // add table row
            final TableRow tr = new TableRow(this);
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);



            tr.addView(tv);
            tr.addView(tv2);
            tr.addView(layCustomer);
            tr.addView(layAmounts);

            if (i > -1) {

                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        TableRow tr = (TableRow) v;
                        //do whatever action is needed

                    }
                });


            }
            mTableLayout.addView(tr, trParams);

            if (i > -1) {

                // add separator row
                final TableRow trSep = new TableRow(this);
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(this);
                TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                tvSepLay.span = 4;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);

                trSep.addView(tvSep);
                mTableLayout.addView(trSep, trParamsSep);
            }


        }
    }

//////////////////////////////////////////////////////////////////////////////

//
// The params are dummy and not used
//
class LoadDataTask extends AsyncTask<Integer, Integer, String> {
    @Override
    protected String doInBackground(Integer... params) {

        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Task Completed.";
    }
    @Override
    protected void onPostExecute(String result) {
        mProgressBar.hide();
        loadData();
    }
    @Override
    protected void onPreExecute() {
    }
    @Override
    protected void onProgressUpdate(Integer... values) {

    }
}

}
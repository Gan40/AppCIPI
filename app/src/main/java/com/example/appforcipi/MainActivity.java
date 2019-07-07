package com.example.appforcipi;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private volatile Boolean flag = false;
    private Thread myThread;
    private int TIME_TO_PING = 5*60*1000;//30 минут
    private Integer[] TIME_TO_PING_ARRAY = {5, 10, 15, 20, 30, 45, 60};

    private static final String APP_PREFERENCES = "mySettings";
    private static final String APP_PREFERENCES_FIRST_START = "firstStart";
    private static final String APP_PREFERENCES_SELECTED_PROVIDER = "selectedProvider";
    private SharedPreferences myPref;

    private final int IDD_LIST_PROVIDERS = 1;

    private android.widget.Toolbar toolBar;
    private TextView textView;
    private RelativeLayout timer_rl;
    private TextView timer_tv_1;
    private TextView timer_tv_2;
    private Chronometer timer;
    private Spinner spinner;
    private Button start;
    private Button choose;
    private FloatingActionButton stop;
    //private RecyclerView list;
    private ListView list;


    private ArrayList<ProvidersBean> providersArray;
    private ArrayList<SitesBean> sitesArray;


    private String jsonin = "";
    private String jsonout = "";

    final Handler handler = new Handler();
    private long timeStart = 0;
    private long timeFinish = 0;


    private String[] mSitesName;

    @SuppressLint("RestrictedApi")
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar = (android.widget.Toolbar)findViewById(R.id.toolbar);
        textView = (TextView)findViewById(R.id.checkTextView);
        timer =(Chronometer)findViewById(R.id.timer);
        spinner = (Spinner) findViewById(R.id.spinner);
        start = (Button) findViewById(R.id.startButton);
        choose = (Button)findViewById(R.id.actionBarButton);
        stop = (FloatingActionButton)findViewById(R.id.stopButton);
        list = (ListView) findViewById(R.id.sitesListView);

        timer_rl = (RelativeLayout)findViewById(R.id.timer_RL);
        timer_tv_1 = (TextView)findViewById(R.id.timer_TV_1);
        timer_tv_2 = (TextView)findViewById(R.id.timer_TV_2);

        setActionBar(toolBar); //установка кастомного action bar
        timer.setCountDown(true);
        start.setOnClickListener(this);
        choose.setOnClickListener(this);
        stop.setOnClickListener(this);
        timer.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);


        //SPINNER
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,TIME_TO_PING_ARRAY);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                Integer item = (Integer)parent.getItemAtPosition(position);
                timer_tv_2.setText(item + " мин");
                TIME_TO_PING = item * 60 * 1000;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }};

        spinner.setOnItemSelectedListener(itemSelectedListener);

        GetContent getContent = new GetContent();
        getContent.execute();
        try {
            getContent.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String in = getContent.getResultJson();
        try{
        if (!in.equals("")) {
                String[] inArray = in.split(",\"sites\"");
                String inJsonProviders = inArray[0].substring(inArray[0].indexOf(":") + 1);
                String inJsonSites = inArray[1].substring(inArray[1].indexOf(":") + 1, inArray[1].length() - 1);


                Gson gson = new Gson();
                //Providers
                Type type1 = new TypeToken<ArrayList<ProvidersBean>>() {
                }.getType();
                providersArray = gson.fromJson(inJsonProviders, type1);
                //Sites
                Type type2 = new TypeToken<ArrayList<SitesBean>>() {
                }.getType();
                sitesArray = gson.fromJson(inJsonSites, type2);
                // Заполнение ListView (Список сайтов)
                String[] mSitesName = new String[sitesArray.size()];
                for (int i = 0; i < sitesArray.size(); i++) {
                    mSitesName[i] = sitesArray.get(i).getSite();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, mSitesName);
                list.setAdapter(adapter);

                //Работа с файлами настройки(SharedPreferences) для выявления первого запуска и сохранение выбранного провайдера
                myPref = getPreferences(MODE_PRIVATE);
                if (myPref.getBoolean(APP_PREFERENCES_FIRST_START, true)) {
                    showDialog(IDD_LIST_PROVIDERS);
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putBoolean(APP_PREFERENCES_FIRST_START, false);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putBoolean(APP_PREFERENCES_FIRST_START, true);
                    editor.apply();
                }
                choose.setText(myPref.getString(APP_PREFERENCES_SELECTED_PROVIDER, "ПРОВАЙДЕР"));
            }else{
                Toast.makeText(this, "ERROR LOADING", LENGTH_LONG).show();
                Toast.makeText(this, "RESTART APP", LENGTH_LONG).show();
            }
        }catch (RuntimeException e){
            start.setClickable(false);
            stop.setClickable(false);
            choose.setClickable(false);
            Toast.makeText(this, "ERROR LOADING", LENGTH_LONG).show();
            Toast.makeText(this, "CHECK CONNECTION AND RESTART APP", LENGTH_LONG).show();
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startButton:
                if (!choose.getText().equals("ПРОВАЙДЕР")) {
                    Toast.makeText(getApplicationContext(), "СТАРТ", LENGTH_LONG).show();

                    flag = false;
                    timer.setBase(SystemClock.elapsedRealtime() + TIME_TO_PING);
                    timer.start();
                    timer.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.VISIBLE);
                    timer_tv_1.setVisibility(View.GONE);
                    timer_tv_2.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    choose.setClickable(false);
                    start.setClickable(false);

                    myThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("thread", "START THREAD");
                            while (!flag) {
                                RequestTask request = new RequestTask();
                                request.execute();
                                try {
                                    request.get();
                                    Thread.sleep(TIME_TO_PING-(timeFinish-timeStart));
                                    Log.e("thread", "PING");
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            timer.setBase(SystemClock.elapsedRealtime() + TIME_TO_PING);
                                            timer.start();
                                        }
                                    });
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                    myThread.start();
                }else
                    Toast.makeText(MainActivity.this, "Выберите провайдера", LENGTH_LONG).show();
                break;

            case R.id.actionBarButton:
                Toast.makeText(getApplicationContext(),"ПРОВАЙДЕРЫ", LENGTH_LONG).show();
                showDialog(IDD_LIST_PROVIDERS);
                break;

            case R.id.stopButton:
                Toast.makeText(getApplicationContext(),"СТОП", LENGTH_LONG).show();
                myThread.interrupt();
                //myThread.stop();
                if (myThread.isInterrupted()) Log.e("thread","STOP THREAD");
                flag = true;
                timer.stop();
                timer.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                timer_tv_1.setVisibility(View.VISIBLE);
                timer_tv_2.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                choose.setClickable(true);
                start.setClickable(true);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id){
            case IDD_LIST_PROVIDERS:
                final String[] mProvidersName = new String[providersArray.size()];
                for (int i = 0; i < providersArray.size(); i++){
                     mProvidersName[i] = providersArray.get(i).getProvider_name();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Выберите провайдера"); // заголовок для диалога
                builder.setItems(mProvidersName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(),
                                "Выбранный провайдер: " + mProvidersName[item],
                                Toast.LENGTH_SHORT).show();
                        choose = (Button)findViewById(R.id.actionBarButton);
                        choose.setText(mProvidersName[item]);
                        myPref = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPref.edit();
                        editor.putString(APP_PREFERENCES_SELECTED_PROVIDER,mProvidersName[item]);
                        editor.apply();
                    }
                });
                builder.setCancelable(false);
                return builder.create();

            default:
                return null;
        }
    }

    private class GetContent extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {

            try{
                URL url = new URL("http://134.0.113.128/index.php?r=api%2Fproviders");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                resultJson = buffer.toString();
                setResultJson(resultJson);
                return resultJson;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Result:","POST " + s);
            jsonin = s;
        }

        public String getResultJson() {
            return resultJson;
        }

        public void setResultJson(String resultJson) {
            this.resultJson = resultJson;
        }
    }

    private class RequestTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            timeStart = System.currentTimeMillis();
            ArrayList<String> siteHashCodeArray = new ArrayList<>();
            ArrayList<Integer> statusArray = new ArrayList<>();
            ArrayList<Long> timestampArray = new ArrayList<>();
            for (int i = 0; i < sitesArray.size(); i++) {
                String site = sitesArray.get(i).getSite().replaceFirst("https", "http");
                int responseCode = 404;
                String responseMessage = "Not Found";
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) new URL(site).openConnection();
                    connection.setRequestProperty("Accept-Encoding", "");
                    connection.setRequestMethod("HEAD");
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(15000);
                    responseCode = connection.getResponseCode();
                    responseMessage = connection.getResponseMessage();
                } catch (ConnectException e) {
                    responseCode = 403;
                    responseMessage = "Forbidden";
                } catch (SocketTimeoutException e) {
                    responseCode = 408;
                    responseMessage = "Request Timeout";
                } catch (UnknownHostException e) {
                } catch (SocketException e) {
                    responseCode = 400;
                    responseMessage = "Bad Request";
                } catch (Exception e) {
                } finally {
                    connection.disconnect();
                }

                siteHashCodeArray.add(sitesArray.get(i).getHashCode());
                statusArray.add(responseCode);
                timestampArray.add(System.currentTimeMillis());

                System.out.println((i + 1) + ") " + sitesArray.get(i).getSite() + " : " + responseCode + " : " + responseMessage);
            }
            SendMessageClass send = new SendMessageClass(choose.getText().toString(), siteHashCodeArray, statusArray, timestampArray);

            //ОТПРАВКА ДАННЫХ МЕТОДОМ POST
            URL url = null;
            try {
                url = new URL("http://134.0.113.128/index.php?r=api%2Fmobile-data");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                //ОТПРАВКА JSON
                //String outStr = "{ \"provider\": \"babilon\", \"sites\": { \"aed92a64fde04639f83b7efc5f10b42d1fed2e3f\": { \"statusCode\": 200, \"timestamp\": \"1560540423\" }, \"3873b9eebc69b5904e6cba65a4304a7b1302731c\": { \"statusCode\": 403, \"timestamp\": \"1560540423\" }, \"6f19df22e7ab23e02897572406bd21110a6ff77f\": { \"statusCode\": 401, \"timestamp\": \"1560540423\" }, \"45006b107dd20312dccda4c20dd75fa6a7c72564\": { \"statusCode\": 301, \"timestamp\": \"1560540423\" } } }";
                String outStr = send.toString();
                System.out.println("TO STRING ANSWER : " + outStr );
                byte[] out = outStr.getBytes();
                int length = out.length;
                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/json: charset=UTF-8");
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
                //ПОЛУЧАЕМ ОТВЕТ ОТ СЕРВЕРА
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] data = null;
                String resultString = null;
                if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = http.getInputStream();
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    data = baos.toByteArray();
                    resultString = new String(data, "UTF-8");
                    final String finalResultString = resultString;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "SUCCESS !!!", LENGTH_LONG).show();
                        }
                    });
                    System.out.println(resultString);
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "BADLY !!!", LENGTH_LONG).show();
                        }
                    });
                }
                http.setRequestMethod("GET");
                System.out.println(http.getResponseCode() + " : " + http.getResponseMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }

            timeFinish = System.currentTimeMillis();

            return null;
        }
    }
}

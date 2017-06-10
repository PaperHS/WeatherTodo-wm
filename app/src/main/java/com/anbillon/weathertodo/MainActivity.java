package com.anbillon.weathertodo;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    ListAdapter adapter ;
//    ListView listView;
    List<ThingTodo> things = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerAdapter adapter ;

    Intent serverIntent ;

    ProgressDialog progressDialog;
    ViewPager vp;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        vp = (ViewPager)findViewById(R.id.viewpager);
        List<View> dds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TextView ttttt = new TextView(this);
            ttttt.setText("position`  "+i);
            ttttt.setTextColor(Color.BLUE);
            dds.add(ttttt);
        }
        VpAdapter vpAdapter = new VpAdapter(dds);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("debug","position:"+position + "   positionOffset+"+positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("debug","position:"+position );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("debug","state:"+state );
            }
        });
        vp.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

            }
        });
        vp.setAdapter(vpAdapter);


//        adapter = new ListAdapter(things,this);
        things.clear();
        things.addAll(queryInfo());
        adapter = new RecyclerAdapter(things,this);
        recyclerView = (RecyclerView)findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        adapter.setListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("debug","click pos="+position);
            }
        });
        serverIntent = new Intent(MainActivity.this,MyService.class);


//        listView = (ListView)findViewById(R.id.listview);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ThingTodo thingTodo = adapter.getItem(position);
//                Log.d("debug","click `:"+ thingTodo.thing);
//            }
//        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                things.remove(position);
//                adapter.notifyDataSetChanged();
//                return false;
//            }
//        });

        setSupportActionBar(toolbar);


//        IntentFilter intentFilter = new IntentFilter("com.anbillon.demo");
//        intentFilter.setPriority(200);
//        registerReceiver(bir,intentFilter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

//                new BackGroundTask().execute("p11111","p22222");
//        bindService(serverIntent,sc ,Context.BIND_AUTO_CREATE);
//            startService(serverIntent);
//        MyIntentService.startActionBaz(MainActivity.this,"a","b");
//        MyIntentService.startActionFoo(MainActivity.this,"C","D");
                Log.d("debug","onclick");
                startActivityForResult(new Intent(MainActivity.this,WriteNewTodoActivity.class),1);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                unbindService(sc)
                    stopService(serverIntent);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.anbillon.weathertodo.demo");
        filter.setPriority(1);
        registerReceiver(bir,filter);



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("进度君");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
   private ServiceConnection sc =   new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("debug","onServiceConnected");
            MyService.MyBinder b = (MyService.MyBinder)service;
            b.getTest();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("debug","onServiceDisconnected");
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bir);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            things.clear();
            things.addAll(queryInfo());
            adapter.notifyDataSetChanged();
        }
    }

    public List<ThingTodo> queryInfo(){
//        ThingTodoSqlHelper helper = new ThingTodoSqlHelper(getApplicationContext());
//        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
//        Cursor c = sqLiteDatabase.query(ThingTodo.TABLE_NAME,new String[]{
//            ThingTodo.THING,
//            ThingTodo.TIME,
//            ThingTodo.NOTI
//        },null,null,null,null,ThingTodo.TIME);
        Cursor c =  getContentResolver().query(MyContentProvider.CONTENT_URI,new String[]{
            ThingTodo.THING,
            ThingTodo.TIME,
            ThingTodo.NOTI
        },null,null,ThingTodo.TIME);

        List<ThingTodo> thingTodos = new ArrayList<>();
        c.moveToFirst();
        do{
            String s = c.getString(0);
            long time = c.getLong(1);
            long noti = c.getInt(2);
            thingTodos.add(new ThingTodo(time,s,noti == 1));
        }while (c.moveToNext());
        c.close();
//        helper.close();
        return thingTodos;
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            writePreference();
//            try {
//                javaWrite();
//            }catch (Exception e){
//                Log.e("debug",e.getMessage());
//            }
            return true;
        } else if (id == R.id.action_read) {
            readPreference();
//            try {
//                javaRead();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        return super.onOptionsItemSelected(item);
    }







    public void javaWrite() throws IOException {
        File file = new File(getExternalCacheDir().getAbsolutePath()+"/text.txt");
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream f = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(f,"utf-8");
        writer.append("123");
        writer.append("456");
        writer.close();
        f.close();
    }

    public void javaRead() throws IOException {
        File file = new File(getExternalCacheDir().getAbsolutePath()+"/text.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(fileInputStream,"utf-8");
        StringBuffer stringBuffer = new StringBuffer("");
        while (reader.ready()){
            stringBuffer.append((char)reader.read());
        }
        reader.close();
        fileInputStream.close();
        Log.d("debug",stringBuffer.toString());
    }

    public void writePreference(){
        SharedPreferences settings = getSharedPreferences("Weather", MODE_PRIVATE);
        settings.edit().putBoolean("open",true).apply();
        settings.edit().putInt("day",21).apply();
        settings.edit().putString("thing","sleep").apply();
        settings.edit().commit();

        Log.d("debug","write");
    }

    public void readPreference(){
        SharedPreferences settings = getSharedPreferences("Weather", MODE_PRIVATE);
        int day = settings.getInt("day",0);
        int month = settings.getInt("month",10);
        String th = settings.getString("thing","");
        Log.d("debug","day = " + day +"   month="+month +"  things="+th);
    }
    private BroadCastInnerReciever bir = new BroadCastInnerReciever();
    private class BroadCastInnerReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("debug","main:   "+intent.getStringExtra("info"));
        }
    }



    /**
     * asyncTask
     */
    private class BackGroundTask extends AsyncTask<String,Integer,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("debug","p0:"+params[0]);
            Log.d("debug","p1:"+params[1]);
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
        }
    }

}

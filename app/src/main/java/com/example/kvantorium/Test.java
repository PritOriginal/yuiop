package com.example.kvantorium;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.kvantorium.server.GetUser;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnUserListener {
    SQLiteDatabase database;

    FloatingActionButton create;
    FloatingActionButton clear;
    LinearLayout lay;
    TableLayout Table;
    DBHelper dbHelper;
    RecyclerView recyclerView;
    RVAdapter adapter;
    NavigationView navigationView;
    DrawerLayout drawer;
    List<Project> projects = new ArrayList<Project>();
    TextView title_name;

    public int USER_ID = 0;
    private int countID;

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_USER_ID = "USER_ID";
    User user = new User();

    SharedPreferences sPref;

    final String TAG = "dig1";

    String HOST = "91.219.102.45";
    int PORT = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mSettings = getSharedPreferences("MyPref", MODE_PRIVATE);
        login();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigationView.setCheckedItem();

        GetUser task = new GetUser(this, USER_ID);
        task.execute();
        setItem();

        String data;
 //       Server server = new Server(HOST, PORT);
        try {
        //    data = Server.connect();

        //    System.out.println(data);


/*
            server.openConnection();
            server.sendData("test");
            data = server.getData();
            Toast toast = Toast.makeText(getApplicationContext(),
                    data,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
*/
        } catch (Exception e) {
            e.printStackTrace();
  //          server = null;
        }




        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        //dbHelper.onCreate( database);
       // ContentValues contentValues1 = new ContentValues();
       // contentValues1.put(dbHelper.ID_USER, 0);
       // contentValues1.put(dbHelper.NAME, "????????????");
       // contentValues1.put(dbHelper.DESCRIPTION, "????????????????");
      //  database.insert(dbHelper.TABLE_NAME, null, contentValues1);


  //      user.setFirstName("????????????");
  //      user.setSecondName("??????????????????");
  //      user.setRaiting(56);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt("USER_ID", Integer.parseInt("0"));
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_qr) {
            DialogFragment dialogFragment = new QRCodeFragment();
            dialogFragment.show(getSupportFragmentManager(), TAG);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Context context = null;
        int i = 0;
        int id = item.getItemId();

        if (id == R.id.nav_projects) {
            fragment = new FragmentOne();
        } else if (id == R.id.nav_components) {
            fragment = new FragmentTwo();
        } else if (id == R.id.nav_profile) {
            fragment = new Profile();
        } else if (id == R.id.nav_people) {
            fragment = new People();
        }

        // ?????????????????? ????????????????, ?????????????? ?????????????? ????????????????
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.con, fragment).commit();
        }
        // ???????????????? ?????????????????? ?????????? ???????? ?? ????????????
        item.setChecked(true);
        // ?????????????? ?????????????????? ?????????? ?? ??????????????????
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
     //   myProject();
        return true;
    }

    public void setItem() {
        try {
            int useItem = 0;
            useItem = getIntent().getExtras().getInt("item");
            Menu menu;
            MenuItem item = null;
            Fragment fragment = null;
            switch (useItem) {
                case 0:
                    menu = navigationView.getMenu();
                    item = (MenuItem) menu.findItem(R.id.nav_profile);
                    fragment = new Profile();
                    break;
                case 1:
                    menu = navigationView.getMenu();
                    item = (MenuItem) menu.findItem(R.id.nav_projects);
                    fragment = new FragmentOne();
                    break;
                case 2:
                    menu = navigationView.getMenu();
                    item = (MenuItem) menu.findItem(R.id.nav_components);
                    fragment = new FragmentTwo();
            }
            // ?????????????????? ????????????????, ?????????????? ?????????????? ????????????????
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.con, fragment).commit();
            }
            // ???????????????? ?????????????????? ?????????? ???????? ?? ????????????
            item.setChecked(true);
            // ?????????????? ?????????????????? ?????????? ?? ??????????????????
            setTitle(item.getTitle());
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e){
            Menu menu;
            MenuItem item = null;
            Fragment fragment = null;
            menu = navigationView.getMenu();
            item = (MenuItem) menu.findItem(R.id.nav_profile);
            fragment = new Profile();
            // ?????????????????? ????????????????, ?????????????? ?????????????? ????????????????
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.con, fragment).commit();
            }
            // ???????????????? ?????????????????? ?????????? ???????? ?? ????????????
            item.setChecked(true);
            // ?????????????? ?????????????????? ?????????? ?? ??????????????????
            setTitle(item.getTitle());
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void login(){
        try {

            if (mSettings.contains("USER_ID") && mSettings.getInt("USER_ID", 0) != 0) {
                USER_ID = mSettings.getInt("USER_ID", 0);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // sPref = getPreferences(MODE_PRIVATE);
       // String ID = sPref.getString("USER_ID", "");
       // System.out.println("USER_ID: " + ID);
       // if (ID.equals("")) {
       //     Intent intent = new Intent(this, LoginActivity.class);
       //     startActivity(intent);
       // }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.create:
                Intent intent = new Intent(Test.this, CreateProject.class);
                intent.putExtra("idUser", USER_ID);
                startActivity(intent);
                break;
       /*     case R.id.clear:
                //database.delete("projects", null, null);
                //database.delete(dbHelper.TABLE_NAME, null, null);
                Intent intent1 = new Intent(Test.this, Test.class);
                startActivity(intent1);
                break;
        */
        }
    }

    @Override
    public void onUserCompleted(User us) {
        user = us;
        user.setRaiting(100);
        System.out.println("USER: " + user.getSecondname() + " " + user.getFirstName());
        View header = navigationView.getHeaderView(0);
        title_name = (TextView)header.findViewById(R.id.title_name);
        title_name.setText(user.getSecondname() + " " + user.getFirstName());
    }

    @Override
    public void onUserError(String error) {

    }
}

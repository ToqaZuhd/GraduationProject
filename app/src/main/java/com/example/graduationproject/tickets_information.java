package com.example.graduationproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class tickets_information extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView score ;
    TextView numberText ;
    ImageView barcode;
    LinearLayout info ;
    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tickets_information);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("معلومات تذاكر الرحلة");

        score = findViewById(R.id.score);
        numberText = findViewById(R.id.number);
        barcode = findViewById(R.id.barcode);
        info = findViewById(R.id.info);

        Intent intent = getIntent();
        String scoreData = intent.getStringExtra("score");
        String TicketNumber = intent.getStringExtra("ticketNumber");

        System.out.println(scoreData);
        score.setText(" "+ scoreData );
        numberText.setText(TicketNumber+"");

//        Random rnd = new Random();
//        int number = rnd.nextInt(99999);
//        numberText.setText(number+"");


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(numberText.getText()+"", BarcodeFormat.CODABAR,235,70);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            barcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }



        nav();

    }

    public void saveOnClick(View view) {
        FileOutputStream fileOutputStream=null;
        File file=getdisc();
        if (!file.exists() && !file.mkdirs())
        {
            Toast.makeText(getApplicationContext(),"sorry can not make dir",Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyymmsshhmmss");
        String date=simpleDateFormat.format(new Date());
        String name="barcode"+date+".jpeg";
        String file_name=file.getAbsolutePath()+"/"+name; File new_file=new File(file_name);
        try {
            fileOutputStream =new FileOutputStream(new_file);
            Bitmap bitmap=viewToBitmap(info,info.getWidth(),info.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(getApplicationContext(),"تم حفظ المعلومات في الصور بنجاح", Toast.LENGTH_LONG).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch
        (FileNotFoundException e) {

        } catch (IOException e) {

        } refreshGallary(file);
    } private void refreshGallary(File file)
    { Intent i=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        i.setData(Uri.fromFile(file)); sendBroadcast(i);
    }
    private File getdisc(){
        File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,"My Image");
    }


    private static Bitmap viewToBitmap(View view, int width, int height)
    {
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap); view.draw(canvas);
        return bitmap;
    }

    public void nav (){
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_Drawer_Open, R.string.navigation_Drawer_Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);


        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(),postNews.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.aboutApp_nav:
                intent=new Intent(tickets_information.this, aboutApp.class);
                startActivity(intent);
                break;
            case R.id.myTrips_nav:
                intent=new Intent(tickets_information.this, MyTrips.class);
                startActivity(intent);
                break;
            case R.id.setting_nav:
                intent=new Intent(tickets_information.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.logOut_nav:
                intent=new Intent(tickets_information.this, LogOut.class);
                startActivity(intent);
                break;



        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    }



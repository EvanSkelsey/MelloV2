package com.example.mellov2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.util.UUID;
import android.content.Intent;
import java.util.concurrent.TimeUnit;
import android.graphics.drawable.ClipDrawable;
import android.widget.EditText;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    //temp buttons
    Button Pulse, Sense;
    Byte test;
    String address = "98:D3:51:FD:86:53";
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //define the upper and lower bounds of our calibrated system
    public static int lowBound = 0;
    public static int upBound = 264;

    //=============================================

    //for notifications
    private static String notification_title = "Bladder Fullness Status";
    private String notification_content;
    private final String CHANNEL_ID = "mello_notifications";
    private final int NOTIFICATION_ID = 001;

    //read in value from device
    private int percentBladderFullness = 43;

    //for the drop animation
    private EditText etPercent;
    private ClipDrawable mImageDrawable;
    private int mLevel = 0;
    private int fromLevel = 0;
    private int toLevel = 0;
    public static final int MAX_LEVEL = 100;  //******to be updated with maximum ADC value
    public static final int LEVEL_DIFF = 20;   //minimum ADC value
    public static final int DELAY = 30;

    //to move progress upwards
    private Handler mUpHandler = new Handler();
    private Runnable animateUpImage = new Runnable(){
        @Override
        public void run(){
            doTheUpAnimation(fromLevel, toLevel);
        }
    };

    //to move progress downwards
    private Handler mDownHandler = new Handler();
    private Runnable animateDownImage = new Runnable(){
        @Override
        public void run(){
            doTheDownAnimation(fromLevel,toLevel);
        }
    };
    //=============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Navigation Bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_current_status);
        }
        navigation.setSelectedItemId(R.id.navigation_current_status);

        //check bluetooth status, if not enabled prompt the user to toggle status
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!myBluetooth.isEnabled()) {
            //prompt user to turn bluetooth on and connect with device
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        //connect to bluetooth device
        try {
            if (btSocket == null) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        } catch (IOException e) {
            msg("Bluetooth connection failed.  Please restart application.");
        }

        //set up status bar
        etPercent = (EditText) findViewById(R.id.etPercent);
        ImageView img = (ImageView) findViewById(R.id.imageView1);
        mImageDrawable = (ClipDrawable) img.getDrawable();
        mImageDrawable.setLevel(0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_current_status:
                    //finish();
                    return true;
                case R.id.navigation_trends_stats:
                    getSupportFragmentManager().beginTransaction().add(R.id.main_container,
                            new TrendsStatsFragment()).commit();
                    return true;
                case R.id.navigation_calibration:
                    getSupportFragmentManager().beginTransaction().add(R.id.main_container,
                            new CalibrationFragment()).commit();
                    return true;
                case R.id.navigation_settings:
                    getSupportFragmentManager().beginTransaction().add(R.id.main_container,
                            new SettingsFragment()).commit();
                    return true;
            }
            return false;
        }
    };


    public void pulseLED(String LEDnum) { //method to pulse a specific LED to observe its effect
        if (btSocket != null) {
            try {
                switch (LEDnum) {
                    case "1A": //pulse LED 1, get reading from sensor 1/A
                        //btSocket.getOutputStream().write("1".getBytes());
                        btSocket.getOutputStream().write(0x11); //00010001
                        break;
                    case "1B": //pulse LED 1, get reading from sensor 2/B
                        btSocket.getOutputStream().write((byte)18); //00010010
                        break;
                    case "1C": //pulse LED 1, get reading from sensor 3/C
                        btSocket.getOutputStream().write((byte)19); //00010011
                        break;
                    case "1D": //pulse LED 1, get reading from sensor 4/D
                        btSocket.getOutputStream().write((byte)20); //00010100
                        break;
                    case "2A": //pulse LED 2, get reading from sensor 1/A
                        btSocket.getOutputStream().write((byte)33); //00100001
                        break;
                    case "2B": //pulse LED 2, get reading from sensor 2/B
                        btSocket.getOutputStream().write((byte)34); //00100010
                        break;
                    case "2C": //pulse LED 2, get reading from sensor 3/C
                        btSocket.getOutputStream().write((byte)35); //00100011
                        break;
                    case "2D": //pulse LED 2, get reading from sensor 4/D
                        btSocket.getOutputStream().write((byte)36); //00100100
                        break;
                    case "3A": //pulse LED 3, get reading from sensor 1/A
                        btSocket.getOutputStream().write((byte)49); //00110001
                        break;
                    case "3B":  //pulse LED 3, get reading from sensor 2/B
                        btSocket.getOutputStream().write((byte)50); //00110010
                        break;
                    case "3C":  //pulse LED 3, get reading from sensor 3/C
                        btSocket.getOutputStream().write((byte)51); //00110011
                        break;
                    case "3D":  //pulse LED 3, get reading from sensor 4/D
                        btSocket.getOutputStream().write((byte)52); //00110100
                        break;
                    case "4A":  //pulse LED 4, get reading from sensor 1/A
                        btSocket.getOutputStream().write((byte)65); //01000001
                        break;
                    case "4B": //pulse LED 4, get reading from sensor 2/B
                        btSocket.getOutputStream().write((byte)66); //01000010
                        break;
                    case "4C": //pulse LED 4, get reading from sensor 3/C
                        btSocket.getOutputStream().write((byte)67); //01000011
                        break;
                    case "4D": //pulse LED 4, get reading from sensor 4/D
                        btSocket.getOutputStream().write((byte)68); //01000100
                        break;
                    case "5A": //pulse LED 5, get reading from sensor 1/A
                        btSocket.getOutputStream().write((byte)81); //01010001
                        break;
                    case "5B": //pulse LED 5, get reading from sensor 2/B
                        btSocket.getOutputStream().write((byte)82); //01010010
                        break;
                    case "5C": //pulse LED 5, get reading from sensor 3/C
                        btSocket.getOutputStream().write((byte)83); //01010011
                        break;
                    case "5D": //pulse LED 5, get reading from sensor 4/D
                        btSocket.getOutputStream().write((byte)84); //01010100
                        break;
                    default:
                        btSocket.getOutputStream().write(0xFF); //00010001
                        break;
                }
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    public String recVolt() {
        byte[] buffer = null;
        buffer = new byte[7];
        try {
            int len = btSocket.getInputStream().read(buffer,0,3);
            String text = new String(buffer, 0, len);
            return text;
        } catch (IOException e) {
            //something happened
            msg("error");
            return "error";
        }
    }

    private void doTheUpAnimation(int fromLevel, int toLevel){
        mLevel += LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);
        if(mLevel<=toLevel){
            mUpHandler.postDelayed(animateUpImage, DELAY);
        }else{
            mUpHandler.removeCallbacks(animateUpImage);
            MainActivity.this.fromLevel = toLevel;
        }
    }

    private void doTheDownAnimation(int fromLevel, int toLevel){
        mLevel -= LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);
        if (mLevel >= toLevel){
            mDownHandler.postDelayed(animateDownImage,DELAY);
        }else{
            mDownHandler.removeCallbacks(animateDownImage);
            MainActivity.this.fromLevel = toLevel;
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

}
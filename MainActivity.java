package com.example.adwall;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button BT_Search;
   // private TextView TV1;
    private BluetoothAdapter mBluetoothAdapter;
    private List<String> bluetoothdeviceslist = new ArrayList<String>();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private String ID_target = "Galaxy A50";
    private String ID_target2 = "Jimmy";





    double dis;
    int a=0,b=0;
    VideoView myVideoView,oldview,nfcview;
    String path,path2,path3;
    static int pos=1; //静态整型变量用于标记播放到了第几段视频
    MediaController mController;
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;



    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
// Android M Permission check
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new
                        String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }
    public void SearchBluetooth() {

        if (mBluetoothAdapter == null) { //沒找到

            Toast.makeText(this, "not find the bluetooth", Toast.LENGTH_SHORT).show();


            finish();

        }

        if (!mBluetoothAdapter.isEnabled()) {

//藍芽未開 跳出視窗提示使用者是否開啟藍芽

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(intent, 1);

            Set<BluetoothDevice> myDevices = mBluetoothAdapter.getBondedDevices();

            if (myDevices.size() > 0) {

                for (BluetoothDevice device : myDevices)

                    bluetoothdeviceslist.add(device.getName() + ":" + device.getAddress() + "\n"); //藍芽連接的裝置資訊

            }

        }

//註冊BroadcastReceiver: 用來接收搜尋到的結果

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        registerReceiver(myreceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(myreceiver, filter);
       // filter.removeUpdates(filter);
    }
    private final BroadcastReceiver myreceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

//收到的廣播類型

            String action = intent.getAction();

//發現設備的廣播

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

//從intent中獲取設備

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                double txPower = -59;

                double ratio = rssi*1.0/txPower;

                if (ratio < 1.0) {

                    dis = Math.pow(ratio,10);

                }

                else {

                    dis = (0.89976)*Math.pow(ratio,7.7095) + 0.111;

                }

                try{

                    if(device.getName().equals(ID_target) ) {
                  //      TV1.setText(device.getName().toString() + " " + Double.toString(dis)); //device.getName().equals(ID_target)
                        myVideoView.setVisibility(View.GONE);
                        oldview.setVisibility(View.VISIBLE);
                        nfcview.setVisibility(View.GONE);
                       // path = "android.resource://" + getPackageName() + "/" + R.raw.old;
                        if (a!=1) {
                            oldview.start();
                        }
                        a=1;
                        b=1;

                    }
                    else if(device.getName().equals(ID_target2)) {
                        //      TV1.setText(device.getName().toString() + " " + Double.toString(dis)); //device.getName().equals(ID_target)
                        myVideoView.setVisibility(View.VISIBLE);
                        oldview.setVisibility(View.GONE);
                        nfcview.setVisibility(View.GONE);
                        // path = "android.resource://" + getPackageName() + "/" + R.raw.old;
                        if (a!=2) {
                            myVideoView.start();
                        }
                        a=2;
                        b=1;
                    }
                   /* else
                    {       myVideoView.setVisibility(View.VISIBLE);
                            oldview.setVisibility(View.GONE);
                            // path = "android.resource://" + getPackageName() + "/" + R.raw.old;
                            // myVideoView.start();


                    }
*/


                }

                catch(Exception e){

                }

            }


        }
    };

    private TimerTask task = new TimerTask(){
        public void run() {
         //   if(a!=1 ) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                checkBluetoothPermission();
                SearchBluetooth();
                if(mBluetoothAdapter.isDiscovering()){
                    mBluetoothAdapter.cancelDiscovery();
                }
                mBluetoothAdapter.startDiscovery();
          //  }
            /*
            if(a==0) {
                path = "android.resource://" + getPackageName() + "/" + R.raw.baby;
            }
            else if (a==2)
            {
                path = "android.resource://" + getPackageName() + "/" + R.raw.baby;

            }
            else{
                //path = "android.resource://" + getPackageName() + "/" + R.raw.old;
                //  myVideoView.stopPlayback();
              //  myVideoView.setVisibility(View.GONE);
               // oldview.setVisibility(View.VISIBLE);
                //  myVideoView.setVideoURI(Uri.parse(path));
                //myVideoView.start();

                a=2;


            }*/


        }
    };

    private  void  initView() {

        myVideoView = (VideoView) findViewById(R.id.videoView);
        oldview=(VideoView) findViewById(R.id.videoView2);
        nfcview=(VideoView) findViewById(R.id.videoView3);

    }
    @Override
    protected void onResume() { //讓Activity啟動時啟動NfcAdapter支持前景模式下處理NFC Intent
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }
    @Override
    protected void onPause() { //暫停時關閉NfcAdapter的前景模式
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) { //辨識卡號
        String cardID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
        Toast.makeText(this, "你是"+cardID,Toast.LENGTH_SHORT).show(); // cardID.equals("組長的卡號")
        myVideoView.setVisibility(View.GONE);
        oldview.setVisibility(View.GONE);
        nfcview.setVisibility(View.VISIBLE);
        nfcview.start();


    }
    private String ByteArrayToHexString(byte[] inarray) { //轉10進位
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];}
        return out;
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();

        // BT_Search = (Button) findViewById(R.id.button);
       // TV1 = (TextView) findViewById(R.id.textView);
        //  view = (VideoView)findViewById(R.id.videoView);
        if(a==0) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.baby;
        }
        else{
            path = "android.resource://" + getPackageName() + "/" + R.raw.old;
        }
        myVideoView.setVideoURI(Uri.parse(path));
        myVideoView.start();

        path2 = "android.resource://" + getPackageName() + "/" + R.raw.old;
        oldview.setVideoURI(Uri.parse(path2));

        path3= "android.resource://" + getPackageName() + "/" + R.raw.nut;
        nfcview.setVideoURI(Uri.parse(path3));

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            //nfc not support your device.
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetoothPermission();
        SearchBluetooth();
        Timer timer01 = new Timer();
        timer01.schedule(task, 0, 3000);

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();




        myVideoView.setOnPreparedListener( new  MediaPlayer.OnPreparedListener() {

            @Override
            public  void  onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer. VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mp.start();
                mp.setLooping( true );


            }
        });
        myVideoView
                .setOnCompletionListener( new  MediaPlayer.OnCompletionListener() {

                    @Override
                    public  void  onCompletion(MediaPlayer mp) {
                        myVideoView.setVideoURI(Uri.parse(path));
                        myVideoView.start();

                    }
                });

        oldview.setOnPreparedListener( new  MediaPlayer.OnPreparedListener() {

            @Override
            public  void  onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer. VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mp.start();
                mp.setLooping( true );

            }
        });
        oldview
                .setOnCompletionListener( new  MediaPlayer.OnCompletionListener() {

                    @Override
                    public  void  onCompletion(MediaPlayer mp) {
                        oldview.setVideoURI(Uri.parse(path));
                        oldview.start();

                    }
                });
        nfcview.setOnPreparedListener( new  MediaPlayer.OnPreparedListener() {

            @Override
            public  void  onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer. VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mp.start();
                mp.setLooping( true );

            }
        });
        nfcview
                .setOnCompletionListener( new  MediaPlayer.OnCompletionListener() {

                    @Override
                    public  void  onCompletion(MediaPlayer mp) {
                        nfcview.setVideoURI(Uri.parse(path3));
                        nfcview.start();

                    }
                });





    }
}

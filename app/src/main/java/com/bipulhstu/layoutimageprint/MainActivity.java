package com.bipulhstu.layoutimageprint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    RelativeLayout idToConvert;
    TextView textView;
    Button convertMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        idToConvert = findViewById(R.id.idToConvert);
        convertMe = (Button) findViewById(R.id.convertMe);
        convertMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                convertLayout();
            }
        });

    }

    private void convertLayout() {
        Bitmap bitmap = getBitmapFromView(idToConvert);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        textView.setText(imageString);

        printIt(imageString);


//        try {
//            File file = new File(this.getExternalCacheDir(), "back.png");
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
////        Canvas canvas = new Canvas(bitmap);
////        canvas.drawColor(Color.WHITE);
////        byte[] imageBytes = byteArrayOutputStream.toByteArray();
////        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
////        textView.setText(imageString);
//
//            byteArrayOutputStream.flush();
//            byteArrayOutputStream.close();
//            file.setReadable(true, false);
//
//
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//            intent.setType("image/png");
//            startActivity(Intent.createChooser(intent, "share by"));
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }


    private void printIt(String thisData) {

        BluetoothSocket socket = null;
        byte[] data = thisData.getBytes();

        //Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Open Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get sunmi InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {
            Toast.makeText(getBaseContext(), "Make Sure Bluetooth have InnterPrinter", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            socket = BluetoothUtil.getSocket(device);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert socket != null;
            BluetoothUtil.sendData(data, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
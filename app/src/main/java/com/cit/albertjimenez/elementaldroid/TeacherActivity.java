package com.cit.albertjimenez.elementaldroid;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cit.albertjimenez.elementaldroid.barcode.BarcodeCaptureActivity;
import com.cit.albertjimenez.elementaldroid.dao.Element;
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerJ;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import es.dmoral.toasty.Toasty;

public class TeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        //Data manager
        DataManagerJ dataManagerFB = DataManagerJ.getInstance();
        dataManagerFB.initFB();
        dataManagerFB.storeNewElement(new Element("P", "1", "d"));
//        ImageView imageView = findViewById(R.id.pluton);
//        Log.d("BASE64", MyBase64ParserKt.encodeImage(((BitmapDrawable)imageView.getDrawable()).getBitmap()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("Barcode");
                    Point[] p = barcode.cornerPoints;
                    Toasty.info(this, barcode.displayValue).show();
                } else Toasty.error(this, "No QR code captured").show();
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }
}

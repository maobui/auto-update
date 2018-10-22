package com.me.bui.autoupdate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import static android.content.Intent.ACTION_INSTALL_PACKAGE;

public class MainActivity extends AppCompatActivity {
    private final String APK_URL = "https://github.com/maobui/new_version/blob/master/apk/sample.apk";

    Button btnInstallApk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        initView();

//        Intent updateIntent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("https://github.com/EmpireCTF/empirectf/blob/master/writeups/2018-06-19-SCTF/files/simple.apk"));
//        startActivity(updateIntent);
    }

    private void initView() {
        btnInstallApk = findViewById(R.id.btnInstallApk);
        btnInstallApk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                installApkFile("sample.apk");
            }
        });
    }

    private void installApkFile(String filename) {
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"  + filename);
        if(!f.exists()) {
            Toast.makeText(this, "No file exists !!!", Toast.LENGTH_SHORT).show();
            return;
        }
//        f.setReadable(true, false);
        Intent intent = new Intent();
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID + ".provider",  f);
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
//            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        } else {
            uri = Uri.fromFile(f);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
        this.finish();
    }
}

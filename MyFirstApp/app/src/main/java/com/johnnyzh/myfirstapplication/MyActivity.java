package com.johnnyzh.myfirstapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

public class MyActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.johnnyzh.myfirstapplication.MESSAGE";


    /**
     * 生物识别代码部分
     */
    private static final String TAG = "gryphon";
    private BiometricPrompt mBiometricPrompt;
    private CancellationSignal mCancellationSignal;
    private BiometricPrompt.AuthenticationCallback mAuthenticationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        mBiometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("指纹验证")
                .setDescription("描述")
                .setNegativeButton("取消", getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Log.i(TAG , "Cancel button clicked");
                    }
                }).build();
        mCancellationSignal = new CancellationSignal();
        mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                //handle cancel result
                finish();
                Log.i(TAG , "Canceled");
            }
        });

        mAuthenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.i(TAG , "onAuthenticationError" + errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Log.i(TAG , "onAuthenticationSucceeded" + result.toString());
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.i(TAG , "onAuthenticationFailed");
            }
        };

        mBiometricPrompt.authenticate(mCancellationSignal , getMainExecutor() , mAuthenticationCallback);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE , message);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //处理动作按钮的点击事件
        switch (item.getItemId()){
            case R.id.app_bar_search:
                //openSearch();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                    return super.onOptionsItemSelected(item);
        }
    }
}

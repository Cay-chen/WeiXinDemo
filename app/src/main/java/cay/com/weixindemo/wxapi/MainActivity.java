package cay.com.weixindemo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cay.com.weixindemo.R;

/**
 * Created by Cay on 2016/8/18.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dengluchengg);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String adn =bundle.getString("USER_MES");
        Log.i("TAG", "adn: "+adn);

    }
}

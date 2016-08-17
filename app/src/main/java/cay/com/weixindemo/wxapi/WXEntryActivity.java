package cay.com.weixindemo.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cay.com.weixindemo.R;

/**
 * Created by C on 2016/8/17.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String APP_ID = "wx9d9f1e3deacd44d2";
    private IWXAPI mWeiXinAPI;
    private Button loginButton;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.login_btn);
        shareButton = (Button) findViewById(R.id.share_btn);
        mWeiXinAPI = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mWeiXinAPI.registerApp(APP_ID);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weixinLogin();
            }
        });

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
    /**
     * 微信登录判定
     */
    private void weixinLogin() {
        if (mWeiXinAPI == null) {
            mWeiXinAPI = WXAPIFactory.createWXAPI(this, APP_ID, false);
        }

        if (!mWeiXinAPI.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(this,"请安装微信后在登录！",Toast.LENGTH_LONG).show();
            return;
        }

        mWeiXinAPI.registerApp(APP_ID);

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        mWeiXinAPI.sendReq(req);

    }
}

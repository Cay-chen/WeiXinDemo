package cay.com.weixindemo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cay.com.weixindemo.R;
import cay.com.weixindemo.wxapi.Util.BaseUiListener;
import cay.com.weixindemo.wxapi.Util.Util;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by C on 2016/8/17.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler ,View.OnClickListener{
    private static final String APP_ID = "wxab940fc44ffda729";
    private static final String QQ_APP_ID = "222222";
    private QQAuth mQqAuth;
    private Tencent mTencent;
    private IWXAPI mWeiXinAPI;
    private Button loginButton;
    private Button webshareWeiXinButton;
    private Button webshareFriendButton;
    private Button textshareWeiXinButton;
    private Button textshareFriendButton;
    private Button voidshareWeiXinButton;
    private Button voidshareFriendButton;
    private Button yinyueshareWeiXinButton;
    private Button yinyueshareFriendButton;
    private Button imageshareWeiXinButton;
    private Button imageshareFriendButton;
    private Button qqButtonLogin;
    private Button qqshareButton;
    private Button kongjianShareButton;

    private IUiListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        initView();

        // 微信
        mWeiXinAPI = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mWeiXinAPI.registerApp(APP_ID);

        // QQ
        mQqAuth = QQAuth.createInstance(QQ_APP_ID, this.getApplicationContext());
        mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());

        mWeiXinAPI.handleIntent(getIntent(), this);
        loginButton.setOnClickListener(this);
        webshareFriendButton.setOnClickListener(this);
        webshareWeiXinButton.setOnClickListener(this);
        textshareWeiXinButton.setOnClickListener(this);
        textshareFriendButton.setOnClickListener(this);
        voidshareWeiXinButton.setOnClickListener(this);
        voidshareFriendButton.setOnClickListener(this);
        yinyueshareWeiXinButton.setOnClickListener(this);
        yinyueshareFriendButton.setOnClickListener(this);
        imageshareWeiXinButton.setOnClickListener(this);
        imageshareFriendButton.setOnClickListener(this);
        qqButtonLogin.setOnClickListener(this);
        qqshareButton.setOnClickListener(this);
        kongjianShareButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void helloEve(String message) {
        if (message.equals("finsh")) {
            finish();
        }
    }
    private void initView() {
        loginButton = (Button) findViewById(R.id.login_btn);
        webshareFriendButton = (Button) findViewById(R.id.share_friend_btn);
        webshareWeiXinButton = (Button) findViewById(R.id.share_weixin_btn);
        textshareWeiXinButton = (Button) findViewById(R.id.text_share_weixin_btn);
        textshareFriendButton = (Button) findViewById(R.id.text_share_friend_btn);
        voidshareWeiXinButton = (Button) findViewById(R.id.void_share_weixin_btn);
        voidshareFriendButton = (Button) findViewById(R.id.void_share_friend_btn);
        yinyueshareWeiXinButton = (Button) findViewById(R.id.yinyue_share_weixin_btn);
        yinyueshareFriendButton = (Button) findViewById(R.id.yinyue_share_friend_btn);
        imageshareWeiXinButton = (Button) findViewById(R.id.image_share_weixin_btn);
        imageshareFriendButton = (Button) findViewById(R.id.image_share_friend_btn);
        qqButtonLogin = (Button) findViewById(R.id.btn_qqlogin);
        kongjianShareButton = (Button) findViewById(R.id.btn_kongjianshare);
        qqshareButton = (Button) findViewById(R.id.btn_qqshare);

    }
    /**
     * 分享文字TEXT
     * @param flag 0 代表分享到微信好友  1 代表分享到微信朋友圈
     */
    private void weTextShare(int flag) {
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = "分享的文字";

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
         msg.title = "Will be ignored";
        msg.description = "分享的文字";

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene =  flag==0? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

        // 调用api接口发送数据到微信
        mWeiXinAPI.sendReq(req);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }
    /**
     * 分享图片 IMage  Bitmap
     * @param flag 0 代表分享到微信好友  1 代表分享到微信朋友圈
     */
    private void weiImageShare(int flag) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);//图片大小 150 x 150
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mWeiXinAPI.sendReq(req);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("TAG", "getType() : "+baseResp.getType() );
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            Toast.makeText(WXEntryActivity.this,"分享成功",Toast.LENGTH_LONG).show();
        }
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    String code = ((SendAuth.Resp) baseResp).code;
                    Log.i("TAG", "code: " + code);

                    if (code != null) {
                        String urll = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxab940fc44ffda729&secret=19a13a1fe8c4aa79d95d1e18f66c9ca5&code=" + code + "&grant_type=authorization_code";
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        Request mRequest = new Request.Builder()
                                .url(urll)
                                .build();
                        Call mCall = mOkHttpClient.newCall(mRequest);
                        mCall.enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                String mmm = response.body().string();
                                Log.i("TAG", "mmm: "+mmm);

                                try {
                                    JSONObject mmmJsonObject = new JSONObject(mmm);
                                    String USER = "https://api.weixin.qq.com/sns/userinfo?access_token=" + mmmJsonObject.getString("access_token") + "&openid=" + mmmJsonObject.getString("openid");

                                    OkHttpClient userMesClient = new OkHttpClient();
                                    Request userRequest = new Request.Builder()
                                            .url(USER)
                                            .build();
                                    Call userCall = userMesClient.newCall(userRequest);
                                    userCall.enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Request request, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Response response) throws IOException {
                                            String mmmUSER = response.body().string();
                                            Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                            intent.putExtra("USER_MES", mmmUSER);
                                            EventBus.getDefault().post("finsh");
                                            startActivity(intent);
                                            Log.i("TAG", "mmmUSER: "+mmmUSER);
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.i("TAG", "mmm: "+mmm);
                            }
                        });

                    }
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:

                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    break;
            }
        }

    }



    private void weImageUrlShare(int flag) {
        String url = "http://weixin.qq.com/zh_CN/htmledition/images/weixin/weixin_logo0d1938.png";

        try{
            WXImageObject imgObj = new WXImageObject();
           // imgObj.imageUrl = url;

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;

            Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
            bmp.recycle();
            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = flag==0 ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            mWeiXinAPI.sendReq(req);

            finish();
        } catch(Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 分享webpage
     * @param flag 1 代表分享到微信好友  0 代表分享到微信朋友圈
     */
    private void wechatShare(int flag){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "www.baidu.com";  //分享的链接
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "这里填写标题";
        msg.description = "这里填写内容";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
        mWeiXinAPI.sendReq(req);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                weixinLogin();
                break;
            case R.id.share_friend_btn:
                wechatShare(0);
                break;
            case R.id.share_weixin_btn:
                wechatShare(1);
                break;
            case R.id.text_share_weixin_btn:
                weTextShare(1);
                break;
            case R.id.text_share_friend_btn:
                weTextShare(0);
                break;
            case R.id.image_share_friend_btn:
                weiImageShare(0);
                break;
            case R.id.image_share_weixin_btn:
                weiImageShare(1);
                break;
            case R.id.btn_qqlogin:
                Log.i("TAGAA", "登录");

                onClickLogin();
                break;
            case R.id.btn_qqshare:
                Log.i("TAGAA", "QQ分享");

                share(qqshareButton);
                break;
            case R.id.btn_kongjianshare:
                Log.i("TAGAA", "空间分享");
                //share(kongjianShareButton);
                shareToQzone();
                break;
        }
    }


    /**
     * QQ登录
     */
    public void onClickLogin() {
        // 登录
        if (!mQqAuth.isSessionValid()) {
            // 实例化回调接口
             listener = new BaseUiListener() {
                @Override
                protected void doComplete(Object values) {

                    Log.i("TAGAA", "成功 "+values);

                    // updateUserInfo();
                    // updateLoginButton();
                    if (mQqAuth != null) {
                       // mNewLoginButton.setTextColor(Color.BLUE);
                      //  mNewLoginButton.setText("登录");
                    }
                }
            };
            // "all": 所有权限，listener: 回调的实例
          //  mQqAuth.login(this, "all", listener);

            // 这版本登录是使用的这种方式，后面的几个参数是啥意思 我也没查到
           mTencent.loginWithOEM(this, "all", listener, "10000144",
                  "10000144", "xxxx");
        } else {
            // 注销登录
            mQqAuth.logout(this);
         //   updateUserInfo();

            // updateLoginButton();
        //    mNewLoginButton.setTextColor(Color.RED);
          //  mNewLoginButton.setText("退出帐号");
        }
    }


    public void share(View view)
    {

        Bundle bundle = new Bundle();
//这条分享消息被好友点击后的跳转URL。

        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://connect.qq.com/");
//分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, "我在测试");
//分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
//分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, "测试");
//手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, "??我在测试");
//标识该消息的来源应用，值为应用名称+AppId。
   //     bundle.putString(Constants.PARAM_APP_SOURCE, "星期几" + AppId);

        switch (view.getId()) {
            case R.id.btn_qqshare:
                mTencent.shareToQQ(this, bundle , new BaseUiListener());
                break;
            case R.id.btn_kongjianshare:
                mTencent.shareToQzone(this, bundle, new BaseUiListener());
                break;
        }

    }

    private void shareToQzone () {


        ArrayList<String> mlist = new ArrayList<String>();
        mlist.add("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
        mlist.add("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
        mlist.add("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
        mlist.add("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");

        Bundle params = new Bundle();

        //分享类型
       params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "www.baidu.com");//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, mlist);
        mTencent.shareToQzone(this, params, new BaseUiListener());
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,listener);
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }

}

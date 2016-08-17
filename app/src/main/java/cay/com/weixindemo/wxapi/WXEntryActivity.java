package cay.com.weixindemo.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.net.URL;

import cay.com.weixindemo.R;
import cay.com.weixindemo.wxapi.Util.Util;

/**
 * Created by C on 2016/8/17.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler ,View.OnClickListener{
    private static final String APP_ID = "wx9d9f1e3deacd44d2";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mWeiXinAPI = WXAPIFactory.createWXAPI(this, APP_ID, true);
        mWeiXinAPI.registerApp(APP_ID);
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
        }
    }

}

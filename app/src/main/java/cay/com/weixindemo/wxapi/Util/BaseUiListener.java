package cay.com.weixindemo.wxapi.Util;

import android.util.Log;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by Cay on 2016/8/19.
 */
public class BaseUiListener implements IUiListener {
    /**
     * 成功
     * @param o
     */

    @Override
    public void onComplete(Object o) {
        doComplete(o);
        Log.i("TAGAA", "成功 "+o);

    }
    /**
     * 处理返回的消息 比如把json转换为对象什么的
     *
     * @param values
     */
    protected void doComplete(Object values) {

    }

    @Override
    public void onError(UiError uiError) {
        Log.i("TAGAA", "onError:错误 ");

    }

    @Override
    public void onCancel() {
        Log.i("TAGAA", "onCancel:取消 ");
    }
}

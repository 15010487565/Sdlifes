package www.xcd.com.mylibrary.http;

import java.util.Map;

/**
 * Created by Leo on 2017/2/20.
 * 请求回调接口
 */
public interface HttpInterface {
    /**
     * 成功
     * status 状态
     * msg 提示
     * result 返回数据
     */
    void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps);

    /**
     * 错误
     */
    void onErrorResult(int requestCode, String returnMsg);
    
}

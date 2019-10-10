package ning.zhou.rpc.framework;

import java.io.Serializable;

/**
 * @author 周宁
 * @Date 2019-10-10 18:05
 */
public class ResultRefer implements Serializable {

    private static final ResultRefer NO_RESULT = new ResultRefer();

    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public static ResultRefer getNoResult() {
        return NO_RESULT;
    }
}

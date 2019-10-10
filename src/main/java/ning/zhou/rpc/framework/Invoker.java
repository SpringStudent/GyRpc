package ning.zhou.rpc.framework;

import java.io.Serializable;

/**
 * 方法调用
 *
 * @author 周宁
 * @Date 2019-10-10 15:44
 */
public class Invoker implements Serializable {
    /**
     *
     * 调用方法的参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 调用方法的参数数组对象
     */
    private Object[] arguments;
    /**
     * 调用方法的名称
     */
    private String methodName;
    /**
     * 类
     */
    private Class clss;

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class getClss() {
        return clss;
    }

    public void setClss(Class clss) {
        this.clss = clss;
    }
}

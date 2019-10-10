package ning.zhou.rpc.framework;


import java.util.HashMap;
import java.util.Map;

/**
 * @author 周宁
 * @Date 2019-10-10 16:31
 */
public class Provider {

    public static void main(String[] args) {
        Export export = new Export();
        Map<Class,Object> serv = new HashMap<>();
        serv.put(HelloService.class,new HelloServiceImpl());
        export.setRegistried(serv);
        export.export(1234);
    }
}

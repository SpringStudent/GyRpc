package ning.zhou.rpc.framework;


/**
 * @author 周宁
 * @Date 2019-10-10 16:31
 */
public class Provider {

    public static void main(String[] args) {
        Export export = new Export();
        export.regist(HelloService.class, new HelloServiceImpl());
        export.export(1234);
    }
}

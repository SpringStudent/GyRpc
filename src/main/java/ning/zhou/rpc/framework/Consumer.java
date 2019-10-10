package ning.zhou.rpc.framework;


/**
 * @author 周宁
 * @Date 2019-10-10 17:04
 */
public class Consumer {

    public static void main(String[] args) throws InterruptedException {
        HelloService helloService = Refer.refer(HelloService.class,"127.0.0.1",1234);
        for(int i = 0;i<Integer.MAX_VALUE;i++){
            String hello = helloService.hello("World" + i);
            System.out.println(hello);
            Thread.sleep(1000);
        }

    }
}

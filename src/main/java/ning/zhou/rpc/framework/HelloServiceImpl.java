package ning.zhou.rpc.framework;

/**
 * @author 周宁
 * @Date 2019-10-09 13:48
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello" + name;
    }
}

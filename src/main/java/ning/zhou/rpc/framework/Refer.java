package ning.zhou.rpc.framework;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

/**
 * @author 周宁
 * @Date 2019-10-10 15:49
 */
public class Refer {

    public static <T> T refer(Class<T> clss, final String host, final int port) {
        return (T) Proxy.newProxyInstance(clss.getClassLoader(), new Class<?>[]{clss}, (proxy, method, args) -> {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ResultRefer resultRefer = new ResultRefer();
            EventLoopGroup group = null;
            try {
                group = new NioEventLoopGroup();
                Bootstrap b = new Bootstrap();
                b.group(group).channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel channel) throws Exception {
                                channel.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers
                                        .cacheDisabled(this.getClass().getClassLoader())));
                                channel.pipeline().addLast(new ObjectEncoder());
                                channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        Invoker invoker = new Invoker();
                                        invoker.setArguments(args);
                                        invoker.setMethodName(method.getName());
                                        invoker.setParameterTypes(method.getParameterTypes());
                                        invoker.setClss(clss);
                                        ctx.writeAndFlush(invoker);
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        if(msg!=ResultRefer.getNoResult()){
                                            resultRefer.setResult(msg);
                                        }
                                        countDownLatch.countDown();
                                    }
                                });
                            }
                        });
                b.connect(host, port);
                countDownLatch.await();
                return resultRefer.getResult();
            } finally {
                group.shutdownGracefully();
            }
        });
    }

}

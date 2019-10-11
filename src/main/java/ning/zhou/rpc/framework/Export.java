package ning.zhou.rpc.framework;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 周宁
 * @Date 2019-10-10 15:48
 */
public class Export {
    /**
     * 已注册服务
     */
    private Map<Class, Object> registried = new HashMap<>();

    public void regist(Class clss,Object obj){
        registried.put(clss,obj);
    }

    public void export(int port) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            serverBootstrap.group(bossGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new ObjectDecoder(1024, ClassResolvers
                                    .cacheDisabled(this.getClass().getClassLoader())));
                            channel.pipeline().addLast(new ObjectEncoder());
                            channel.pipeline().addLast(new SimpleChannelInboundHandler<Invoker>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, Invoker invoker) throws Exception {
                                    Object obj = registried.get(invoker.getClss());
                                    Class<?>[] parameterTypes = invoker.getParameterTypes();
                                    Object[] arguments = invoker.getArguments();
                                    String methodName = invoker.getMethodName();
                                    Method method = obj.getClass().getMethod(methodName, parameterTypes);
                                    Object result = method.invoke(obj, arguments);
                                    if(result==null){
                                        result = ResultRefer.getNoResult();
                                    }
                                    channelHandlerContext.writeAndFlush(result);
                                }
                            });
                        }
                    });
            ChannelFuture f = serverBootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }
}

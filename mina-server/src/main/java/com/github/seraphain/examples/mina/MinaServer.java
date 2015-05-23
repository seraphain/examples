package com.github.seraphain.examples.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MINA Server。
 * 
 * @author
 * 
 */
public class MinaServer {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(MinaServer.class);

    /** Server port */
    private static final int SERVER_PORT = 9876;

    /** IoProcessor number */
    private static final int IO_PROCESSOR_NUM = 2;

    /** Thread pool size */
    private static final int THREAD_POOL_SIZE = 5;

    /** Idel time in seconds */
    private static final int IDLE_TIME = 60;

    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String[] args) {

        // IoProcessor number
        NioSocketAcceptor acceptor = new NioSocketAcceptor(IO_PROCESSOR_NUM);

        // Filter chain
        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

        // Loggin filter
        chain.addLast("logger", new LoggingFilter());

        // Protocol codec filter
        chain.addLast("textLineCodec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        // Executor filter
        chain.addLast("threadPool", new ExecutorFilter(Executors.newFixedThreadPool(THREAD_POOL_SIZE)));

        // Idel time
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDLE_TIME);

        // Handler
        acceptor.setHandler(new IoHandler());

        // Bind to port
        try {
            acceptor.bind(new InetSocketAddress(SERVER_PORT));
            logger.info("MINA server started. Port: " + SERVER_PORT);
        } catch (IOException e) {
            logger.info("MINA start failed.", e);
        }

    }

}

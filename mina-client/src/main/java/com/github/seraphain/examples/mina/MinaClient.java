package com.github.seraphain.examples.mina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MINA Client.
 * 
 * @author
 * 
 */
public class MinaClient {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(MinaClient.class);

    /** Quit command */
    private static final String QUIT = "quit";

    /** Server address */
    private static final String SERVER_ADDRESS = "localhost";

    /** Server port */
    private static final int SERVER_PORT = 9876;

    /** Connect timeout in milliseconds */
    private static final long CONNECT_TIMEOUT_MILLIS = 60000;

    /** Idel time in seconds */
    private static final int IDLE_TIME = 60;

    /**
     * @param args
     */
    public static void main(String[] args) {
        SocketConnector socketConnector = new NioSocketConnector();
        socketConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        socketConnector.setConnectTimeoutMillis(CONNECT_TIMEOUT_MILLIS);
        socketConnector.setHandler(new IoHandler());
        ConnectFuture connectFuture = socketConnector.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
        connectFuture.awaitUninterruptibly();
        IoSession ioSession = null;
        try {
            ioSession = connectFuture.getSession();
            if (ioSession != null) {
                ioSession.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDLE_TIME);
                logger.info("Server connected. Address: " + SERVER_ADDRESS + ":" + SERVER_PORT);
                logger.info("Input any text and enter to send message to server. Enter 'quit' and enter to exit.");
                while (true) {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String message = br.readLine();
                        if (message != null && message.trim().length() != 0) {
                            if (message.trim().equalsIgnoreCase(QUIT)) {
                                break;
                            }
                            if (ioSession.isConnected()) {
                                ioSession.write(message.trim());
                            } else {
                                logger.error("IoSession is closed，Client will shutdown。");
                                break;
                            }
                        }
                    } catch (IOException e) {
                        logger.error("Process message failed。", e);
                    }
                }
            } else {
                logger.error("Connect to server failed. Address: " + SERVER_ADDRESS + ":" + SERVER_PORT);
                return;
            }
        } catch (RuntimeIoException e) {
            logger.error("Connect to server failed. Address: " + SERVER_ADDRESS + ":" + SERVER_PORT, e);
            return;
        } finally {
            if (ioSession != null) {
                try {
                    ioSession.close(true);
                } catch (RuntimeIoException e) {
                    logger.error("Close server connection failed.", e);
                }
            }
        }
        socketConnector.dispose();
    }

}

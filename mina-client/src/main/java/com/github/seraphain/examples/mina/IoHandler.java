package com.github.seraphain.examples.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client IO handler.
 * 
 * @author
 */
public class IoHandler extends IoHandlerAdapter {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(IoHandlerAdapter.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.info("IoSession created. Server address: " + session.getRemoteAddress());
        session.setAttribute("remote_address", session.getRemoteAddress().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("IoSession closed，Server address: " + session.getAttribute("remote_address"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.info("IoSession idle(status: " + status + "). Disconnecting. Server address: "
                + session.getAttribute("remote_address"));
        session.close(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.info("Exception caught. Disconnecting. Server address: " + session.getAttribute("remote_address"), cause);
        session.close(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        logger.info("Message received. Server address: " + session.getAttribute("remote_address") + ". Message: "
                + message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("Message sent. Server address: " + session.getAttribute("remote_address") + ". Message: " + message);
    }

}

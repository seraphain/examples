package com.github.seraphain.examples.httptunnel;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring Bean Servlet proxy.
 * 
 * @author
 */
@SuppressWarnings("serial")
public class SpringBeanServletProxy extends HttpServlet {

    /** Bean name of the target Servlet in Spring container */
    private String targetBeanName;

    /** Target Servlet */
    private Servlet targetBean;

    /**
     * Constructor.
     */
    public SpringBeanServletProxy() {
        super();
    }

    /**
     * Initialize Servlet.
     * 
     * @throws ServletException
     *             if an exception occurs that interrupts the servlet's normal
     *             operation
     */
    @Override
    public void init() throws ServletException {
        this.targetBeanName = getInitParameter("targetBean");
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this
                .getServletContext());
        this.targetBean = (Servlet) wac.getBean(targetBeanName);
        this.targetBean.init(this.getServletConfig());
    }

    /**
     * Handle request, dispatch request to targetBean.
     * 
     * @param req
     *            the ServletRequest object that contains the client's request
     * @param res
     *            the ServletResponse object that contains the servlet's
     *            response
     * @throws ServletException
     *             if an exception occurs that interferes with the servlet's
     *             normal operation
     * @throws IOException
     *             if an input or output exception occurs
     */
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        this.targetBean.service(req, res);
    }

}

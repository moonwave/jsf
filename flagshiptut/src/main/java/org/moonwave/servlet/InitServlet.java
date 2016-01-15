package org.moonwave.servlet;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.moonwave.util.AppProperties;
import org.moonwave.util.MailProperties;
import org.moonwave.util.MimeProperties;
import org.moonwave.util.StackTrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class InitServlet
 * 
 */
@WebServlet("/InitServlet")
public class InitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(InitServlet.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitServlet() {
        super();
    }

    @Override
    public void init() {

        try {
            Properties properties = new Properties();
            try {
                // stackoverflow.com/questions/9963373/read-properties-file-in-static-code-of-a-jsf-web-application
                // InputStream input = EmailController.class.getResourceAsStream("/deploy.properties");
                InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("mail.properties");
//                InputStream stream = getClass().getClassLoader().getResourceAsStream("mail.properties");
                properties.load(input);

                MailProperties mailProperties = MailProperties.getInstance();
                mailProperties.setFrom(properties.getProperty("from"));
                mailProperties.setTo(properties.getProperty("to"));
                mailProperties.setReplyTo(properties.getProperty("replyTo"));
                mailProperties.setCc(properties.getProperty("cc"));
                mailProperties.setBcc(properties.getProperty("bcc"));
                mailProperties.setSubjectPrefix(properties.getProperty("subjectPrefix"));
                mailProperties.setSubjectSuffix(properties.getProperty("subjectSuffix"));
                mailProperties.setBodyPrefix(properties.getProperty("bodyPrefix"));
                mailProperties.setBoySuffix(properties.getProperty("boySuffix"));
                mailProperties.setPostscript(properties.getProperty("postscript"));
            } catch (Exception e) {
                System.out.println(e);
            }

            LOG.info("MailProperties initialization succeeded");
        } catch (Throwable e) {
            LOG.error("MailProperties initialization error: " + e);
        }

        try {
            Properties properties = AppProperties.getInstance().getProperties();
            try {
                InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
                properties.load(input);

            } catch (Exception e) {
                System.out.println(e);
            }

            LOG.info("AppProperties initialization succeeded");
        } catch (Throwable e) {
            LOG.error("AppProperties initialization error: " + StackTrace.toString(e));
        }

        try {
            Properties properties = MimeProperties.getInstance().getProperties();
            try {
                InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("mime-types.properties");
                properties.load(input);

            } catch (Exception e) {
                System.out.println(e);
            }

            LOG.info("MimeProperties initialization succeeded");
        } catch (Throwable e) {
            LOG.error("MimeProperties initialization error: " + StackTrace.toString(e));
        }
    }

    @Override
    public void destroy() {
    }
}

package com.sln.webdisk.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.sln.webdisk.data.UserFileDB;

/**
 * Application Lifecycle Listener implementation class WebdiskContextListener
 * This will initialize one time per life of a container
 * The attribute {@code currentYear} is set here
 * I have also tried to set global var of MySQL <strong>max_allowed_packet</strong> here but it
 * doesn't work - either because MySQL connection is not yet available at this point OR because
 * the connection may be different each call
 *
 * @author  Sln
 * @version 1.0
 */
@WebListener
public class WebdiskContextListener implements ServletContextListener {

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
	@Override
    public void contextInitialized(ServletContextEvent sce)  { 
        ServletContext sc = sce.getServletContext();

        // initialize the current year that's used in the copyright notice
        GregorianCalendar currentDate = new GregorianCalendar();
        int currentYear = currentDate.get(Calendar.YEAR);
        sc.setAttribute("currentYear", currentYear);
        
        // Doesn't work - probably no MySQL connection yet at this point. Will set before each upload
        // Set global var of MySQL max_allowed_packet to 16MB so we could upload files this large
        //final long MAX_UPLOAD_PACKET = 16 * 1024 * 1024;  
        //UserFileDB.setMaxPacket(MAX_UPLOAD_PACKET);

    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce)  { 
    	// no cleanup necessary
    }
	
}

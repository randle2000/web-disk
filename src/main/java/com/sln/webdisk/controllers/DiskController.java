package com.sln.webdisk.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.sln.webdisk.business.User;
import com.sln.webdisk.business.UserFile;
import com.sln.webdisk.data.UserDB;
import com.sln.webdisk.data.UserFileDB;

/**
 * This servlet will process calls to:
 * <p>	disk/controller/downloadFile
 * <p>	disk/controller/uploadFile
 * <p>	disk/controller/deleteFile
 *
 * @author  Sln
 * @version 1.0
 */
public class DiskController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String defaultURL = "/disk/index.jsp";


	@Override
	public void init() throws ServletException {
		
		super.init();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String url = defaultURL;
        if (requestURI.endsWith("/downloadFile")) {
        	url = downloadFile(request, response);
        } else
        	url = showDisk(request, response);
        getServletContext()
            .getRequestDispatcher(url)
            .forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String url = defaultURL;
        if (requestURI.endsWith("/uploadFile")) {
        	url = uploadFile(request, response);
        } else if (requestURI.endsWith("/deleteFile")) {
        	url = deleteFile(request, response);
        }
        getServletContext()
        	.getRequestDispatcher(url)
        	.forward(request, response);
	}

	private String showDisk(HttpServletRequest request, HttpServletResponse response) {
		String email = request.getUserPrincipal().getName();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
		if (user == null || !user.getEmail().equalsIgnoreCase(email)) {
			user = UserDB.selectUser(email);
			// user should never be null actually because this servlet is inside security realm
			session.setAttribute("user", user);
		}
		//populate file list
		List<UserFile> userFiles = UserFileDB.selectUserFiles(user.getUserId());
		session.setAttribute("userFiles", userFiles);
		return defaultURL;
	}
	
	private String downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get upload id from URL's parameters
        long fileId = Long.parseLong(request.getParameter("fileId"));
        UserFile userFile = UserFileDB.retrieveFileInfo(fileId);
        if (userFile == null) {
    		request.setAttribute("message", "Download failed!");
    		return defaultURL;
        }
        
        // gets file name and file blob data
        String fileName = userFile.getFileName();
        int fileLength = userFile.getFileSize();

        ServletContext context = getServletContext();

        // sets MIME type for the file download
        String mimeType = context.getMimeType(fileName);
        if (mimeType == null) {        
            mimeType = "application/octet-stream";
        }              
         
        // set content properties and header attributes for the response
        response.setContentType(mimeType);
        response.setContentLength(fileLength);
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);

        // writes the file to the client
        OutputStream outStream = response.getOutputStream();
        boolean downloadSuccess = UserFileDB.downloadFile(fileId, outStream);
        outStream.close();
        if (downloadSuccess) {
        	request.setAttribute("message", "File " + fileName + " was sent to your computer");
        } else {
        	request.setAttribute("message", "Download failed!");
        }
		return defaultURL;
	}

	private String uploadFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream inputStream = null; // input stream of the upload file
        UserFile userFile = new UserFile();
         
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("file");
        if (filePart != null) {
        	userFile.setUserId(user.getUserId());
        	userFile.setFileName(filePart.getSubmittedFileName());
        	userFile.setFileSize((int)filePart.getSize());
            //System.out.println(filePart.getContentType());

        	// obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }
        boolean uploadSuccess = false;
        if (userFile.getFileSize() > 0) 
        	uploadSuccess = UserFileDB.insert(userFile, inputStream);

        if (uploadSuccess) 
        	request.setAttribute("message", "File " + userFile.getFileName() + " uploaded successfully");
        else 
        	request.setAttribute("message", "File upload failed!");
		return showDisk(request, response);
	}

	private String deleteFile(HttpServletRequest request, HttpServletResponse response) {
		boolean uploadSuccess = UserFileDB.delete(Long.parseLong(request.getParameter("fileId")));
        if (uploadSuccess) {
        	request.setAttribute("message", "File " + request.getParameter("fileName") + " was deleted successfully");
        } else {
        	request.setAttribute("message", "Deleting file failed!");
        }
		return showDisk(request, response);
	}
	
}

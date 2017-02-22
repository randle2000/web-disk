package com.sln.webdisk.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sln.webdisk.business.UserFile;

/**
* This class contains static methods for work with database
* Works with table <strong>files</strong>
* It uses static ConnectionPool to get DataSource
*  
* @author  Sln
* @version 1.0
* @see ConnectionPool 
* @see UserFile
*/
public class UserFileDB {
	// also see <multipart-config> in web.xml
	final static long MAX_UPLOAD_PACKET = 16 * 1024 * 1024;  // 16MB
	
	public static boolean insert(UserFile userFile, InputStream inputStream) {
		if (inputStream == null) 
			return false;

		String query1 = "SET GLOBAL max_allowed_packet = ?";
        PreparedStatementSetter pss1 = (ps) -> ps.setLong(1, MAX_UPLOAD_PACKET);
		
        String query2 = "INSERT INTO files (UserID, FileName, FileSize, UploadDate, File) VALUES (?, ?, ?, NOW(), ?)";
        PreparedStatementSetter pss2 = (ps) -> { 
            ps.setLong(1, userFile.getUserId()); 
            ps.setString(2, userFile.getFileName()); 
            ps.setInt(3, userFile.getFileSize());
            ps.setBlob(4, inputStream);
            //ps.setBinaryStream(4, inputStream, (int)inputStream.available());
        };
        
        try (Connection connection = ConnectionPool.getInstance().getConnection();
        		PreparedStatement ps1 = DBUtil.createPreparedStatement(connection, query1, pss1);
        		PreparedStatement ps2 = DBUtil.createPreparedStatement(connection, query2, pss2)) {
    		connection.setAutoCommit(false);

        	//ps1.execute();	// set max_allowed_packet
            int row = ps2.executeUpdate();

            connection.commit();

            return (row > 0) ? true : false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
	}
	
	public static boolean delete(long fileId) {
        String query = "DELETE FROM files WHERE FileID = ?";
        PreparedStatementSetter pss = (ps) -> ps.setLong(1, fileId); 
        
        try (Connection connection = ConnectionPool.getInstance().getConnection();
        		PreparedStatement ps = DBUtil.createPreparedStatement(connection, query, pss)) {
            int row = ps.executeUpdate();
            return (row > 0) ? true : false;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
	}
	
	public static List<UserFile> selectUserFiles(long userId) {
		List<UserFile> list = new ArrayList<>();
        String query = "SELECT * FROM files WHERE UserID = ?";
        PreparedStatementSetter pss = (ps) -> ps.setLong(1, userId); 
        try (Connection connection = ConnectionPool.getInstance().getConnection();
        		PreparedStatement ps = DBUtil.createPreparedStatement(connection, query, pss);
        		ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UserFile userFile = new UserFile();
                userFile.setFileId(rs.getLong("FileID"));
                userFile.setUserId(rs.getLong("UserID"));
                userFile.setFileName(rs.getString("FileName"));
                userFile.setFileSize(rs.getInt("FileSize"));
                userFile.setUploadDate(rs.getDate("UploadDate"));
                list.add(userFile);
            }
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } 
		return list;
	}
	
	public static UserFile retrieveFileInfo(long fileId) {
		UserFile userFile = new UserFile();
        String query = "SELECT * FROM files WHERE FileID = ?";
        PreparedStatementSetter pss = (ps) -> ps.setLong(1, fileId); 
        try (Connection connection = ConnectionPool.getInstance().getConnection();
        		PreparedStatement ps = DBUtil.createPreparedStatement(connection, query, pss);
        		ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                userFile.setFileId(rs.getLong("FileID"));
                userFile.setUserId(rs.getLong("UserID"));
                userFile.setFileName(rs.getString("FileName"));
                //userFile.setFileSize(rs.getInt("FileSize"));
                userFile.setUploadDate(rs.getDate("UploadDate"));
            }

            // return actual file size, not the one specified in the row
            int fileSize = -1;
            try (ResultSet rs2 = connection
            		.createStatement()
            		.executeQuery("SELECT OCTET_LENGTH(File) FROM files WHERE FileID = " + fileId)) {
    	        if (rs2.next())
    	            fileSize = rs2.getInt(1);
            } catch (SQLException e) {
            	System.err.println(e);
            }
            userFile.setFileSize(fileSize);
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } 
		return userFile;
	}
	
	public static boolean downloadFile(long fileId, OutputStream outStream) throws IOException {
		// size of byte buffer to send file
	    final int BUFFER_SIZE = 4096;   
	    
		if (outStream == null) 
			return false;
        String query = "SELECT * FROM files WHERE FileID = ?";
        PreparedStatementSetter pss = (ps) -> ps.setLong(1, fileId); 
        try (Connection connection = ConnectionPool.getInstance().getConnection();
        		PreparedStatement ps = DBUtil.createPreparedStatement(connection, query, pss);
        		ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Blob blob = rs.getBlob("File");
                InputStream inputStream = blob.getBinaryStream();
                // writes the file to the client
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                 
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
        		inputStream.close();
        		//outStream.close();
            } else {
            	return false;
            }
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } 
		return true;
	}


}

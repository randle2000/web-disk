package com.sln.webdisk.business;

import java.io.Serializable;
import java.util.Date;

public class UserFile implements Serializable {
	private long fileId;
	private long userId;
	private String fileName;
    private int fileSize;
    private Date uploadDate;

    public UserFile() {
        fileName = "";
        fileSize = -1;
        uploadDate = null;
    }

    public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
}

package com.poscodx.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.poscodx.mysite.exception.FileUploadServiceException;

@Service
public class FileUploadService {
	final static String ServiceName = "image-service";

	@Value("${mysite.upload.location}")
	private String uploadLocation;

	@Value("${mysite.static.pathBase}")
	private String staticPathBase;
	
	public String restore(MultipartFile file) {
		final String savePath = uploadLocation + "/" + ServiceName;
		final String urlBase = staticPathBase + "/" + ServiceName;
		
		String url = null;
		
		try {
			File uploadDirectory = new File(savePath);
			if(!uploadDirectory.exists()) {
				uploadDirectory.mkdirs();
			}
			
			if(file.isEmpty()) {
				return url;
			}
			
			String originFilename = file.getOriginalFilename();
			String extName = originFilename.substring(originFilename.lastIndexOf(".") + 1);
			String saveFilename = generateSaveFilename(extName);
			Long fileSize = file.getSize();
			
			System.out.println("########" + originFilename);
			System.out.println("########" + saveFilename);
			System.out.println("########" + fileSize);
			
			byte[] data = file.getBytes();
			OutputStream os = new FileOutputStream(savePath + "/" + saveFilename);
			os.write(data);
			os.close();
			
			url = urlBase + "/" + saveFilename;
			
		} catch(IOException ex) {
			throw new FileUploadServiceException(ex.toString());
		}
		
		return url;
	}

	private String generateSaveFilename(String extName) {
		String filename = "";
		
		Calendar calendar = Calendar.getInstance();
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName);
		
		return filename;
	}

}

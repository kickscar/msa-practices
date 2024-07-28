package com.poscodx.msa.service.storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

@Service
public class FileUploadService {

	@Value("${storage.location}")
	private String storageLocation;

	@Value("${storage.httpd.host}")
	private String host;

	@Value("${storage.httpd.port}")
	private int port;

	public String restoreImage(MultipartFile file) throws RuntimeException {
		try {
			File uploadDirectory = new File(storageLocation);
			if(!uploadDirectory.exists()) {
				uploadDirectory.mkdirs();
			}
			
			if(file.isEmpty()) {
				throw new RuntimeException("file upload error: image empty");
			}
			
			String originFilename = file.getOriginalFilename();
			String extName = originFilename.substring(originFilename.lastIndexOf('.')+1);
			String saveFilename = generateSaveFilename(extName);
			
			byte[] data = file.getBytes();
			OutputStream os = new FileOutputStream(storageLocation + "/" + saveFilename);
			os.write(data);
			os.close();

			String[] locations = storageLocation.split("/");
			return String.format("http://%s:%d/%s/%s", host, port, locations[locations.length-1], saveFilename);

		} catch(IOException ex) {
			throw new RuntimeException("file upload error:" + ex);
		}
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

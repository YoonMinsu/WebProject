package org.zreock.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class UploadController {

	@GetMapping("/uploadForm")
	public void uploadForm() {
		
		log.info( "upload form" );
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormatPost(MultipartFile[] uploadFile, Model model) {
		
		String uploadFolder = "C:\\upload";
		
		for ( MultipartFile multipartFile : uploadFile ) {
			
			log.info("========================================");
			log.info("Uploac File Name : " + multipartFile.getOriginalFilename());
			log.info("Upload File Szie : " + multipartFile.getSize());
			log.info("========================================");
			
			File saveFile = new File( uploadFolder, multipartFile.getOriginalFilename());
		
			
			try {
				multipartFile.transferTo(saveFile);
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			
		} // end for
	}
	
	@PostMapping("/uploadAjaxAction")
	public void uploadAjaxPost( MultipartFile[] uploadFile ) {
		
		log.info( "update ajax post...........");
		
		String uploadFolder = "C:\\upload";
		
		// make folder
		File uploadPath = new File( uploadFolder, getFolder() );
		
		log.info("upload Path : " + uploadPath );
		
		if (uploadPath.exists() == false ) {
			uploadPath.mkdirs();
		}
		// make yyyy/MM/dd folder
		
		
		for ( MultipartFile multipartFile : uploadFile ) {
			
			log.info("=======================================");
			log.info("Upload File Name : " + multipartFile.getOriginalFilename() );
			log.info("Upload File Size : " + multipartFile.getSize() );
			
			String uploadFileNmae = multipartFile.getOriginalFilename();
			
			uploadFileNmae = uploadFileNmae.substring(uploadFileNmae.lastIndexOf("\\") + 1 );
			
			log.info( "only file name : " + uploadFileNmae );
			UUID uuid = UUID.randomUUID();
			
			uploadFileNmae = uuid.toString() + "_" + uploadFileNmae;
			
			File saveFile = new File( uploadPath, uploadFileNmae );
			log.info("=======================================");
			try {
				multipartFile.transferTo( saveFile );
			} catch ( Exception e ) {
				log.error( e.getMessage() );
			}
		}
		
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
}

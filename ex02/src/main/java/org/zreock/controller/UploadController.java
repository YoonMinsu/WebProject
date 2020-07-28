package org.zreock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

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
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost( MultipartFile[] uploadFile ) {
		
		log.info( "update ajax post...........");
		
		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";
		String uploadFolderPath = getFolder();
		
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
			
			AttachFileDTO attachFileDTO = new AttachFileDTO();
			
			String uploadFileNmae = multipartFile.getOriginalFilename();
			
			// IE has file path
			uploadFileNmae = uploadFileNmae.substring(uploadFileNmae.lastIndexOf("\\") + 1 );
			log.info( "only file name : " + uploadFileNmae );
			attachFileDTO.setFileName(uploadFileNmae);
			
			UUID uuid = UUID.randomUUID();
			
			uploadFileNmae = uuid.toString() + "_" + uploadFileNmae;
			
			log.info("=======================================");
			try {
				File saveFile = new File( uploadPath, uploadFileNmae );
				multipartFile.transferTo( saveFile );
				
				attachFileDTO.setUuid( uuid.toString() );
				attachFileDTO.setUploadPath(uploadFolderPath);
				
				//check Image type file
				if ( checkImageType( saveFile ) ) {
					
					attachFileDTO.setImage( true );
					
					FileOutputStream thumbnail = new FileOutputStream( new File( uploadPath, "s_" + uploadFileNmae ) );
					
					Thumbnailator.createThumbnail( multipartFile.getInputStream(), thumbnail, 100,100 );
					
					thumbnail.close();
				}
				
				// add to List
				list.add( attachFileDTO );
			} catch ( Exception e ) {
				e.printStackTrace();
			} // end catch
		} // end for
		return new ResponseEntity<List<AttachFileDTO>>(list, HttpStatus.OK);
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
	
	private boolean checkImageType( File file ) {

		try {
			String contentType = Files.probeContentType( file.toPath() );
			
			return contentType.startsWith("image");
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		return false;
	}

	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile( String fileName ) {
		log.info( "#=====================================#");
		log.info( "fileName : " + fileName );
		
		File file = new File( "c:\\upload\\" + fileName );
		
		log.info( "file : " + file );
		
		ResponseEntity<byte[] > result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK );
		
		} catch ( IOException e ){
			e.printStackTrace();
		}
		
		log.info( "#=====================================#\n");
		return result;
	}

	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {
		log.info("===================================");
		
		log.info("download file : " + fileName );
		
		Resource resource = new FileSystemResource("c:\\upload\\" + fileName );
		
		if ( resource.exists() == false ) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		
		log.info( "resource : " + resource );
		
		String resourceName = resource.getFilename();
		
		//remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1 );
		
		HttpHeaders header = new HttpHeaders();
		
		try {
			
			String downloadName = null;
			
			//인터넷 익스플로러
			if ( userAgent.contains("Trident")) {
				
				log.info( "IE Browser" );
				
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll( "\\+", " " );
			
			// 엣지 브라우저
			} else if ( userAgent.contains( "Edge" ) ) {
				
				log.info( "Edge Browser" );
				
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8" );
				
				log.info( "Edge Name : " + downloadName );
			
			// 크롬 브라우저
			} else {
				
				log.info( "Chrome Browser" );
				
				downloadName = new String( resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
				
			}
			
			log.info( "downloadName : " + downloadName );
			
			header.add("Content-Disposition", "attachment; filename=" + downloadName);
			
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		} // end catch
		
		log.info("===================================\n");
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK );
	} // end downloadFile
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type) {
		log.info("====================================");
		
		File file;
		
		try {
			
			file = new File( "c:\\upload\\" + URLDecoder.decode(fileName,"UTF-8"));
		
			file.delete();
			
			if ( type.equals("image") ) {
				
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				
				log.info("largeFileName : " + largeFileName );
				
				file = new File(largeFileName);
				
				file.delete();
				
			} // end if
			
			
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
		
		log.info("====================================\n");
		return new ResponseEntity<String>("deleted", HttpStatus.OK );
	}
	
}



























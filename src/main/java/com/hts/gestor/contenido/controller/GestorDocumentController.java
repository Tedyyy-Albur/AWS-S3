package com.hts.gestor.contenido.controller;

import com.amazonaws.services.rekognition.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hts.gestor.contenido.dto.RespuestaGenerica;
import com.hts.gestor.contenido.services.GestorBucketService;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/gestor/documento")
@RequiredArgsConstructor
public class GestorDocumentController {
	
	@Autowired
	private final GestorBucketService gestorBucketService;

	
	@PostMapping("/create/folder")
	public @ResponseBody
	RespuestaGenerica createS3Bucket(@RequestParam("bucketName") String bucketName) {
		System.out.println("Recibiendo bucket" + bucketName);
		return gestorBucketService.createBucket(bucketName);
	}
	
	@GetMapping("/list/folder")
	public @ResponseBody
	RespuestaGenerica listS3Bucket() {
		return gestorBucketService.listBucket();
	}

	@DeleteMapping("/delete/{bucketName}")
	public @ResponseBody
	RespuestaGenerica deleteBucket(@PathVariable String bucketName) {
		return gestorBucketService.deleteBucket(bucketName);
	}

	@DeleteMapping("/delete/object")
	public @ResponseBody
	RespuestaGenerica deleteFile(@RequestParam("bucketName") String bucketName, @RequestParam("objectName") String objectName) {
		return gestorBucketService.deleteFile(bucketName, objectName);
	}


	
	@PostMapping("/file/upload")
	RespuestaGenerica uploadFile(@RequestParam("bucketName") String bucketName, @RequestParam("fileName") String fileName,
            @RequestParam("file") MultipartFile file) throws IOException {
		return gestorBucketService.uploadFile(bucketName, fileName, file, false);
	}

	@GetMapping("/list/files")
	public ResponseEntity<List<String>> getListOfFiles(@RequestParam("name") String bucketName) {
		return new ResponseEntity<>(gestorBucketService.listFiles(bucketName), HttpStatus.OK);
	}

	//@GetMapping("/downloadS3File" )
	@RequestMapping(value = "/downloadS3File", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ByteArrayResource>
	downloadS3File(@RequestParam(value = "bucketName") String bucketName, @RequestParam(value = "fileName") String fileName)
			throws IOException {
		byte[] data= gestorBucketService.downloadFile(bucketName,fileName);
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity.ok()
				.contentLength(data.length)
				.header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
				.body(resource);
	}
}

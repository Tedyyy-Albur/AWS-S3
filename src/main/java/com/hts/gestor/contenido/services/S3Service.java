package com.hts.gestor.contenido.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.hts.gestor.contenido.dto.BucketObjectRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {
	 private final AmazonS3 amazonS3Client;

	    //Bucket level operations

	    public String createS3Bucket(String bucketName, boolean publicBucket) {
	    	log.info("generando un nuevo bucket con el nombre de :::::" + bucketName);
	    	
	        if(amazonS3Client.doesBucketExist(bucketName)) {
	            log.info("Bucket name already in use. Try another name.");
	            return "Fail";
	        }
	        if(publicBucket) {
	            amazonS3Client.createBucket(bucketName);
	        } else {
	            amazonS3Client.createBucket(new CreateBucketRequest(bucketName).withCannedAcl(CannedAccessControlList.Private));
	        }
	        return "Bucket Creado Correctamente";
	    }

	    public List<Bucket> listBuckets(){
	        return amazonS3Client.listBuckets();
	    }

	    public String deleteBucket(String bucketName){
	        try {
	            amazonS3Client.deleteBucket(bucketName);
	        } catch (AmazonServiceException e) {
	            log.error(e.getErrorMessage());
	            return "Fail";
	        }
	        return "Se elimino Correctamente el bucket";
	    }

	    //Object level operations
	    public String putObject(String bucketName, BucketObjectRepresentation representation, boolean publicObject) throws IOException {

	        String objectName = representation.getObjectName();
	        MultipartFile objectValue = representation.getStream();
	        
	        /*ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucketName, keyName, file.getInputStream(), metadata);*/

	        File file = convertMultiPartToFile(objectValue);
	        
	        /*
	        FileWriter fileWriter = new FileWriter(file, false);
	        PrintWriter printWriter = new PrintWriter(fileWriter);
	        printWriter.println(objectValue);
	        printWriter.flush();
	        printWriter.close();
	        */

	        try {
	            if(publicObject) {
	                var putObjectRequest = new PutObjectRequest(bucketName, objectName, file).withCannedAcl(CannedAccessControlList.PublicRead);
	                amazonS3Client.putObject(putObjectRequest);
	            } else {
	                var putObjectRequest = new PutObjectRequest(bucketName, objectName, file).withCannedAcl(CannedAccessControlList.Private);
	                amazonS3Client.putObject(putObjectRequest);
	            }
	        } catch (Exception e){
	            log.error("Error ::: " + e);
	            return "Error al subir archivo";
	        }
	        return "Archivo guardado correctamente:: " + objectName;
	    }

	    public List<S3ObjectSummary> listObjects(String bucketName){
	        ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
	        return objectListing.getObjectSummaries();
	    }

	    public void downloadObject(String bucketName, String objectName){
	        S3Object s3object = amazonS3Client.getObject(bucketName, objectName);
	        S3ObjectInputStream inputStream = s3object.getObjectContent();
	        try {
	            FileUtils.copyInputStreamToFile(inputStream, new File("." + File.separator + objectName));
	        } catch (IOException e) {
	            log.error(e.getMessage());
	        }
	    }

	    public String deleteObject(String bucketName, String objectName){
			try{
				amazonS3Client.deleteObject(bucketName, objectName);
			} catch (AmazonServiceException e) {
				log.error(e.getErrorMessage());
				return "Fail";
			}
			return "Se elimino Correctamente el archivo";
	    }

	    public void deleteMultipleObjects(String bucketName, List<String> objects){
	        DeleteObjectsRequest delObjectsRequests = new DeleteObjectsRequest(bucketName)
	                .withKeys(objects.toArray(new String[0]));
	        amazonS3Client.deleteObjects(delObjectsRequests);
	    }

	    public void moveObject(String bucketSourceName, String objectName, String bucketTargetName){
	        amazonS3Client.copyObject(
	                bucketSourceName,
	                objectName,
	                bucketTargetName,
	                objectName
	        );
	    }
	    
	    private File convertMultiPartToFile(MultipartFile file) throws IOException {
	        File convFile = new File(file.getOriginalFilename());
	        FileOutputStream fos = new FileOutputStream(convFile);
	        fos.write(file.getBytes());
	        fos.close();
	        return convFile;
	    }


	public  byte[]  downloadFile(String bucketName, String fileName) {
		S3Object s3Object = amazonS3Client.getObject(bucketName, fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

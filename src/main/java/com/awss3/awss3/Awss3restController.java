package com.awss3.awss3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@CrossOrigin
@RestController
public class Awss3restController {
	
	Regions clientRegion = Regions.US_EAST_1;
    String BUCKETNAME = "ec2logs-bucket";
    String KEY = "log/auth.log";
    
    String ACCESS_KEY = "access_ey";
    String SECRET_KEY = "secret_key";
    
    @GetMapping("/buckets")
	public ResponseEntity<String> listBuckets(HttpServletRequest httpServletRequest) {
    	
    	BasicAWSCredentials cred = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
    	
    	AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .build();
    	
    	List<Bucket> listBuckets = s3Client.listBuckets();
    	listBuckets.forEach(buc -> {
    		System.out.println(buc.getName());
    	});
    	
		return null;
    }
    
    @GetMapping("/keys")
	public ResponseEntity<String> listKeys() {
    	
    	BasicAWSCredentials cred = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
    	
    	AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .build();
    	
    	 List<S3ObjectSummary> objectSummaries = s3Client.listObjects(BUCKETNAME).getObjectSummaries();
    	
    	 objectSummaries.forEach(objs -> {
    		 System.out.println(objs.getKey());
    	 });
    	 
		return null;
    }
    
    @GetMapping("/download")
	public ResponseEntity<String> downloadObject() throws IOException {
    	
    	BasicAWSCredentials cred = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
    	
    	AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .build();
    	
    	 S3Object object = s3Client.getObject(BUCKETNAME, KEY);
    	 
    	 displayTextInputStream(object.getObjectContent());
    	     	 
		return null;
    }
    
    private static void displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println();
    }
}

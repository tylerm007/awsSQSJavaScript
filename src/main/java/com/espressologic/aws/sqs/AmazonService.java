package com.espressologic.aws.sqs;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;

/**
 * Created by Tyler on 5/12/14.
 */
public class AmazonService {
    protected static AWSCredentials credentials = null;
    protected static Regions myRegion = Regions.US_EAST_1;/// TO DO - pass as parm?


    public AmazonService(String accessKey, String secretKey) {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    public AmazonService(String accessKey, String secretKey, String regionName) {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
        Regions myRegion = Regions.fromName(regionName);
    }

    public AmazonService() {

    }

    public static AWSCredentials getCredentials() {
        return credentials;
    }

    public static void setCredentials(AWSCredentials credentials) {
        AmazonService.credentials = credentials;
    }

    public static Regions getMyRegion() {
        return myRegion;
    }

    public static void setMyRegion(String region) {
        AmazonService.myRegion = Regions.fromName(region);
    }

    /*
     * The ProfileCredentialsProvider will return your [default]
     * credential profile by reading from the credentials file located at
     * (~/.aws/credentials).
     * TO DO - rewrite this to pass in credentials
     */
    protected static AmazonSQS createAWSCredentials() {
        //use ~/.aws/credentials first
        try {
            if (credentials == null) {
                credentials = new ProfileCredentialsProvider().getCredentials();
            }
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        AmazonSQS sqs = new AmazonSQSClient(credentials);
        Region region = Region.getRegion(myRegion);
        sqs.setRegion(region);

        return sqs;
    }
}

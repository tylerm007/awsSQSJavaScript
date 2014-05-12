package com.espressologic.aws.sqs;
/*
 * Copyright 2014-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import com.amazonaws.regions.Region;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;

import java.io.IOException;

public class AmazonEmailService extends AmazonService {


    public AmazonEmailService(String accessKey, String secretKey) {
        super(accessKey, secretKey);
    }

    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (~/.aws/credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WANRNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    public static void main(String[] args) throws IOException {
        String FROM = "tylerm007@gmail.com";  // Replace with your "From" address. This address must be verified.
        String TO = "tylerm007@gmail.com"; // Replace with a "To" address. If you have not yet requested
        // production access, this address must be verified.
        String BODY = "This email was sent through Amazon SES by using the AWS SDK for Java.";
        String SUBJECT = "Amazon SES test (AWS SDK for Java)";

        // Construct an object to contain the recipient address.
        try {
            SendEmailRequest request = AmazonEmailService.createEmailRequestInternal(TO, FROM, SUBJECT, BODY);
            sendEmalRequest(request);
        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }
    }

    /**
     * wrapper function to build message and send in one step (called by JavaScript)
     *
     * @param messageTo
     * @param messageFrom
     * @param msgSubject
     * @param msgBody
     * @throws Exception
     */
    public static void sendEmailRequest(String messageTo, String messageFrom, String msgSubject, String msgBody) throws Exception {
        sendEmalRequest(createEmailRequestInternal(messageTo, messageFrom, msgSubject, msgBody));
    }

    /**
     * send email using credentials and region
     *
     * @param request
     * @throws Exception
     */
    public static void sendEmalRequest(SendEmailRequest request) throws Exception {
        try {
            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            /*
             * The ProfileCredentialsProvider will return your [default]
             * credential profile by reading from the credentials file located at
             * (~/.aws/credentials).
             *
             * TransferManager manages a pool of threads, so we create a
             * single instance and share it throughout our application.
             */
            createAWSCredentials();

            // Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);

            // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your production
            // access status, sending limits, and Amazon SES identity-related settings are specific to a given
            // AWS region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using
            // the US East (N. Virginia) region. Examples of other regions that Amazon SES supports are US_WEST_2
            // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html
            Region REGION = Region.getRegion(getMyRegion());
            client.setRegion(REGION);

            // Send the email.
            client.sendEmail(request);
            System.out.println("Email sent!");

        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * build the email message
     *
     * @param messageTo
     * @param messageFrom
     * @param msgSubject
     * @param msgBody
     * @return
     */
    public static SendEmailRequest createEmailRequestInternal(String messageTo, String messageFrom, String msgSubject, String msgBody) {
        Destination destination = new Destination().withToAddresses(new String[]{messageTo});

        // Create the subject and body of the message.
        Content subject = new Content().withData(msgSubject);
        Content textBody = new Content().withData(msgBody);
        Body body = new Body().withText(textBody);

        // Create a message with the specified subject and body.
        Message message = new Message().withSubject(subject).withBody(body);

        // Assemble the email.
        return new SendEmailRequest().withSource(messageFrom).withDestination(destination).withMessage(message);
    }
}

package com.espressologic.aws.sqs;

/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.*;

import java.util.List;
import java.util.Map.Entry;

/**
 * This sample demonstrates how to make basic requests to Amazon SQS using the
 * AWS SDK for Java.
 * <p/>
 * <b>Prerequisites:</b> You must have a valid Amazon Web
 * Services developer account, and be signed up to use Amazon SQS. For more
 * information on Amazon SQS, see http://aws.amazon.com/sqs.
 * <p/>
 * Fill in your AWS access credentials in the provided credentials file
 * template, and be sure to move the file to the default location
 * (~/.aws/credentials) where the sample code will load the credentials from.
 * <p/>
 * <b>WANRNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 */
public class SqsAmazonService extends AmazonService {

    private static String endpoint = null;
    private static String myQueueUrl = null;

    public static void main(String[] args) throws Exception {

        try {
            credentials = null;
            SqsAmazonService.myQueueUrl = createQueue("EspressoLogic9");
            listQueues();
            String msgID = sendMessage("My Message Test");
            System.out.println("Message ID " + msgID);
            String messageID = readMessage(msgID);
            //deleteMessages(messageID);
            deleteQueue();
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        } finally {
            credentials = null;
        }
    }

    public SqsAmazonService(String myEndpoint) {
        super();
        System.out.println("queue endpoint name " + myEndpoint);
        this.endpoint = myEndpoint;
        this.myQueueUrl = createQueue(endpoint);
        System.out.println(this.myQueueUrl);
    }

    public SqsAmazonService(String accessKey, String secretKey) {
        credentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    public SqsAmazonService(String myEndpoint, String accessKey, String secretKey) {
        System.out.println("queue endpoint name " + myEndpoint);
        endpoint = myEndpoint;
        this.myQueueUrl = createQueue(endpoint);
        System.out.println(this.myQueueUrl);
        credentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    public static void deleteQueue() {
        // Delete a queue
        System.out.println("Deleting the test queue.\n");
        createAWSCredentials().deleteQueue(new DeleteQueueRequest(myQueueUrl));
    }

    public static String listQueues() {
        // List queues
        String sep = "";
        AmazonSQS sqs = createAWSCredentials();
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        System.out.println("Listing all queues in your account.\n");
        for (String queueUrl : sqs.listQueues().getQueueUrls()) {
            System.out.println("  QueueUrl: " + queueUrl);
            sb.append(sep);
            sb.append("\"url\":");
            sb.append("\"");
            sb.append(queueUrl);
            sb.append("\"");
            if ("".equals(sep)) sep = ",";
        }
        sb.append("]");
        System.out.println();
        return sb.toString();
    }

    public static String createQueue(String queueName) {
        // Create a queue
        AmazonSQS sqs = createAWSCredentials();
        assert queueName != null;
        System.out.println("Creating a new SQS queue called " + queueName + ".\n");
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        return sqs.createQueue(createQueueRequest).getQueueUrl();
    }

    public static void deleteMessages(String messageRecieptHandle) {
        // Delete a message
        AmazonSQS sqs = createAWSCredentials();
        System.out.println("Deleting a message.\n");
        // String messageRecieptHandle = messages.get(0).getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageRecieptHandle));
    }

    public static String readMessage(String messageID) {
        // Receive messages
        AmazonSQS sqs = createAWSCredentials();
        System.out.println("Receiving messages from " + myQueueUrl + ".\n");
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        String sep = "";
        String receipt = null;
        for (Message message : messages) {
            if (message.getMessageId().equals(messageID)) {
                displayMessageContent(message);
                sb.append(sep);
                sb.append("\"message\" :");
                sb.append(message.getBody());
                if ("".equals(sep)) sep = ",";
                receipt = message.getReceiptHandle();
                deleteMessages(receipt);
            }
        }
        sb.append("]");
        System.out.println();
        return sb.toString();
    }

    private static void displayMessageContent(Message message) {
        System.out.println("  Message");
        System.out.println("    MessageId:     " + message.getMessageId());
        System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
        System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
        System.out.println("    Body:          " + message.getBody());
        for (Entry<String, String> entry : message.getAttributes().entrySet()) {
            System.out.println("  Attribute");
            System.out.println("    Name:  " + entry.getKey());
            System.out.println("    Value: " + entry.getValue());
        }
    }

    public static String sendMessage(String messageText) {
        // Send a message
        AmazonSQS sqs = createAWSCredentials();
        System.out.println("Sending a message to " + myQueueUrl + ".\n");
        SendMessageResult result = sqs.sendMessage(new SendMessageRequest(myQueueUrl, messageText));
        return result.getMessageId();
    }

}

// Equivalent in effect to the Java declaration import java.io.*;
importPackage(java.io);
importPackage(java.lang);
 
out = java.lang.System.out;

var SysUtility = {

    sendMessage : function sqsClient(myQueue,accessKey,secretKey,message) {
            var sqsClient = new com.espressologic.aws.sqs.SqsAmazonService(myQueue,accessKey,secretKey);
            var result = sqsClient.sendMessage(message);
            return result;
    },
    readMessage : function sqsClient(myQueue,accessKey,secretKey,messageID) {
	        var sqsClient = new com.espressologic.aws.sqs.SqsAmazonService(myQueue,accessKey,secretKey);
	        var result = sqsClient.readMessage(messageID);
	        return result;
    },
    createQueue : function sqsClient(myQueue,accessKey,secretKey) {
            var sqsClient = new com.espressologic.aws.sqs.SqsAmazonService(myQueue,accessKey,secretKey);
            var result = sqsClient.createQueue(myQueue);
            return result;
    },
    deleteQueue : function sqsClient(myQueue,accessKey,secretKey) {
            var sqsClient = new com.espressologic.aws.sqs.SqsAmazonService(myQueue,accessKey,secretKey);
            var result = sqsClient.deleteQueue();
            return result;
    },
    listQueues : function sqsClient(accessKey,secretKey) {
            var sqsClient = new com.espressologic.aws.sqs.SqsAmazonService(accessKey,secretKey);
            var result = sqsClient.listQueues();
            return result;
    },
    sendEmail : function sendEmail(accessKey,secretKey,to,from,subject,message){
        var emailClient = new  com.espressologic.aws.sqs.AmazonEmailService(accessKey,secretKey);
        var result = emailClient.sendEmailRequest(to,from,subject,message);
    }
};

load("SQSProvider.js");

// configuration needed for testing (should be read from SQL using Espresso Logic)
//var configSetup = {
//    AWSAccessKeyId : "myAccessKey",
//    AWSSecretKey: "mySecretKey",
//    SQSQueueName : "EspressoLogic7"
//};
var configSetup = {
    AWSAccessKeyId : "AKIAJCX57NYTDTNT7Q3Q",
    AWSSecretKey: "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",
    SQSSendQueueName : "EspressoLogic",
    SQSReadQueueName : "EspressoLogic"
};

var sqs = amazonSQSrCreate();
sqs.configure(configSetup);


out.println("-------------list queues ");
var result =  sqs.listQueues();
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");

/*


out.println("-------------create queues ");
var result =  sqs.createQueue("EspressoLogic6");
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");

*/

out.println("------------- send message");
var messageID = sqs.sendMessage("Logic Test Message");
//out.println(JSON.stringify(result, null, 2));
out.println(messageID);
out.println("-------------");


out.println("------------- read message");
var result = sqs.readMessage(messageID);
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");


out.println("------------- send email message");
var to = 'tylerm007@gmail.com';
var from = 'tylerm007@gmail.com';
var subject = 'JavaScript email';
var body = 'posted from test JavaScript';
sqs.sendEmail(to,from,subject,body);
out.println("-------------");

out.println("-------------delete queues ");
sqs.deleteQueue('EspressoLogic7');
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");

var running = true;
do {
	var chronTable = logicContext.getBeanByPrimaryKey(“chronTable”,1);	
	running = chronTable || chronTable.state === ‘RUN’;
	
	thread.sleep(1000 * chronTable.interval);
 	var sqsSenddBean = LogicContext.createPersistentBean('sqsSendMessage');
 	sqsSendBean.Message =  chronTable.actionToPerform ;
 	LogicContext.insert(sqsSendBean);

} while (running);

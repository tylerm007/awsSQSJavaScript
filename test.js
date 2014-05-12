out = java.lang.System.out;

var SysUtility = {

    sendMessage : function sqsClient(myQueue,accessKey,secretKey,message) {
            var sqsClient = new com.espressologic.aws.sqs.SimpleQueueService(myQueue,accessKey,secretKey);
            var result = sqsClient.sendMessage(message);
            return result;
    },
    readMessage : function sqsClient(myQueue,accessKey,secretKey) {
	        var sqsClient = new com.espressologic.aws.sqs.SimpleQueueService(myQueue,accessKey,secretKey);
	        var result = sqsClient.readMessage();
	        return result;
    },
    createQueue : function sqsClient(myQueue,accessKey,secretKey) {
            var sqsClient = new com.espressologic.aws.sqs.SimpleQueueService(myQueue,accessKey,secretKey);
            var result = sqsClient.createQueue(myQueue);
            return result;
    },
    deleteQueue : function sqsClient(myQueue,accessKey,secretKey) {
            var sqsClient = new com.espressologic.aws.sqs.SimpleQueueService(myQueue,accessKey,secretKey);
            var result = sqsClient.deleteQueue();
            return result;
    },
    listQueues : function sqsClient(accessKey,secretKey) {
            var sqsClient = new com.espressologic.aws.sqs.SimpleQueueService(accessKey,secretKey);
            var result = sqsClient.listQueues();
            return result;
    }
};

load("SQSProvider.js");

// configuration needed for testing
var configSetup = {
    AWSAccessKeyId : "myawsAccessKeyID",
    AWSSecretKey: "myAWSSecretKey",
    SQSQueueName : "EspressoLogic"
};


var sqs = amazonSQSrCreate();
sqs.configure(configSetup);


out.println("-------------delete queues ");
//var result =  sqs.deleteQueue();
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");

out.println("-------------create queues ");
//var result =  sqs.createQueue("EspressoLogic");
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");

out.println("-------------list queues ");
var result =  sqs.listQueues();
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");


out.println("------------- send message");
var result = sqs.sendMessage("Logic Test Message");
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");


out.println("------------- read message");
var result = sqs.readMessage();
//out.println(JSON.stringify(result, null, 2));
out.println("-------------");

function amazonSQSrCreate() {
    var result = {};

    var result = {};
    var configSetup = {
        AWSAccessKeyId : "",
        AWSSecretKey: "",
        SQSSendQueueName : "",
        SQSReadQueueName : ""
    };

    result.configure = function configure(myConfig) {
        configSetup.AWSAccessKeyId = myConfig.AWSAccessKeyId || "";
        configSetup.AWSSecretKey = myConfig.AWSSecretKey || "";
        configSetup.SQSSendQueueName = myConfig.SQSSendQueueName || "";
        configSetup.SQSReadQueueName = myConfig.SQSReadQueueName || "";
    };

    result.readMessage = function readMessage(){
        var response = SysUtility.readMessage(configSetup.SQSReadQueueName,configSetup.AWSAccessKeyId,configSetup.AWSSecretKey);
        out.println(response);
        return response;

    };

    result.sendMessage = function sendMessage(msg){
         var response = SysUtility.sendMessage(configSetup.SQSSendQueueName, configSetup.AWSAccessKeyId,configSetup.AWSSecretKey,msg);
         out.println(response);
         return response;
    };

    result.listQueues = function listQueues(){
         var response = SysUtility.listQueues(configSetup.AWSAccessKeyId,configSetup.AWSSecretKey);
         out.println(response);
         //return JSON.parse(response);
         return response;
    };

     result.createQueue = function createQueue(queueName){
             var response = SysUtility.listQueues(queueName,configSetup.AWSAccessKeyId,configSetup.AWSSecretKey);
             out.println(response);
             return response;
      };

     result.deleteQueue = function deleteQueue(queueName){
          SysUtility.deleteQueue(queueName,configSetup.AWSAccessKeyId,configSetup.AWSSecretKey);
          // out.println(response);
          // return response;
     };

    result.sendEmail = function sendEmail(from,to,subject,body){
          SysUtility.sendEmail(configSetup.AWSAccessKeyId,configSetup.AWSSecretKey,from,to,subject,body);
           out.println("message sent");
          // return JSON.parse(response);
     };

    return result;

}

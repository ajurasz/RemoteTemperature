var FCM = require('fcm-node');
var config = require('./config.js');
var Device = require('./model/device')

var fcm = new FCM(config.gcm.API_KEY)

function push(message, registrationId, callback) {
    var message = { 
        to: registrationId, 
        data: {
            message: message
        }
    };

    fcm.send(message, function(err, response){
        if (err) {
            console.error("Could not send: " + message);
        } else {
            console.log("Successfully sent with response: ", response);
        }
    });
};

module.exports.push = push;

module.exports.pushAll = function(message, callback) {
    Device.find({}, function(err, devices) {
        if (err) 
            console.error(err);

        devices.forEach(function(device) {
            push(message, device.registrationId, callback);
        });
    });
}
var config = require('./config.js')
var mqtt = require('mqtt')
var gcm = require('./fcm')
var moment = require('moment')

var mqttClient = mqtt.connect(config.mqtt.BROKER_URL, [{
    username: config.mqtt.MQTT_CLIENT_USERNAME, 
    password: config.mqtt.MQTT_CLIENT_PASSWORD
}])

var lastUpdateDate = moment([2001, 1, 1]);

module.exports.start = function(aWss, lastTemp) {
    mqttClient.on('error', (error) => {
        console.error(error)
    })

    mqttClient.on('connect', () => {
        mqttClient.subscribe('temperature/home/basement')
    })

    mqttClient.on('message', (topic, message) => {
        console.log('received message %s %s', topic, message)
        lastTemp.temperature = message.toString();
        aWss.clients.forEach(function (client) {
            client.send(JSON.stringify({
                topic: topic,
                temperature: message.toString()
            }));
        });

        if (parseFloat(lastTemp.temperature) > 80.0) {
            var message = 'Warning temperature is very high ' + lastTemp.temperature + '°C';
            console.log(message);
            if (moment().diff(lastUpdateDate, 'minutes') > 5) {
                gcm.pushAll(message, function() {
                    lastUpdateDate = moment()
                });
            }
        }
    })

    return mqttClient;
};
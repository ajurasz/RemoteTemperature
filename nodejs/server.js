var config = require('./config.js');

var http = require('http');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var expressWs = require('express-ws')(app);
var moment = require('moment')

var mqttClient = require('./mqtt-client.js');

var db = require('./db.js').connect();
var Device = require('./model/device')

var lastTemp = { temperature: "0.0" };

app.set('view engine', 'ejs');

app.use(bodyParser.json());

app.get('/', function(req, res) {
    res.render('index', {
        ws: config.app.WS_URL
    });
});

app.post('/temperature', function(req, res) {
    res.send(lastTemp);
});

app.post('/device', function(req, res) {
    var deviceName = req.body.deviceName;
    var deviceId   = req.body.deviceId;
    var registrationId = req.body.registrationId;    

    Device.find({deviceId : deviceId}, function(err, devices) {
        if (err) {
            console.error(err);
            res.sendStatus(500);
        }

        if (devices.length == 0) {
            Device.create({
                deviceName: deviceName,
                deviceId: deviceId,
                registrationId: registrationId
            }, function(err, device) {
                if (err) {
                    console.error(err);
                    res.sendStatus(500);
                }
                res.sendStatus(200);
            });            
        } else {
            res.sendStatus(200);
        }        
    });
});

app.put('/device', function(req, res) {
    var deviceId   = req.body.deviceId;
    var registrationId = req.body.registrationId;

    Device.findOneAndUpdate({ deviceId: deviceId }, { registrationId: registrationId }, function(err, device) {
        if (err) {
            console.error(err);
            res.sendStatus(500);
        }
        res.sendStatus(200);
    });
});

app.delete('/device/:deviceId', function(req, res) {
    var deviceId   = req.params.deviceId;
    Device.findOneAndRemove({deviceId : deviceId}, function(err) {
        if (err) {
            console.error(err);
            res.sendStatus(500);
        }
        res.sendStatus(200);
    });
});

app.ws('/', function(ws, req) {
    ws.send(JSON.stringify(lastTemp));
});

app.listen(config.app.PORT, function () {
    console.log( 'Listening on %s:%s', config.app.IP_ADDRESS, config.app.PORT);
});

mqttClient.start(expressWs.getWss('/'), lastTemp);

app.get('/health', function (req, res) {
    res.sendStatus(200);
});

setInterval(function() {
    var now = moment().hour();
    if (!(now >= 2 && now <= 14)) {
        http.get('http://' + config.app.HOSTNAME + '/health', function(res) {
            console.log('Dont sleep ...');
        });
    }
}, 300000); // 5 minutes

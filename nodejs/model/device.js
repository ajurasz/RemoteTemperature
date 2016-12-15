var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var deviceSchema = new Schema({
    deviceName: {type: String, required: true},
    deviceId: {type: String, required: true},
    registrationId: {type: String, required: true}
},
{
  timestamps: true
});

var Device = mongoose.model('Device', deviceSchema);

module.exports = Device;
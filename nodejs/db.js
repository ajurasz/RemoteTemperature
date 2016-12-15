var mongoose = require('mongoose');
var config = require('./config.js');

module.exports.connect = function (cb) {
  mongoose.Promise = global.Promise;
  var db = mongoose.connect(config.db.URI, function (err) {
    if (err) {
      console.error(err);
    } else {
      mongoose.set('debug', config.db.DEBUG);
      if (cb) cb(db);
    }
  });

  return db;
};
module.exports = {
    app: {
        IP_ADDRESS: process.env.IP || '0.0.0.0',
        PORT: process.env.PORT || 3000,
        WS_URL: process.env.WS_URL || 'ws://localhost:3000/',
        HOSTNAME: process.env.HOSTNAME || 'localhost'
    },
    mqtt: {
        BROKER_URL: 'mqtt://' + process.env.MQTT_BROKER_USERNAME + ':' + process.env.MQTT_BROKER_PASSWORD + '@' + process.env.MQTT_BROKER_HOST + ':' + process.env.MQTT_BROKER_PORT,
        CLIENT_USERNAME: process.env.MQTT_CLIENT_USERNAME || '',
        CLIENT_PASSWORD: process.env.MQTT_CLIENT_PASSWORD || ''
    },
    db: {
        URI: process.env.MONGO_URL || 'mongodb://localhost/remote_temp',
        DEBUG: process.env.MONGODB_DEBUG || false
    },
    gcm: {
        API_KEY: process.env.GCM_API_KEY || ''
    }
};

#include <PubSubClient.h>
#include <OneWire.h>
#include <DallasTemperature.h>


#define wifi_ssid "<your ssid>"
#define wifi_password "<your ssid password>"

#define mqtt_server "<mqtt broker ip address or host name>"
#define mqtt_server_port "<mqtt broker port>"
#define mqtt_user "<mqtt username>"
#define mqtt_password "<mqtt user password>"

#define temperature_topic "temperature/home/basement"

#define ONE_WIRE_BUS 2

WiFiClient espClient;
PubSubClient client(espClient);

OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);

float previousTemperature;

void setup() {
  Serial.begin(115200);
  sensors.begin();
  setup_wifi();
  client.setServer(mqtt_server, mqtt_server_port);
  previousTemperature = 0.0;
}

void setup_wifi() {
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(wifi_ssid);

  WiFi.begin(wifi_ssid, wifi_password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP8266BasementClient", mqtt_user, mqtt_password)) {
      Serial.println("connected");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void loop() {
  if (!client.connected()) {
    reconnect(); 
  }
  client.loop();

  sensors.requestTemperatures();
  float tempC = sensors.getTempCByIndex(0);
  if (abs(tempC - previousTemperature) > 0.5) {
    client.publish(temperature_topic, String(tempC).c_str(), true);
    previousTemperature = tempC;
  }

  // Wait 30 seconds
  delay(30000);
}

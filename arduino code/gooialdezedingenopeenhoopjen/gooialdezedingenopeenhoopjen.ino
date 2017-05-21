#include "SD.h"
#define SD_ChipSelectPin 4
#include "TMRpcm.h"
#include "SPI.h"
#include <SoftwareSerial.h>
#include <Wire.h>

#define addr 0x1E

SoftwareSerial esp8266(2, 3);

#define DEBUG true //show messages between ESP8266 and Arduino in serial port
int x, y, z;


TMRpcm tmrpcm;

void setup() {
  tmrpcm.speakerPin = 9;
  Serial.begin(9600);
  Wire.begin(); // wake up I2C bus
  // set I/O pins to outputs
  Wire.beginTransmission(0x20);
  Wire.write(0x00); // IODIRA register
  Wire.write(0x00); // set all of port A to outputs
  Wire.endTransmission();
  Wire.beginTransmission(0x20);
  Wire.write(0x01); // IODIRB register
  Wire.write(0x00); // set all of port B to outputs
  Wire.endTransmission();
  if (!SD.begin(SD_ChipSelectPin)) {
    Serial.println("SD fail");
    Wire.begin();
    Wire.beginTransmission(addr); //start talking
    Wire.write(0x02); // Set the Register
    Wire.write(0x00); // Tell the HMC5883 to Continuously Measure
    Wire.endTransmission();

    esp8266.begin(9600);

    sendData("AT+RST\r\n", 2000, DEBUG); //reset module
    sendData("AT+CWMODE=1\r\n", 1000, DEBUG); //set station mode
    sendData("AT+CWJAP=\"Goof_NSA_VERSION\",\"qwertyuiop\"\r\n", 2000, DEBUG); //connect wi-fi network
    while (!esp8266.find("OK")) { //wait for connection
    }
    sendData("AT+CIFSR\r\n", 1000, DEBUG); //show IP address
    sendData("AT+CIPMUX=1\r\n", 1000, DEBUG); //allow multiple connections
    sendData("AT+CIPSERVER=1,1337\r\n", 1000, DEBUG); // start web server on port 1337
    delay(1000);
    return;
  }

  tmrpcm.setVolume(5);
  tmrpcm.play("rain.wav");
}

void loop() {
  getCompass();
  sendWiFi();
  //esp8266.println("AT");
  binaryCount();
  delay(1000);
}


void sendWiFi() {
  if (esp8266.available()) {// check if the esp is sending a message
    while (esp8266.available()) { // The esp has data so display its output to the serial window
      char c = esp8266.read(); // read the next character.
      Serial.write(c);
    }
  }

  if (Serial.available()) {
    delay(1000);

    String dingen = String(String(x) + " " + y + " " + z);
    int lengte = dingen.length();
    String sendS = "AT+CIPSEND=0,";
    esp8266.println(sendS + (lengte + 2));
    delay(200);
    esp8266.println(dingen + "\n");


    //while (Serial.available()) // read the command character by character
    // {
    // read one character
    //  command += (char)Serial.read();
    // }
    //  esp8266.println(command); // send the read character to the esp8266
  }


}

void getCompass() {
  Wire.beginTransmission(addr);
  Wire.write(0x03); //start with register 3.
  Wire.endTransmission();


  //Read the data.. 2 bytes for each axis.. 6 total bytes
  Wire.requestFrom(addr, 6);
  if (6 <= Wire.available()) {
    x = Wire.read() << 8; //MSB  x
    x |= Wire.read(); //LSB  x
    z = Wire.read() << 8; //MSB  z
    z |= Wire.read(); //LSB z
    y = Wire.read() << 8; //MSB y
    y |= Wire.read(); //LSB y
  }
}

String sendData(String command, const int timeout, boolean debug)
{
  String response = "";
  esp8266.print(command);
  long int time = millis();
  while ( (time + timeout) > millis())
  {
    while (esp8266.available())
    {
      char c = esp8266.read();
      response += c;
    }
  }
  if (debug)
  {
    Serial.print(response);
  }
  return response;
}

void binaryCount()
{
  for (byte a = 0; a < 256; a++)
  {
    Wire.beginTransmission(0x20);
    Wire.write(0x12); // GPIOA
    Wire.write(a); // port A
    delay(100);
    Wire.endTransmission();
    Wire.beginTransmission(0x20);
    Wire.write(0x13); // GPIOB
    Wire.write(a); // port B
    delay(100);
    Wire.endTransmission();
  }
}

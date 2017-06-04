#include <Wire.h>

#define compRegAddr 0x1E
#define LEDRegAddr 0x20

int headingDegrees = 0.0;

void setup() {
  Serial.begin(9600);
  Wire.begin(); // wake up I2C bus
  // set I/O pins to outputs
  Wire.beginTransmission(LEDRegAddr);
  Wire.write(0x00); // IODIRA register
  Wire.write(0x00); // set all of port A to outputs
  Wire.endTransmission();

  Wire.beginTransmission(LEDRegAddr);
  Wire.write(0x01); // IODIRB register
  Wire.write(0x00); // set all of port B to outputs
  Wire.endTransmission();
  
  //Put the HMC5883 IC into the correct operating mode
  // Modified ASR to match guidelines in datasheet
  Wire.beginTransmission(compRegAddr);
  Wire.write(0x00);
  Wire.write(0x70); //8-average, 15Hz default, positive self-test measurement
  Wire.beginTransmission(compRegAddr);
  Wire.write(0x01);
  Wire.write(0x01); //Gain = 1
  Wire.endTransmission();
  Wire.beginTransmission(compRegAddr); //open communication with HMC5883
  Wire.write(0x02); //select mode register
  Wire.write(0x00); //continuous measurement mode
  Wire.endTransmission();
}

void loop() {
  getCompVal();
  delay(10);
  LEDPointNorth();
}

void getCompVal() {
  int x, y, z; //triple axis data

  //Tell the HMC5883 where to begin reading data
  Wire.beginTransmission(compRegAddr);
  Wire.write(0x03); //select register 3, X MSB register
  Wire.endTransmission();

  double bearing = 0.00;
  double declinationAngle = -0.5 * PI; // seems to work somehow. just substracted 90 degrees from the bearing.

  //Read data from each axis, 2 registers per axis
  Wire.requestFrom(compRegAddr, 6);
  //  while(Wire.available()<6);
  if (6 <= Wire.available()) {
    x = Wire.read() << 8; //X msb
    x |= Wire.read(); //X lsb
    z = Wire.read() << 8; //Z msb
    z |= Wire.read(); //Z lsb
    y = Wire.read() << 8; //Y msb
    y |= Wire.read(); //Y lsb
    bearing = atan2(y, x);
    bearing += declinationAngle;

    if (bearing < 0){
      bearing += 2 * PI;
    }
    // Check for wrap due to addition of declination.
    if (bearing > 2 * PI){
      bearing -= 2 * PI;//Adjust for local magnetic declination
    }
  }

  headingDegrees = bearing * 180 / PI;

  Serial.println(headingDegrees);
  delay(100);

  Wire.beginTransmission(compRegAddr);
  Wire.write(0x03);
  Wire.endTransmission();
  delay(50);
}

void LEDPointNorth() {
  int LEDNumber = round(headingDegrees / 22.5);
  Serial.println(LEDNumber);

  if (LEDNumber < 8) {
    Wire.beginTransmission(0x20);
    Wire.write(0x13); // GPIOB
    Wire.write(1 << LEDNumber);
    delay(100);
    Wire.write(0);
    Wire.endTransmission();
  }
  else {
    Wire.beginTransmission(0x20);
    Wire.write(0x12); // GPIOA
    Wire.write(1 << (LEDNumber - 8));
    delay(100);
    Wire.write(0);
    Wire.endTransmission();
  }
}


// pins 15~17 to GND, I2C bus address is 0x20
#include "Wire.h"
void setup() {
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
}
void loop() {
  circleLED();
}

void circleLED() {
  int getalA = 1, getalB = 1;

  for (int i = 0; i < 16; i++) {
    if (i < 8) {
      Wire.beginTransmission(0x20);
      Wire.write(0x12); // GPIOA
      Wire.write(getalA); // port A
      delay(100);
      Wire.write(0);
      Wire.endTransmission();
      getalA = getalA * 2;
    }
    else {
      Wire.beginTransmission(0x20);
      Wire.write(0x13); // GPIOB
      Wire.write(getalB); // port B
      delay(100);
      Wire.write(0);
      Wire.endTransmission();
      getalB = getalB * 2;
    }
  }
}

// final code - ver2 : 일반 서보모터 사용
// include webserver, servo, dfplayer 

#include <Servo.h> //서보모터 라이브러리 사용
#include <SoftwareSerial.h> 
#include <DFPlayer_Mini_Mp3.h> // dfplayer 라이브러리 사용

Servo myservo; //서보모터 객체 선언

void setup() {
  Serial.begin(9600); //시리얼 통신 초기화
  Serial.println("Program Ready!");
  Serial.println("");
  myservo.attach(9);  //서보모터 핀번호 9번으로 설정
  mp3_set_serial (Serial);  // DFPlayer-mini mp3 module 시리얼 세팅
  delay(1);  // 볼륨값 적용을 위한 delay
  mp3_set_volume (30);  // 볼륨조절 값 0~30
}

int angle = 20; //각도 변수 선언
char ch; // Serial 입력을 위한 변수 선언
int alarmNum = 3; // sd 카드에 저장된 음성파일의 수

void loop() {
  //20 -> 120도로 서보모터 움직임
  for(angle = 20; angle < 120; angle++)
  {
    myservo.write(angle);
    //Serial.println(angle); //각도 값 출력
    delay(150); //delay로 각도의 변화 속도를 조절
    if (Serial.available()){
      ch = Serial.read(); // 안드로이드로부터 경고 음성 송출 요청을 받은 경우 시리얼에 1 입력
      if(ch == '1') { // 시리얼에 1이 입력되면 경고 음성 송출
        Serial.println("");
        Serial.println("Received Alarm Request!");
        int randNum = random(1, alarmNum+1); // 1~alarmNum 사이의 수 랜덤으로 선택 
        Serial.println(randNum);  
        mp3_play (randNum);  // sd 카드에 저장되어 있는 파일 랜덤으로 재생
        delay(1000);
      }
    }
  }
  //120 -> 20도로 서보모터 움직임
  for (angle = 120; angle > 20; angle--)
  {
    myservo.write(angle);
    //Serial.println(angle); //각도 값 출력
    delay(150); //delay로 각도의 변화 속도를 조절
    if (Serial.available()){
      ch = Serial.read(); // 안드로이드로부터 경고 음성 송출 요청을 받은 경우 시리얼에 1 입력 
      if(ch == '1') { // 시리얼에 1이 입력되면 경고 음성 송출
        Serial.println("");
        Serial.println("Received Alarm Request!");
        int randNum = random(1, alarmNum+1); // 1~alarmNum 사이의 수 랜덤으로 선택 
        Serial.println(randNum);  
        mp3_play (randNum);  // sd 카드에 저장되어 있는 파일 랜덤으로 재생
        delay(1000);
      }
    }
  }
}

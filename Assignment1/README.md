# ElGamalRabin Crypto System

## Build

```
./gradlew jar
```

The jar file will be build on ```build/libs/ElGamalRabin-1.0-SNAPSHOT.jar``` 

## Usage

```
usage: java -jar EGamalRabin.jar <options> <message>
 -kdir <arg>   Keys directory
 -m <arg>      Mode [E encrypt | D decrypt | G generate keys]
Examples:
java -jar ElGamalRabin.jar -kdir key_dir -m G 
java -jar EGamalRabin.jar -kdir key_dir -m E < message.txt 
java -jar EGamalRabin.jar -kdir key_dir -m D < cipher.txt
```


@echo off
echo === Building jar ===
mvn clean package

echo === Creating EXE ===
jpackage ^
  --input target ^
  --name Stationery Shop ^
  --main-jar ecommerce-1.0-SNAPSHOT.jar ^
  --main-class com.stationeryshop.Main ^
  --type exe ^
  --icon icon.ico ^
  --java-options "--add-modules javafx.controls,javafx.fxml" ^
  --module-path "C:\Program Files (x86)\Java FX\javafx-sdk-24.0.1\lib" ^
  --win-dir-chooser ^
  --win-shortcut

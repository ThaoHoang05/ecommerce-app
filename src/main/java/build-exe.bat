@echo off

:: Cấu hình các biến
set JAVAFX_SDK=D:\javafx-sdk-21
set JAR_NAME=ecommerce-1.0-SNAPSHOT.jar
set MAIN_CLASS=com.stationeryshop.App

:: Tạo exe với jpackage
jpackage ^
  --type exe ^
  --name StationeryShop ^
  --input target ^
  --main-jar %JAR_NAME% ^
  --main-class %MAIN_CLASS% ^
  --module-path "%JAVAFX_SDK%\lib" ^
  --add-modules javafx.controls,javafx.fxml ^
  --java-options "--enable-preview"

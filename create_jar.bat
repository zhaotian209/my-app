@echo off
javac -cp "." ApkCheckTool.java
jar cvfe ApkCheckTool.jar ApkCheckTool ApkCheckTool.class
dir ApkCheckTool.*
pause
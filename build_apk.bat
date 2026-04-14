@echo off

:: 映射项目路径到Z:驱动器
subst Z: "%~dp0"

:: 切换到Z:驱动器
Z:

:: 执行构建命令
C:\Gradle\gradle-8.5\bin\gradle.bat assembleDebug

:: 移除映射
subst Z: /D

:: 暂停以便查看构建结果
pause
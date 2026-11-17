@echo off
chcp 65001 >nul
title Satinga Conecta - Modulo PQRS
cd /d "%~dp0"

rem Si Maven no esta en el PATH, intenta con la carpeta donde lo descomprimiste
where mvn >nul 2>nul
if errorlevel 1 set "PATH=%PATH%;C:\maven\apache-maven-3.10.1\bin"

where java >nul 2>nul
if errorlevel 1 ( echo [ERROR] No se encontro Java. Instala el JDK 17 o superior. & pause & exit /b 1 )
where mvn >nul 2>nul
if errorlevel 1 ( echo [ERROR] No se encontro Maven. Revisa que este en C:\maven\apache-maven-3.10.1 & pause & exit /b 1 )

echo.
echo  Compilando y arrancando Tomcat... (la primera vez descarga librerias y tarda unos minutos)
echo  Cuando aparezca "Press Ctrl-C to stop the container", abre en el navegador:
echo.
echo      http://localhost:8080/satinga-pqrs/
echo.
echo  Usuarios: maicol / Satinga2026 (ciudadano)   laura / Funcionario2026 (funcionario)
echo  Para detener el servidor presiona Ctrl+C en esta ventana.
echo.
call mvn package -DskipTests cargo:run
pause

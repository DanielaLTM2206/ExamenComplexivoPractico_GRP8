@echo off
rem ============================================================================
rem  Comercializadora de Celulares + Core BanQuito GR08 - Servidores SOAP (Payara 7, puerto 8080)
rem  Prepara esta computadora para que los clientes de OTRAS computadoras y celulares
rem  (consola, escritorio, web o movil) se conecten a sus servidores:
rem    1. Abre el puerto 8080 en el firewall de Windows (solo la primera vez).
rem    2. Comprueba que MySQL (servicio MySQL80) este en ejecucion.
rem    3. Muestra las IP de esta computadora con las direcciones de los WSDL y del cliente web.
rem    4. Arranca Payara (domain1), que carga WS_BanQuito_SOAP, WS_Comercializadora_SOAP
rem       y Celulares_CliWeb_GR8 ya desplegados desde NetBeans.
rem ============================================================================
chcp 65001 >nul
setlocal
title Celulares GR08 - Servidores SOAP Payara (puerto 8080)

set "PAYARA=C:\payara7"
set "PUERTO=8080"

rem Abrir el puerto en el firewall y arrancar servicios necesita permisos de administrador
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo Solicitando permisos de administrador...
    powershell -NoProfile -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
    exit /b
)

rem 1. Firewall (perfil any: tambien funciona en redes Publicas como un Wi-Fi o un hotspot)
set "REGLA=Celulares GR08 SOAP Payara 8080"
netsh advfirewall firewall show rule name="%REGLA%" >nul 2>&1
if %errorlevel% neq 0 (
    netsh advfirewall firewall add rule name="%REGLA%" dir=in action=allow protocol=TCP localport=%PUERTO% profile=any >nul
    echo [OK] Se abrió el puerto %PUERTO% en el firewall de Windows.
) else (
    echo [OK] El puerto %PUERTO% ya estaba abierto en el firewall de Windows.
)

rem 2. MySQL: los dos servidores SOAP guardan sus datos en MySQL
sc query MySQL80 | find "RUNNING" >nul 2>&1
if %errorlevel% neq 0 (
    echo Arrancando MySQL ^(servicio MySQL80^)...
    net start MySQL80 >nul 2>&1
    sc query MySQL80 | find "RUNNING" >nul 2>&1
    if errorlevel 1 (
        echo [ERROR] No se pudo arrancar MySQL80. Los servidores responderán BDD-001.
    ) else (
        echo [OK] MySQL80 en ejecución.
    )
) else (
    echo [OK] MySQL80 ya estaba en ejecución.
)

rem 3. Direcciones para los clientes
echo.
echo IP de esta computadora. En los clientes de la otra computadora o del celular use
echo "Configurar servidor" (o "Cambiar servidor" en el móvil) y escriba una de estas IP:
powershell -NoProfile -Command "Get-NetIPConfiguration | Where-Object { $_.IPv4DefaultGateway -and $_.NetAdapter.Status -eq 'Up' } | ForEach-Object { '    ' + $_.IPv4Address.IPAddress + '    (' + $_.InterfaceAlias + ')' }"
echo.
echo Para comprobar la conexión, en la otra computadora o en el celular abra en el navegador
echo estas direcciones (cópielas tal cual):
powershell -NoProfile -Command "Get-NetIPConfiguration | Where-Object { $_.IPv4DefaultGateway -and $_.NetAdapter.Status -eq 'Up' } | ForEach-Object { $ip = $_.IPv4Address.IPAddress; '    Comercializadora: http://' + $ip + ':%PUERTO%/WS_Comercializadora_SOAP/WSComercializadora?wsdl'; '    Core BanQuito:    http://' + $ip + ':%PUERTO%/WS_BanQuito_SOAP/WSBanQuito?wsdl'; '    Cliente web:      http://' + $ip + ':%PUERTO%/Celulares_CliWeb_GR8/'; '' }"
echo Ambas computadoras deben estar en la misma red (de preferencia el hotspot de un celular).
echo.

rem 4. Payara
"%PAYARA%\bin\asadmin.bat" list-domains | find "domain1 running" >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] Payara ^(domain1^) ya estaba en ejecución.
) else (
    echo Arrancando Payara ^(domain1^)... puede tardar un minuto.
    call "%PAYARA%\bin\asadmin.bat" start-domain domain1
)
echo.
echo Servidores listos. Para detener Payara: %PAYARA%\bin\asadmin.bat stop-domain domain1
pause

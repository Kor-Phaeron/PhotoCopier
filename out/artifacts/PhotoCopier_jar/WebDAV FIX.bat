@echo off
REG ADD "HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\WebClient\Parameters" /v "FileSizeLimitInBytes" /t REG_DWORD /d 4294967295 /f
sc config webclient start=auto
pause

@echo off

::协议文件路径, 最后不要跟“\”符号
set SOURCE_FOLDER=.\proto

::Java编译器路径
set JAVA_COMPILER_PATH=.\protoc.exe

::Java文件生成路径, 最后不要跟“\”符号
set JAVA_TARGET_PATH=D:\qycache\GameServer\generate

::删除之前创建的文件
del %JAVA_TARGET_PATH%\*.* /f /s /q

for /f "delims=" %%i in ('dir /b %SOURCE_FOLDER%\*.proto') do (
    %JAVA_COMPILER_PATH% --java_out=%JAVA_TARGET_PATH% %%i
)

echo 协议生成完毕。。。

pause
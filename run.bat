@echo off
chcp 65001 > nul
echo Running application...
java -Dfile.encoding=UTF-8 -cp "bin;lib/*" view.FormDangNhap
pause

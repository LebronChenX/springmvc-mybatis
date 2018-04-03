@echo off
echo pre-setver.bat
setlocal ENABLEDELAYEDEXPANSION
if {%1}=={} goto end
if not exist "%1\pre-setver.txt" goto end
rem type %1\pre-setver.txt
set _t=%schedule_description%
set _f=VersionNo.h
set second=%time:~6,2%
for /F "eol=# tokens=1-3 delims=:" %%i in ('type %1\pre-setver.txt') do @(
   if {%%i}=={1} (
      if not {%%k}=={} (
         set _f="%%k"
         if exist "!_f!" (
            del /F /Q "%%k" || exit 1
         )
      ) else (
         call:print_log ^-^-^- Error: Invalid file name.  ^-^-^-
         exit 1
      )
   )
   if {%%i}=={0} (
      if {%%j}=={0} (
         echo %%k>>"!_f!"
      ) else (
         if {!_t!}=={^%schedule_description^%} (
            call:print_log ^-^-^- Error: Undefined schedule_description.  ^-^-^-
         ) else (
            if {%%j}=={!_t!} echo %%k>>"!_f!"
         )
      )
   )
)
goto end

:print_log
      echo %*
   goto :EOF

:end

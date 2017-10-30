#!/bin/bash
#Dont forget to disconnect lan cable, to make rpi connect via wifi
adb shell am startservice -n com.google.wifisetup/.WifiSetupService -a WifiSetupService.Connect -e ssid <your_ssid_escape_space_with_\> -e passphrase <your_wifi_password>

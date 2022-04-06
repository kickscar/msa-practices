#! /bin/bash

SERVER_NAME=service-gateway-server
SCRIPT_DIR=$(cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd)
PID=$(ps -ef | grep java | grep $SERVER_NAME.jar | awk '{print $2}')

if  [ ! -z "$PID" ]
then
	echo "stopping [$SERVER_NAME]"
	kill -9 $PID
	sleep 10
fi

echo "starting [$SERVER_NAME]"
cd $SCRIPT_DIR || exit
nohup java -Dspring.profiles.active=production -jar $SCRIPT_DIR/$SERVER_NAME.jar >> $SCRIPT_DIR/launch.log &
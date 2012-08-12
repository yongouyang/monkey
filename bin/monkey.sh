#!/bin/sh

# Extra environment validation/defaults to run application:

COMMAND=$1
if [ "X$COMMAND" != "Xstart" ] && [ "X$COMMAND" != "Xstop" ]
then
    echo "Command not valid"
    echo "Usage: $0 <[start|stop]> <JMX_PORT>";
    exit 1
fi

export JMX_PORT=$2
if [ "X$JMX_PORT" == "X" ]
then
    echo "JMX port not supplied"
    echo "Usage: $0 <[start|stop]> <JMX_PORT>";
    exit 1
fi

if [ "X$MONKEY_ENV" == "X" ]
then
    echo "Mandatory env variable MONKEY_ENV not set";
    exit 1
fi
echo "MONKEY_ENV=${MONKEY_ENV}"

TODAY=`date '+%Y%m%d'`
LOGFILE=/app/monkey/logs/monkey.$MONKEY_ENV.$TODAY.log
echo "Logging to $LOGFILE"

export JAVA_HOME=${JAVA_HOME:-"/app/jre1.6.0_04"}
if [ -d /app/monkey/java ]
then
    export JAVA_HOME=/app/monkey/java
    export JAVA_BINDIR=/app/monkey/java/bin
    export JAVA_ROOT=/app/monkey/java
fi
echo "JAVA_HOME=${JAVA_HOME}" >>$LOGFILE
echo "JAVA_BINDIR=${JAVA_BINDIR}" >> $LOGFILE
echo "JAVA_ROOT=${JAVA_ROOT}" >> $LOGFILE

export PATH=${JAVA_HOME}/bin:${PATH}

export ENABLE_JMX_AUTH=${ENABLE_JMX_AUTH:-false}
if [ -f /app/monkey/secret/${MONKEY_ENV}.jmxremote.pwd ]
then
    export ENABLE_JMX_AUTH=true;
    echo "JMX Auth is enabled" >>$LOGFILE
else
    export ENABLE_JMX_AUTH=false;
fi

MONKEY_BIN_DIR=`dirname $0`

${MONKEY_BIN_DIR}/wrapper.sh monkey_core $COMMAND >>$LOGFILE 2>&1

${MONKEY_BIN_DIR}/wrapper.sh monkey_core status >>$LOGFILE 2>&1
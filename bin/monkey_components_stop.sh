#! /bin/sh

MONKEY_ENV=$1
HOSTNAME=`hostname`

# Extra environment validation/defaults for Application
if [ "X$MONKEY_ENV" == "X" ]
then
    BASENAME=`basename $0`
    echo "Usage: ${BASENAME} <MONKEY_ENV>";
    exit 1
fi

echo "MONKEY_ENV=${MONKEY_ENV}"
echo "HOSTNAME=${HOSTNAME}"

TODAY=`date '+%Y%m%d'`
LOGFILE=/app/monkey/logs/monkey_components_stop.$MONKEY_ENV.$TODAY.log
echo "Logging to $LOGFILE"

export MONKEY_ENV

MONKEY_BIN_DIR=`dirname $0`

stop_monkey_core () {
    echo "Stopping monkey $MONKEY_ENV"
    JMX_PORT=$1
    ${MONKEY_BIN_DIR}/monkey.sh stop $JMX_PORT >>$LOGFILE 2>&1
}

if [ "${MONKEY_ENV}" == "PRODAPAC" ] && [ "${HOSTNAME}" == "production_host_name" ]
then
    #${MONKEY_BIN_DIR}/monkey_sub_components.sh stop componentName jmxPort
    stop_monkey_core 8686
elif [ "${MONKEY_ENV}" == "UATAPAC" ] && [ "${HOSTNAME}" == "uat_host_name" ]
then
    #${MONKEY_BIN_DIR}/monkey_sub_components.sh stop componentName jmxPort
    stop_monkey_core 8686
elif [ "${MONKEY_ENV}" == "DEVAPAC" ] && [ "${HOSTNAME}" == "monkey-dev-01" ]
then
    stop_monkey_core 8686
else
    echo "Unknown HOSTNAME/MONKEY_ENV combination!"
    exit 1
fi
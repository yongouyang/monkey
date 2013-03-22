#!/bin/bash
# Use this script to start & stop a mongod instance (either as a data node or an arbiter)

app_home=/app/monkey
mongo_home=${app_home}/storage/mongodb
mongo_data_home=${mongo_home}/data
mongod=${mongo_home}/mongodb-distribution/bin/mongod
mongod_pid=`ps -aef | grep ${mongod} | grep -v grep | awk '{print $2}'`
mongod_port=`ps -aef | grep ${mongod} | grep -v grep | awk '{print $14}'`
RETVAL=0

usage() {
    echo "Usage: $0 {start|stop|status} {environmentName} {replicaSetName} {dataNode|arbiter} [port]"
    exit 1
}

# check number of input arguments
if [ $# -lt 1 ]
then
    echo "Not enough command arguments!"
    usage
fi

command="$1"
environmentName="$2"
replicateSetName="$3"
nodeType="$4"
port="$5"

status() {
    if [ ${mongod_pid} > 0 ]; then
          echo "MongoDB is already running on port ${mongod_port} as pid ${mongod_pid}"
    else
          echo "MongoDB is not running"
    fi
    exit 1
}

validateEnvironmentName() {
    if [ -z ${environmentName} ]; then
        echo "Environment name is missing"
        usage
    fi
}

validateReplicaSetName() {
    if [ -z ${replicateSetName} ]; then
        echo "Replica set name is missing"
        usage
    fi
}

validateNodeType() {
    if [ -z ${nodeType} ]; then
        echo "Node type is missing"
        usage
    fi
}

updatePortNumber() {
    if [ -z ${port} ]; then
        # use default port if it is not provided
        port=6646
    fi
}


start() {
    if [ ${mongod_pid} > 0 ]; then
        echo "MongoDB is already running on port ${mongod_port}"
    else
        validateEnvironmentName
        validateReplicaSetName
        validateNodeType
        updatePortNumber

        echo "Starting MongoDB on port ${port}"
        `${mongod} --dbpath ${mongo_data_home}/db --logpath ${mongo_data_home}/log/mongod_${environmentName}_${replicateSetName}.log --port ${port} --fork --logappend`
        RETVAL=$?

    fi
}

stop() {
    if [ ${mongod_pid} > 0  ]; then
        echo "Stopping MongoDB running on port ${mongod_port} as pid ${mongod_pid}"
        `kill -2 ${mongod_pid}`
        echo "Stopped MongoDB running on port ${mongod_port} as pid ${mongod_pid}"
        RETVAL=$?
    else
        echo "MongoDB is not running at all"
    fi
}

case ${command} in
        start)
                start
                ;;
        stop)
                stop
                ;;
        status)
                status
                ;;
        *)
                usage
esac
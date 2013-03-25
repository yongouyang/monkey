#!/bin/bash
# Use this script to start & stop a mongod instance (either as a data node or an arbiter)

app_home=/app/monkey
mongo_home=${app_home}/storage/mongodb
mongo_data_home=${mongo_home}/data
mongod=${mongo_home}/mongodb-distribution/bin/mongod
mongod_pid=`ps -aef | grep ${mongod} | grep -v grep | awk '{print $2}'`
mongod_port=`ps -aef | grep ${mongod} | grep -v grep | awk '{print $14}'`



RETVAL=0

function usage() {
    echo "Usage: $0 start {replicaSetName} {dataNode|arbiter} [port]"
    echo "       $0 stop"
    echo "       $0 status"
    exit 1
}

# check number of input arguments
if [ $# -lt 1 ]
then
    echo "Not enough command arguments!"
    usage
fi

command="$1"
replicaSetName="$2"
nodeType="$3"
port="$4"

function status() {
    if [ ${mongod_pid} > 0 ]; then
          echo "MongoDB is already running on port ${mongod_port} as pid ${mongod_pid}"
    else
          echo "MongoDB is not running"
    fi
    exit 1
}

function validateReplicaSetName() {
    if [ -z ${replicaSetName} ]; then
        echo "Replica set name is missing"
        usage
    fi
}

function validateNodeType() {
    if [ -z ${nodeType} ]; then
        echo "Node type is missing"
        usage
    fi
}

function updatePortNumber() {
    if [ -z ${port} ]; then
        # use default port if it is not provided
        port=6646
    fi
}

function start() {
    if [ ${mongod_pid} > 0 ]; then
        echo "MongoDB is already running on port ${mongod_port}"
    else
        validateReplicaSetName
        validateNodeType
        updatePortNumber

        dataNodeOptions=" --replSet ${replicaSetName} "
        arbiterOptions=" --replSet ${replicaSetName} --oplogSize 1"
        commonStartUpOptions=" ${mongod} --dbpath ${mongo_data_home}/db --logpath ${mongo_data_home}/log/mongod_${replicaSetName}.log --port ${port} --fork --logappend --keyFile ${mongo_home}/keyFile.txt "

        case ${nodeType} in
                dataNode)
                        echo "Starting MongoDB dataNode of replica set ${replicaSetName} on port ${port}"
                        ${commonStartUpOptions} ${dataNodeOptions}
                        RETVAL=$?
                        echo "Started MongoDB dataNode of replica set ${replicaSetName} on port ${port}"
                        ;;
                arbiter)
                        echo "Starting MongoDB arbiter of replica set ${replicaSetName} on port ${port}"
                        ${commonStartUpOptions} ${arbiterOptions}
                        RETVAL=$?
                        echo "Started MongoDB arbiter of replica set ${replicaSetName} on port ${port}"
                        ;;
                *)
                        echo "The nodeType ${nodeType} you specified is not supported!"
                        usage
        esac
    fi
}

function stop() {
    if [ ${mongod_pid} > 0  ]; then
        echo "Stopping MongoDB running on port ${mongod_port} as pid ${mongod_pid}"
        `kill -2 ${mongod_pid}`
        RETVAL=$?
        echo "Stopped MongoDB running on port ${mongod_port} as pid ${mongod_pid}"
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

exit ${RETVAL}
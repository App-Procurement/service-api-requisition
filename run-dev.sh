#!/usr/bin/env bash

CMD=$1
NOHUP=${NOHUP:=$(which nohup)}
PS=${PS:=$(which ps)}

# default java
JAVA_CMD=${JAVA_CMD:=$(which java)}

get_pid() {
    cat "${PROCUREMENT_PID}" 2> /dev/null
}

pid_running() {
    kill -0 $1 2> /dev/null
}

die() {
    echo $*
    exit 1
}

if [ -n "$JAVA_HOME" ]
then
    # try to use $JAVA_HOME
    if [ -x "$JAVA_HOME"/bin/java ]
    then
        JAVA_CMD="$JAVA_HOME"/bin/java
    else
        die "$JAVA_HOME"/bin/java is not executable
    fi
fi


# take variables from environment if set
SERVICE_JAR=${SERVICE_JAR:=target/procurement-0.0.1-SNAPSHOT.jar}

PROCUREMENT_PID=${PROCUREMENT_PID:=procurement_service.pid}
LOG_FILE=${LOG_FILE:=console.log}
#LOG4J=${LOG4J:=}
DEFAULT_JAVA_OPTS="--SERVER_PORT=7050 -Dspring.profiles.active=dev --PSQL_HOST=localhost --PSQL_PORT=5432 --PSQL_DB=procurementdev --PSQL_USER=postgres --PSQL_PSWD=postgres -Djdk.tls.acknowledgeCloseNotify=true -Xms1g -Xmx1g -XX:NewRatio=1 -XX:+ResizeTLAB -XX:+UseConcMarkSweepGC -XX:+CMSConcurrentMTEnabled -XX:+CMSClassUnloadingEnabled -XX:-OmitStackTraceInFastThrow"

if $JAVA_CMD -XX:+PrintFlagsFinal 2>&1 |grep -q UseParNewGC; then
        DEFAULT_JAVA_OPTS="${DEFAULT_JAVA_OPTS} -XX:+UseParNewGC"
fi

JAVA_OPTS="${JAVA_OPTS:="$DEFAULT_JAVA_OPTS"}"

start() {
    echo "Starting procurement service ...${JAVA_CMD}"
    #"${NOHUP}" "${JAVA_CMD}" -jar "${SERVICE_JAR}" ${JAVA_OPTS} >> "${LOG_FILE}" 2>> "${LOG_FILE}" &
        "${NOHUP}" "${JAVA_CMD}" -jar "${SERVICE_JAR}" ${JAVA_OPTS}  >> "${LOG_FILE}" 2>> "${LOG_FILE}" & echo $! > "${PROCUREMENT_PID}"
        #"${NOHUP}" "${JAVA_CMD}" -jar "${SERVICE_JAR}" ${JAVA_OPTS} > /dev/null 2>&1 & echo $! > "${PROCUREMENT_PID}"
}

run() {
    echo "Running procurement service ..."
    exec "${JAVA_CMD}" -jar "${SERVICE_JAR}" ${JAVA_OPTS}  >> "${LOG_FILE}" 2>> "${LOG_FILE}" & echo $! > "${PROCUREMENT_PID}"
}

stop() {
    if [ ! -f "${PROCUREMENT_PID}" ]; then
      die "Not stopping. PID file not found: ${PROCUREMENT_PID}"
    fi

    PID=$(get_pid)

    echo "Stopping procurement service ($PID) ..."
    echo "Waiting for procurement service to halt."

    kill $PID

    while "$PS" -p $PID > /dev/null; do sleep 1; done;
    rm -f "${PROCUREMENT_PID}"

    echo "procurement service stopped"
}

restart() {
    echo "Restarting procurement service ..."
    stop
    start
}

status() {
    PID=$(get_pid)
    if [ ! -z $PID ]; then
        if pid_running $PID; then
            echo "procurement service running with PID ${PID}"
            return 0
        else
            rm "${PROCUREMENT_PID}"
            die "Removed stale PID file ${PROCUREMENT_PID} with ${PID}."
        fi
    fi

    die "procurement service not running"
}

case "$CMD" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    run)
        run
        ;;
    *)
        echo "Usage $0 {start|stop|restart|status|run}"
esac

#!/bin/bash

# ==========================
# global variables
# ==========================

# ansi colors
R='\033[0;31m'
G='\033[0;32m'
O='\033[0;33m'
CY='\033[0;36m'
LG='\033[0;37m'
NC='\033[0;0m'

# defaults, etc.
NUM_PATTERN='^[0-9]+$'
TIME_TO_EXECUTE=5
FILE_DESCRIPTION="unknownIssue"
BASE_OUTPUT_DIR="/foo/bar/flight_recordings"
HOST_NAME="$(hostname -s)"
MAIN_CLASS="MainClass"
PID=`(jps | grep -i "${MAIN_CLASS}" | awk '{print $1}')`
JFR_PROFILE_NAME="exceptionProfile"
JFR_PID=""

validateExpectedOutputDirectory() {
    if ! [ -d ${BASE_OUTPUT_DIR} ]; then
        printf "${R}Unable to execute a fava flight recording due to the following issue:${NC}\n"
        printf "${R}The base output directory ${BASE_OUTPUT_DIR} does not appear to exist on host ${HOST_NAME}!${NC}\n"
        exit -1
    fi
}

validateRunningProcess() {
    if ! [ -z ${PID} ]; then
        printf "${CY}Found running process for main class ${MAIN_CLASS} with PID: ${PID}$ ...${NC}\n"
    else
        printf "${R}Unable to execute a fava flight recording due to the following issue:${NC}\n"
        printf "${R}The following main process does not appear to running on host ${HOST_NAME}: ${MAIN_CLASS}!${NC}\n"
        exit -1
    fi
}

validateExpectedJfrProfile() {
    if ! [ -f "${JAVA_HOME?:missing ENV:JAVA_HOME}/jre/lib/jfr/${JFR_PROFILE_NAME}.jfc" ]; then
        printf "${R}Unable to execute a fava flight recording due to the following issue:${NC}\n"
        printf "${R}The JFR profile ${JFR_PROFILE_NAME} does not appear to exist on host ${HOST_NAME}!${NC}\n"
        exit -1
    fi    
}

bashtrap() {
    printf "\n${R}CTRL+C: stopping script; flight flight recording may or may not exist at ${FILE_PATH} ...${NC}\n"
    exit 0
}

displayProgress() {
    if [ $# -ne 1 ]; then
        printf "${R}Invalid number of args supplied to ${FUNCNAME[0]}; it requires 1 numeric argument!${NC}\n"
        exit -1
    fi
    if ! [[ "$1" =~ ${NUM_PATTERN} ]]; then
        printf "${R}Invalid input value supplied to ${FUNCNAME[0]}; it needs to be a number representing minutes!${NC}\n"
        exit -1
    fi
    
    TIME_TO_WAIT_IN_MINS=$1
    TIME_TO_WAIT_IN_SECS=`expr ${TIME_IN_MINS} \* 60`
    END=$((${SECONDS} + ${TIME_TO_WAIT_IN_SECS}))
    while [ ${SECONDS} -lt ${END} ]; do
        echo -n "."; sleep 1
    done
    
    printf "${G}\n${TIME_TO_WAIT_IN_MINS} minutes have elapsed and the flight recording collection should have completed, but it might be in persistence mode!${NC}\n"
}

runFlightRecording() {
    if [[ $# -ne 1 ]]; then
        printf "${R}Invalid number of args supplied to ${FUNCNAME[0]}; it requires 1 numeric argument!${NC}\n"
        exit -1
    fi
    if ! [[ "$1" =~ ${NUM_PATTERN} ]]; then
        printf "${R}Invalid input value supplied to ${FUNCNAME[0]}; it needs to be a number representing minutes!${NC}\n"
        exit -1
    fi
    
    TIME_IN_MINS=$1
    TIME_IN_SECS=`expr ${TIME_IN_MINS} \* 60`
    
    printf "${CY}Staring JFR ${FILE_PATH} for ${TIME_IN_MINS} minutes worth of statistics (press CTRL-C to exit) ...${NC}\n"
    
    # run in the background so we can display the progress
#   set -x
    jcmd ${PID} JFR.start duration=${TIME_IN_SECS}s filename=${FILE_PATH} settings=${JFR_PROFILE_NAME} & 
#   set +x

    JFR_PID=$!
    printf "${CY}JFR PID: ${JFR_PID}${NC}\n"
}

waitForJfrToComplete() {
    # ensure the pid exists (not actually checking to see if it is running)
     if ! [ -z ${JFR_PID} ]; then
        printf "${R}The JFR process with PID ${JFR_PID} is not currently running and may have already completed; check ${FILE_PATH}!${NC}\n"
     fi
     
     printf "${CY}JFR process with PID ${JFR_PID} is running and might still be writing to disk; press CTRL-C if exit doesn't happen shortly!${NC}\n"
     
     # wait for file to exist and be non-empty
     while ! [ -e ${FILE_PATH} ]; do
        echo -n "."; sleep 1
     done
     
     printf "${G}${FILE_PATH} exists and is non-empty; waiting for FILE IO to complete ...${NC}\n"
     
     # wait for process to finish (this may not be the most efficient way)
     while (ps -ef | grep "${JFR_PID:?-1}" | grep -v grep >/dev/null 2>&1); do
        echo -n "."; sleep 1
     done
     
      printf "${G}FILE IO for JFR at ${FILE_PATH} has completed!${NC}\n"
}

parseInputArguments() {
    OPTION_PATTERN='^[-]{1,2}[t|d|p].*$'
    
    while test $# -gt 0; do
        case "$1" in
            -h|--help)
                 printf "${G}Usage: $0 [-h|--help] [-t|--time #] [-d|--description value] [-p|--profile value]${NC}\n"
                 printf "\t${G}-h or --help\t\tshow this message${NC}\n"
                 printf "\t${G}-t or --time\t\tJFR execution time in minutes, default=${TIME_TO_EXECUTE}${NC}\n"
                 printf "\t${G}-d or --description\t\tProvide a descript to the file name, default=${FILE_DESCRIPTION}${NC}\n"
                 printf "\t${G}-p or --profile\t\tProvide a JFR profile, default=${JFR_PROFILE_NAME}${NC}\n"
                 printf "\t${G}${NC}\n"
                 exit 0
            ;;
            
            -t|--time)
                if [[ "$2" =~ ${OPTION_PATTERN} ]]; then
                    shift
                else
                    if ! [[ "$2" =~ ${OPTION_PATTERN} ]] && [[ "$2" =~ ${NUM_PATTERN} ]]; then
                        TIME_TO_EXECUTE=$2; shift
                    else
                        printf "${R}Error parsing time:${NC} ${LG}$2${NC}, falling back to default ${LG}${TIME_TO_EXECUTE}${NC} minutes\n"  
                    fi
                fi
            ;;
            
            -d|--description)
                if [[ "$2" =~ ${OPTION_PATTERN} ]]; then
                    shift
                else
                    if ! [ -z "$2" ]; then
                        FILE_DESCRIPTION=$2; shift
                    else
                        printf "${R}Error parsing description:${NC} ${LG}$2${NC}, falling back to default ${LG}${FILE_DESCRIPTION}${NC}\n"  
                    fi
                fi           
            ;;
            
            -p|--profile)
                if [[ "$2" =~ ${OPTION_PATTERN} ]]; then
                    shift
                else
                    if ! [ -z "$2" ]; then
                        JFR_PROFILE_NAME=$2; shift
                    else
                        printf "${R}Error parsing JFR profile:${NC} ${LG}$2${NC}, falling back to default ${LG}${JFR_PROFILE_NAME}${NC}\n"  
                    fi
                fi             
            ;; 
            
            *)
                printf "${R}Unknown option received:${NC} ${LG}$1${NC}, falling back to default!\n"
                shift
            ;;
        esac
    done
    
    printf "${CY}Using JFR profile: ${JFR_PROFILE_NAME}${NC}\n"
}

setFileNameAndPath() {
    DELIMITER="_"
    FILE_DATE=`date '+%Y%m%d-%H%M%S'`
    FILE_NAME="${HOST_NAME}${DELIMITER}${FILE_DESCRIPTION}${DELIMITER}${FILE_DATE}.jfr"
    FILE_PATH="${BASE_OUTPUT_DIR}/${FILE_NAME}"
}

# ==========================
# main
# ==========================

umask 022
trap bashtrap INT

parseInputArguments $*
validateExpectedOutputDirectory
validateRunningProcess
validateExpectedJfrProfile
setFileNameAndPath
runFlightRecording ${TIME_TO_EXECUTE}
displayProgress ${TIME_TO_EXECUTE}
waitForJfrToComplete

#!/bin/bash

# =======================
# global functions
# =======================

getDate() {
    if [ $# -ne 2 ]; then
        printf "${R}Invalid number of args supplied to ${FUNCTNAME[0]}; it requires 1 numeric argument for days ago and one date format string!${NC}\n"
        exit -1
    fi
    if ! [[ "$1" =~ ${NUM_PATTERN} ]]; then
        printf "${R}Invalid input value supplied to ${FUNCTNAME[0]}; it needs to be a number representing minutes!${NC}\n"
        exit -1
    fi
    
    DAYS_AGO=$1
    DATE_FORMAT=$2
    DATE_RESULT=`date -d "${DAYS_AGO} day ago" "+${DATE_FORMAT}"`
    echo $DATE_RESULT
}

# =======================
# global variables
# =======================

# ansi colors
R='\033[0;31m'
G='\033[0;32m'
O='\033[0;33m'
CY='\033[0;36m'
LG='\033[0;37m'
NC='\033[0;0m'

# globals
BASE_DIR="${HOME:=/users/analytics}"
LOG_DIR_NAME="logs"
NUM_PATTERN='^[0-9]+$'

# date stuff
YEAR=`getDate 0 '%Y'`
MONTH=`getDate 0 '%m'`
TODAY=`getDate 0 '%s'`
DAY=`getDate 0 '%a'`
HOUR=`getDate 0 '%H'`
YESTERDAY=`getDate 1 '%d'`

# =======================
# other functions
# =======================

verifyDirectory() {
    if [ $# -ne 1 ]; then
        printf "${R}Invalid number of args supplied to ${FUNCTNAME[0]}; it requires 1 numeric argument!${NC}\n"
        exit -1
    fi
    if [[ -d "$1" ]]; then
        printf "${R}Unable to verify analytic because the following directory does not exist: $1!${NC}\n"
        return 1
        exit -1
    fi    
    return 0
}

verifyFile() {
    if [ $# -ne 1 ]; then
        printf "${R}Invalid number of args supplied to ${FUNCTNAME[0]}; it requires 1 numeric argument!${NC}\n"
        exit -1
    fi
    if [[ -f "$1" ]]; then
        printf "${R}Unable to verify analytic because the following file does not exist: $1!${NC}\n"
        return 1
        exit -1
    fi    
    return 0
}

addLine() {
    printf "${O}---------------------------------------------------------------------------------------${NC}\n"
}

verifyCm() {
    printf "${G}Verifying ${CM_APP_NAME} ...${NC}\n"
    verifyDiretory "${CM_HOME}"
    verifyFile "${CM_LOG_FILE}"
    if [ "$?" -eq "0" ]; then
        printf "${G}Showing 'completed' lines in log file:  ${CM_LOG_FILE}${NC}\n"
        grep "completed" ${CM_LOG_FILE}
        
        case "${DAY}" in
            Mon)
                CM_PRE_LOG_DAYS=3
                CM_PRE_DAYS=4
                CM_CUR_DAYS=1
            ;;
            Tue)
                CM_PRE_LOG_DAYS=4
                CM_PRE_DAYS=5
                CM_CUR_DAYS=2            
            ;;
            Wed|Fri)
                CM_PRE_LOG_DAYS=2
                CM_PRE_DAYS=3
                CM_CUR_DAYS=1            
            ;;
            Thu)
                CM_PRE_LOG_DAYS=3
                CM_PRE_DAYS=4
                CM_CUR_DAYS=2            
            ;;
        esac
        
        CM_PREVIOUS_LOG="${CM_LOG_FILE}.$(getDate ${CM_PRE_LOG_DAYS} '%Y%m%d')"
        printf "${G}Showing 'completed' lines for previous log file:  ${CM_PREVIOUS_LOG}${NC}\n"
        grep "completed" ${CM_PREVIOUS_LOG}
        
        CM_PREVIOUS_HDFS_DIR="${CM_HDFS_IN_DIR}/$(getDate ${CM_PRE_DAYS} '%Y')/$(getDate ${CM_PRE_DAYS} '%m'/$(getDate ${CM_PRE_DAYS} '%d'))"
        printf "${G}Retrieving HDFS directory size for:  ${CM_PREVIOUS_HDFS_DIR} ...${NC}\n"
        hdfs dfs -ls "${CM_PREVIOUS_HDFS_DIR}" | wc -l
        
        CM_CURRENT_HDFS_DIR="${CM_HDFS_IN_DIR}/$(getDate ${CM_CUR_DAYS} '%Y')/$(getDate ${CM_CUR_DAYS} '%m'/$(getDate ${CM_CUR_DAYS} '%d'))"
        printf "${G}Retrieving HDFS directory size for:  ${CM_CURRENT_HDFS_DIR} ...${NC}\n"
        hdfs dfs -ls "${CM_CURRENT_HDFS_DIR}" | wc -l    
    fi
}

verifyRf() {
    printf "${G}Verifying RF ...${NC}\n"
    hdfs fds -ls -R "/foo/bar" | awk '$5 > 0'
}

showCurrentlyRunningYarnProcesses() {
    printf "${G}Showing currently running yarn processes ...${NC}\n"
    yarn application -list | awk 'NR > 2' | awk -F '\t' 'gsub(/[ \t]+/,"", $6); printf "%-3d $-10s %-35s %s\n", NR, $6, $1, $2 }'
}

# =======================
# main
# =======================

printf "${G}Starting verification of analytics for day [${DAY}] ...${NC}\n"
addLine
verifyCm
addLine
verifyRf
addLine
showCurrentlyRunningYarnProcesses
addLine
printf "${G}Finished!${NC}\n"

# .bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# =====================================
# color 
# =====================================

red='\[\e[0;91m\]'
RED='\[\e[0;31m\]'
blue='\[\e[0;94m\]'
BLUE='\[\e[0;34m\]'
darkblue='\[\e[0;34m\]'
black='\[\e[0;90m\]'
BLACK='\[\e[0;30m\]'
magenta='\[\e[0;95m\]'
MAGENTA='\[\e[0;35m\]'
cyan='\[\e[0;96m\]'
CYAN='\[\e[0;36m\]'
CLEAR='\[\e[0;0m\]'

export LS_COLORS='ex=00;31:ln=01;39:di=01;34:fi=00;36'
for ext in 7z bz bz2 deb gz jar tar tbz tgz war zip; do LS_COLORS="$LS_COLORS:*.$ext=01;35"; done
for ext in cmd groovy html ini java js json md properties scripts txt xml; do LS_COLORS="$LS_COLORS:*.$ext=90"; done
for ext in ico gif jpg png svg; do LS_COLORS="$LS_COLORS:*.$ext=00;33"; done
for type in pi cd bd so; do LS_COLORS="$LS_COLORS:*.$ext=00;46;37"; done

# =====================================
# prompt / history
# =====================================

export PS1="\n${red}\[\u\]${darkblue}@${black}\[\h\] ${BLACK}\$(pwd)${CLEAR}\n${BLACK}\$ ${BLACK}"
export HISTCONTROL=ignoreboth
export HISTIGNORE=$'[ \t]*:&:[fb]g:exit:ls'

#########
# LINUX #
#########
#PS1="[\u:\w]\$ "
#LS_COLORS='di=01;34:fi=00:ln=36:pi=40;37:so=43:bd=45:cd=47:or=100;33:mi=100;31:ex=91'
#for ext in rpm gz tar jar zip; do LS_COLORS="$LS_COLORS:*.${ext}=35;01"; done
#HISTIGNORE="&:exit:ls:lo:x:y:history"

###############
# ENVIRONMENT #
###############
HOME_DIR=/home/tjjenk2
APPS_DIR=$HOME_DIR/apps
SPRING_HOME=$APPS_DIR/springcli-1.4.3.RELEASE
export DATA_DIR=$HOME_DIR/data
export CATALINA_HOME=$APPS_DIR/tomcat-8.0.30
#export JAVA_HOME=$APPS_DIR/jdk1.7.0_79
#export JAVA_HOME=$APPS_DIR/jdk1.8.0_121
export JAVA_HOME=$APPS_DIR/jdk1.8.0_172
#export JAVA_HOME=$APPS_DIR/jdk-9.0.1
export MAVEN_HOME=$APPS_DIR/maven-3.5.2
export GRADLE_HOME=$APPS_DIR/gradle-4.4
export GRAILS_HOME=$APPS_DIR/grails-2.4.3
export GROOVY_HOME=$APPS_DIR/groovy-2.4.6
export SCALA_HOME=$APPS_DIR/scala-2.12.2
export FEZZIK_HOME=$HOME_DIR/fezzik
export SPARK_HOME=$APPS_DIR/spark-2.2.0-hadoop2.7

########
# PATH #
########

pathmunge () {
   if ! echo $PATH | /bin/egrep -q "(^|:)$1($|:)" ; then
      if [ "$2" = "after" ] ; then
         PATH=$PATH:$1
      else
         PATH=$1:$PATH
      fi
   fi
}

function path() {
   old=$IFS
   IFS=:
   printf "%s\n" $PATH
   IFS=$old
}

#pathmunge $GRAILS_HOME/bin
pathmunge $GROOVY_HOME/bin
pathmunge $SPRING_HOME/bin
pathmunge $APPS_DIR/node-v10.6.0/bin
pathmunge $APPS_DIR/nodenpm/bin
pathmunge $SPARK_HOME/bin
pathmunge $SCALA_HOME/bin
pathmunge $GRADLE_HOME/bin
pathmunge $MAVEN_HOME/bin
pathmunge $APPS_DIR/bin
pathmunge $JAVA_HOME/bin
pathmunge /usr/local/bin
pathmunge .

export path

###########
# ALIASES #
###########

# personal
alias apps='cd $APPS_DIR'
alias notes='cd $HOME_DIR/notes'
alias dls='cd $HOME_DIR/Downloads'
 
# linux
alias bashit='vim ~/.bashrc'
alias srcit='source ~/.bashrc'
alias latr='ls -latr'
alias l.='ls -d .*' 
alias grep='grep --color'
alias egrep='egrep --color=auto'
alias fgrep='fgrep --color=auto'
alias rgrep='find . -name "*" -print | xargs grep \!*'
alias latr='ls -latr'
alias l='ls -a'
alias bashit='vim ~/.bashrc'
alias addrsa='ssh-add /home/tjjenk2/.ssh/id_rsa'
alias cls='clear && echo -en "\e[3J"'
alias goroot='sudo su -s - root'
alias showsize='du -h -d 1 -c | sort -h'
alias f='find . -name "\!*" -print'
alias files="find . -type f -print | grep -v '~'"
alias psg="ps -ef | grep \!* | more"

# work
alias work="cd $HOME_DIR/data/"
alias iwork="cd $HOME_DIR/data/intellij_workspace"

# editors
alias runbrackets="$APPS_DIR/brackets/brackets &"
alias runeclipse="$APPS_DIR/eclipse/eclipse &"
alias runidea="sh $APPS_DIR/intellij-15.0.3/bin/idea.sh &"
alias runkomodo="$APPS_DIR/komodo-9/bin/komodo &"
alias runsublime="$APPS_DIR/sublime_text_3/sublime_text &"

# tomcat
alias gotomcat="cd $CATALINA_HOME/bin"
alias gotomcatlogs="cd $CATALINA_HOME/logs"

# mongo
alias startmongo="sudo service mongod start"
alias stopmongo="sudo service mongod stop"
alias gomongologs="cd /var/log/mongodb"

# docker mongo alias
alias docmongostart="docker run --name fezzik-mongo -p 27017:27017 -v ~/dev/data/dir:/data/db -d mongo"
alias docmongostop="docker stop fezzik-mongo"
alias docmongoadmin="docker exec -it fezzik-mongo mongo admin"
alias docmongoremove="docker rm fezzik-mongo"
alias doctomcatstart="docker rm -f fezzik-rest;docker run --name fezzik-rest -p 8443:8443 -p 9443:9443 -e FEZZIK_HOME=/tmp -v $FEZZIK_HOME:/tmp --link fezzik-mongo:mongo -d ets/fezzik-rest;docker ps"

# other
alias watchhttp="sh $APPS_DIR/ZAP_2.4.3/zap.sh &"

# python
alias python="python3.8"
alias pip="pip3.8"
alias jup='jt -t onedork -f hack -fs 14 -nfs 14 -nf robotosans -T -tfs 14 -ofs 14 -cellw 85% -altp'

# git
alias s="git status"
alias gitpass="git config credential.helper store"

# Uncomment the following line if you don't like systemctl's auto-paging feature:
# export SYSTEMD_PAGER=

unset pathmunge

#THIS MUST BE AT THE END OF THE FILE FOR SDKMAN TO WORK!!!
#export SDKMAN_DIR="/home/tjjenk2/.sdkman"
#[[ -s "/home/tjjenk2/.sdkman/bin/sdkman-init.sh" ]] && source "/home/tjjenk2/.sdkman/bin/sdkman-init.sh"

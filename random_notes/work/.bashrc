# .bashrc

# source global definitions
if [ -f /etc/bashrc ] ; then
    . /etc/bashrc
fi

#eval "$(ssh-agent)" > /dev/null
#ssh-add

# =====================================
# color 
# =====================================

red='\[\e[0;91m\]'
RED='\[\e[1;91m\]'
blue='\[\e[0;94m\]'
BLUE='\[\e[1;94m\]'
darkblue='\[\033[0;34m\]'
black='\[\e[0;30m\]' # or '\[\033[0;30m\]'
BLACK='\[\e[1;39m\]' # or '\[\033[0;39m\]'
magenta='\[\e[0;95m\]'
MAGENTA='\[\e[1;95m\]'
cyan='\[\e[0;96m\]'
CYAN='\[\e[1;96m\]'
CLEAR='\[\033[0m\]'

# =====================================
# prompt 
# =====================================

HOSTIP=$(ip addr show dev eth0 | grep 'inet ' | cut -d" "-f6 | cut -d/ -f1)
export PS1="\n${red}\[\u\]${darkblue}@${black}\[\h\][${HOSTIP}] ${BLACK}\$(pwd)${CLEAR}\n${BLACK}\$ ${black}"

# =====================================
# environment 
# =====================================

# personal
MY_HOME=/home/tjjenk2

# curl NSSDB
export SSL_DIR=/home/tjjenk2/.pki/nssdb
export NSS_DEFAULT_DB_TYPE=sql

# maven
export SSL_JAVA_OPTS="$TRUSTSTORE $TRUSTSTORE_PASS $TRUSTSTORE_TYPE $KEYSTORE $KEYSTORE_PASS $KEYSTORE_TYPE"
export MAVEN_OPTS="-Xms128m -Xmx2048m $SSL_JAVA_OPTS"
export M2_OPTS=${MAVEN_OPTS}

# =====================================
# path 
# =====================================

echoLine() {
    echo "---------------------------------------------------------------------------------"
}

# function to assist in path setting (it isn't perfect)
pathmunge() {
    if ! echo $PATH | /bin/egrep -q "(^|:)$1($|:)" ; then
        if [ "$2" = "after" ] ; then
            PATH=$PATH:$1
        else
            PATH=$1:$PATH
        fi
    fi
}

path() {
    echoLine
    printf "Current Shell Path:\n--------------------\n"
    old=$IFS
    IFS=:
    printf "%s\n" $PATH
    IFS=$old
    echoLine
}

# path

pathmunge $JAVA_HOME/bin
pathmunge .

# =====================================
# misc 
# =====================================

export HISTCONTROL=ignoreboth
export HISTIGNORE=$'[ \t]*:&:[fb]g:exit:ls'

export LS_COLORS='ex=00;31:ln=01;39:di=01;34:fi=00;36'
for ext in 7z bz bz2 deb gz jar tar tbz tgz war zip; do LS_COLORS="$LS_COLORS:*.$ext=01;35"; done
for ext in cmd groovy html ini java js json md properties scripts txt xml; do LS_COLORS="$LS_COLORS:*.$ext=90"; done
for ext in ico gif jpg png svg; do LS_COLORS="$LS_COLORS:*.$ext=00;33"; done
for type in pi cd bd so; do LS_COLORS="$LS_COLORS:*.$ext=00;46;37"; done

# turn off sount
#xset b off

# =====================================
# alias 
# =====================================

# linux

alias grep='grep --color'
alias egrep='egrep --color=auto'
alias fgrep='fgrep --color=auto'
alias rgrep='find . -name "*" -print | xargs grep \!*'
alias latr='ls -latr'
alias l='ls -a'
alias bashit='vim ~/.bashrc'
alias addrsa='ssh-add /home/tjjenk2/.ssh/id_rsa'
alias cls='clear &&echo -en "\e[3J"'
alias goroot='sudo su -s - root'
alias showsize='du -h -d 1 -c | sort -h'
alias f='find . -name "\!*" -print'
alias files="find . -type f -print | grep -v '~'"
alias psg="ps -ef | grep \!* | more"

# git

alias gitwho="git for-each-ref --format='%(authorname) %09 %(refname)' --sort=authorname"
alias s='git status'

# tunnels
alias tunnel_es2='ssh -v -N -L 9200:master.host:9200 tjjenk2@master.host'
alias tunnel_webscv_debug='ssh -v -N -L 8443:web.host:7443 tjjenk2@web.host'
alias tunnel_setup="ssh -f -N -D 5055 host1 & ssh -f -N -D 5056 host2 & ssh -F -N -D 5057 host3"
alias tunnel_es6_foxyproxy="ssh -v -N -L 9250:es1:9250 tjjenk2@es1"
alias tnnel_sql="ssh -v -N -L 3306:mysql:3306 tjjenk2@mysql"

# npm
alias npmll='npm list --depth=0'

# =====================================
# cleanup 
# =====================================

unset pathmunge

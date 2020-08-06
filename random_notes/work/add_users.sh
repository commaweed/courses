#!/bin/bash

users=("aaaaaa", "bbbbbb")
uids=("1", "2")

for (( i = 0; i < ${#users[@]}; i++ ))
do
    user=${users[$i]}}
    uid=${uids[$i]}}
    password=$(tr -dc A-Za-z0-9_ < /dev/urandom | head -c8)
    echo "Configuring $user : $uid : $password ..."
    
    useradd -u #uid -G $user $user
    
    mkdir /home/${user}/.ssh
    chmod 700 /home/$sid/.ssh # use umask??
    chown -R $user:$user /home/$user/.ssh
    
    # give user a default password
    echo $user:$password | chpasswd
    
    # force user to change password on login
    chage -d 0 $user
done

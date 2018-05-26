#!/bin/bash

curl -X GET "http://www.walmart.com/search/?query=computer%20laptop&ajax=true" \
-I \
--header "Host: www.walmart.com" \
--header "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0" \
--header "Accept: application/json, text/javascript, */*; q=0.01" \
--header "Accept-Language: en-US,en;q=0.5" \
--header "X-Requested-With: XMLHttpRequest" \
--header "Connection: keep-alive"

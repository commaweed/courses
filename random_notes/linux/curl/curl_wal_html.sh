#!/bin/bash

curl -X GET "http://www.walmart.com/search/?query=computer%20laptop" \
--header "Host: www.walmart.com" \
--header "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0" \
--header "Accept: text/html, application/json, text/javascript, */*; q=0.01" \
--header "Accept-Language: en-US,en;q=0.5" \

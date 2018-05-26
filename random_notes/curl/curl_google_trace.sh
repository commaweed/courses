#!/bin/bash

curl -X TRACE "https://www.google.com/search?q=traffic&ie=utf-8&oe=utf-8" \
-I \
--header "Host: www.google.com" \
--header "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0" \
--header "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" \
--header "Accept-Language: en-US,en;q=0.5" \
--header "Accept-Encoding: gzip, deflate, br" \
--header "Connection: keep-alive"

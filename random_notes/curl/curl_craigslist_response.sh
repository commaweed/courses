#!/bin/bash 

#set -x

curl -X GET "http://baltimore.craigslist.org" \
-I \
--header "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0" \
--header "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" \
--header "Accept-Language: en-US,en;q=0.5" \
--header "Accept-Encoding: gzip, deflate" \
--header "Cookie: cl_def_hp=baltimore" \
--header "Connection: keep-alive" \
--header "If-Modified-Since: Sun, 13 Mar 2016 03:15:10 GMT" \
--header "Cache-Control: max-age=0" 

#set +x


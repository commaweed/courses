MONGO_CMD=/usr/bin/mongo
#USER=blah
#PWD=blah
DATABASE=db_name
SSL_OPTIONS='--ssl --sslAllowInvalidHostNames --sslPEMKeyFile /blah/blah.pem --sslCAFile /blah/blah.crt'
DEV=x.x.x.x
STG=x.x.x.x
PROD=x.x.x.x

ERROR_MSG="Please supply the environment (dev, stg, prod)! (e.g. $0 dev)"
CONNECTION_STR="Connection to $1 shell..."

if [ $# -eq 1 ]
then
   #set -x #echo on

   case "$1" in
      dev)
         SERVER=$DEV
      ;;
      stg)
         SERVER=$STG
      ;;
      prod)
         SERVER=$PROD
      ;;
   esac

   if [ -n SERVER ]; then
      #set -x #echo on
      echo "$CONNECTION_STR : $SERVER"
      #echo "$MONGO_CMD -u $USER -p --shell $SSL_OPTIONS $SERVER/$DATABASE"
      #eval $MONGO_CMD -u $USER -p $PASSWORD --shell $SSL_OPTIONS $SERVER/$DATABASE
      echo "Connection with... $MONGO_CMD --shell $SSL_OPTIONS $SERVER/$DATABASE"
      eval $MONGO_CMD --shell $SSL_OPTIONS $SERVER/$DATABASE
   else
      echo $ERROR_MSG
   fi
else
   echo $ERROR_MSG
fi


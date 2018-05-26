.mongorc.js

//db.getSiblingsDB("db_name");
db.getSiblingsDB("$external").auth({mechanism:"MONGODB-X509",user:"CN="});

// set the editor
EDITOR="VIM"

// always cause return pretty-printing
DBQuery.prototype._prettyShell=true;

// modify the size of the cursor batch (20 is the default)
//DBQuery.shellBatchSize = 100;

load("support/underscore.js");
load("support/mongo_hacker.js");
load("support/tj_utils.js");

// change the prompt
(function() {
   var lineCounter = 0;
   var masterInfo = db.isMaster();
   print(tojson(masterInfo));

   if (masterInfo.setName) {
      prompt = function () {
         return db._name +":" + masterInfo.setName + ":" + (masterInfo.ismaster === true ? "PRIMARY" : "SECONDARD") + ">";
      }
   } else {
      prompt = function() {
          return db._name + ">";
      };
   }
})();

myshell.sh

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

tj_utils.js

var tj={};
(function(tj) {
   function blah() {}

   _.extend(tj, {
       'blah': blah
})(tj);

FavoritesDao.java

@Repository("x-web-ref.FavoritesDao")
public interface FavoritesDao extends
   MongoRepository<Favorite, String>, FavoritesDaoCustom, PagingAndSortingRepository<Favorite, String>
{
   @Query(
       value="{" + SubjectFavorite.FIELD_NAME_SUBJECT + ": ?0" + ", " + blah + ": ?1" + ", " + blah3 + ": ?2 }", delete=true)
      void deleteSubjectFavoriteForUser(String subject, String parentValue, String user);

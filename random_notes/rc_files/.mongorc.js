// set to default database
db = db.getSiblingDB("fezzik");

// set up SSL auth (not used for now)
//db.getSiblingsDB("$external").auth({mechanism:"MONGODB-X509",user:"CN="});

// set the editor
EDITOR="VIM"

// always cause return pretty-printing
DBQuery.prototype._prettyShell=true;

// modify the size of the cursor batch (20 is the default)
//DBQuery.shellBatchSize = 100;

load("/home/tjjenk2/scripts/mongo/support/underscore-min.js");
load("/home/tjjenk2/scripts/mongo/support/mongo_hacker.js");
load("/home/tjjenk2/scripts/mongo/support/tj_utils.js");

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

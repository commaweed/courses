db.auth('user', 'password');

db.getCollectionNames().filter(funct (c) { return .indexOf('filter_here') > 0; });

db.queries.find( { "identity.uid":"tjjenk2", flows: } $elemMatch: {status: "COMPLETED" }}}, { "flows.status":1}).pretty();

db.sources.find({ "systemName": { $regex:/tjjenk2/i}}, { systemName:1, actions:1}).pretty();
db.sources.find({ "systemName": { $regex:/tjjenk2/i}}, { fields:0 }).pretty();

# see the number of fields 
db.sources.aggregate([{ $match: { "systemName":"some_name"}}, { $group: { _id:"$systemName", total: { $sum:{$size:"$fields" }}}}]);

db.sources.find({ "template.vals": { $elemMatch: { val: { $ne:"" } }} }, { name:1, template.1 }).limit(10).pretty();

db.queries.find( { _id:ObjectId("xxxx") }, { queryName:1, "source.uid":1 }).pretty();

# how to fake mongo into writing to a file
/usr/bin/mongo --ssl x.x.x.x:xxxx/dbname --sslPEMKeyFile /etc/pki/private/identity.pem --sslAllowInvalidHostNames --sslCAFile /etc/pki/public/trust.crt << EOF > script.out
use dbname
db.auth('user', 'password')
db.queries.find({ status:"COMPLETED" }, { name:1, template:1, status:1 }).pretty()
EOF

# a lookup example
db.sources.aggregate([
  { $match: { _id:ObjectId("xxx") }},
  { $unwind: "$actions" },
  {
    $lookup: {
      from: "actions",
      localField: "actions",
      foreignField: "name",
      as: "actions"
    }
  },
  { $unwind: "$actions" },
  {
    $group, {
      _id: "$_id",
      actions: { "$push":"$dataActions" }
  }
]).pretty();

# return all the flow uuids for a particular query
db.queries.aggregate([
  { $match: { _id:ObjectId("xxx") }},
  { $project: { _id:0, flows:1 }},
  { $unwind: "$flows" },
  { $project: { "flows.uuid":1, "flows:alt_uuid":1 }}
]).pretty();

# server info
db.serverBuildInfo()
db.isMaster()

# date example
db.queries.find( { "queries.suspendedBy":"tjjenk2", "queries.suspendedDate": { "$gte": new ISODate("2016-04-26T00:00:00Z") } });

# query flows that are grouped by true/false/null
db.queries.aggregate([
  { $match: { _id:ObjectId("xxx") }},
  { $project: { _id:0, flows:1 }},
  { $unwind: "$flows" },
  { $project: { 
    uuid: "$flows.uuid",
    isMarked: "$flows.isMarked"
  }},
  { $group: { 
    _id: "$isMarked",
    total_count: { $sum:1 },
    uuids: { $addToSet: "$uuid" }
  }}  
]).pretty();

# count all field data types for a particular source (fields -> array type)
db.sources.aggregate([
  { $match: { "fields.1": { $exists:true} }},
  { $project: { _id:0, fields:1 }},
  { $unwind: "$fields" },
  { $project: { dataType:"$fields:dataType" }},
  { $group: { 
    _id: "$dataType",
    total_count: { $sum:1 }
  }} 
]);

# sorted example
db.sources.aggregate([
  { $match: {
    roles: { $in: [ "role1", "role7", "role13" ] },
    status: { $in: [ "COMPLETED", "FINAL" ] }
  }},
  { $project: { name:1, userRoles:1, status:1 }},
  { $sort: { name:1 } },  
  { $sort: { status:1 } } 
]);

# count of the above
db.sources.aggregate([
  { $match: {
    roles: { $in: [ "role1", "role7", "role13" ] },
    status: { $in: [ "COMPLETED", "FINAL" ] }
  }},
  { $project: { name:1, userRoles:1, status:1 }},
  { $group: { _id: null, total: { $sum:1 } }} 
]);

# group values into an arry using an aggregate
db.ations.aggregate([
  { $match: {
    name: { $regex: /xyz/i },
    status: { $in: [ "COMPLETED", "FINAL" ] }
  }},
  { $project: { name:1 }},
  { $group: {
    _id:"some_action_name",
    names: { $push:"$name" }
  }}
]);

# count an array grouping
db.sources.aggregate([
  { $match: {
    ids: { $exists:true }
  }},  
  { $project: { ids:1 }},
  { $group: {
    _id:"$ids",
    total: { $sum:1 }
  }}  
]);

# connecting to another host
var myConnection = connect("x.x.x.x:xxxx/dbname");
myConnection.getSiblingDB("$external").auth({
  mechanism: "MONGODB-X509",
  user: "CN=x"
});

# replace all array elements with a new value (positional operator won't work)
var record = db.sources.findOne({ _id:ObjectID("xxx") });
record.fields.forEach(function(field) { field.categories = [] });
db.sources.update( { _id:ObjectId("xxx") }, record);

# one way to output to a file (but have it behave like the shell)
./myshell.sh prod < some_script.js > prod.txt

# to return default authorities 
db.sources.aggregate([
  { $match: {
    "query.authorities": { $exists:true, $ne:[] }
  }}
  { $project: { name:1, status:1, authorities:"$query.authorities" }},
  { $sort: { status:1 }
]);

# show the unique sources in preferences
db.preferences.aggregate([
  { $match: {
    authorities: { $exists:true, $ne:[] },
    sourceIds: { $exists:true, $ne:[] 
  }},
  { $project: { _id:0, sourceIds:1 }},
  { $unwind: "$sourceIds" },
  { $sort: { sourceIds:1 }},
  { $group: {
    _id:"$sourceIds",
    total: { $sum:1 }
  }}   
]);

# print with padding (as an array, excel-like)
var padding = Array(100).join(' ');
function pad(pad, str, padLeft) {
  if (typeof str === 'undefined') {
    return pad;
  }
  
  if (padLeft) {
    return (pad + str).slice(-pad.length);
  } else {
    return (str + pad).substring(0, pad.length);
  }
}
db.sources.count({ status:"FINAL"});
DBQuery.shellBatchSize = 1000;
var sources = db.sources.find({ status:"FINAL"}, { name:1, status:1, roles:1 }).toArray();
sources.map(function(source) { return source._id + " | " + pad(padding, source.name) + " | " + source.roles; });

# see last modified
db.sources.find( 
  { _id: { $in: [ ObjectId('x'), ObjectId('y') ] }},
  { name:1, status:1, "modifiedBy": { $slice:1 }}
);


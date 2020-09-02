load("mongo/underscore.js");
load("mongo/tj_utils.js");
load("mongo/queries_to_suspend.js");

(function() {
  var collection = "queries";
  
  var dedupeIds = function dedupeIds(ids) {
    if (ids && ids.length > 0) {
      var uniqueIds = _.uniq(ids, function(id) {
        return id;
      });
    }
    return uniqueIds;
  };
  
  var displayIndented = function displayIndented(message) {
    print('    *** ' + message);
  };
  
  print("Total query IDS before dedupe [" + queryIds.length + "]");
  queryIds = dedupeIds(queryIds);
  print("Total query IDS after dedupe [" + queryIds.length + "]")

  var suspendQuery = function suspendQuery(queryId) {
    var wasQuerySuspended = false;
    if (queryId) {
      var id = ObjectId(queryId);
      var whereClause = {
        id: id,
        "query.status": { "$ne": "SUSPENDED" }
      };
      
      var filter = {
        status: 1,
        "query.suspendedDate": 1,
        "query.suspendedBy":1,
        "query.status":1
      };
      
      var updateValues = { "$set": {
        "query.suspendedDate": new ISODate(),
        "query.suspendedBy":adminUser,
        "query.status":"SUSPENDED"
      }};
      
      var queryCursor = db[collection].find(whereClause, filter);
      if (queryCursor.hasNext()) {
        db[collection].updateOne(whereClause, updateValues);
        wasQuerySuspended = true;
      }
    } else {
      throw "missing queryId";
    }
    
    return wasQuerySuspended;
  };
  
  var validateQuerySuspension = function validateQuerySuspension(queryId) {
    var queryResults = db[collection].aggregate([
      { $match: { _id:ObjectId(queryId) }},
      { $project: { _id:0, flows:1 }},
      { $unwind: { "$flows" }},
      { $project: {
        uuid: "$flows.uuid",
        marked: "$flows.marked"
      }},
      { $group: {
        _id: "$marked",
        total_count: { $sum: 1 }
      }}      
    ]);
    
    if (queryResults) {
      // aggregates executed in a shell are not a cursor and are wrapped in "x.result"; outside a shell, they are a cursor
      queryResults = queryResults.result || queryResults.toArray();
      
      for (var recordNum = 0; recordNum < queryResults.length; recordNum++) {
        var record = queryResults[recordNum];
        var isMarked = record._id;
        var totalCount = record.total_count;
        
        if (isMarked === true) {
          displayIndented(queryId + ' contains [' + totalCount + '] flows.marked = true; fixing by setting them to false');
          db[collection].update(
            { _id: ObjectId(queryId), "flows.marked": true },
            { $set: { "flows.$.marked": false }}
          );
          break;
        }
      };
    }
  }
  
  print("Starting query suspension ...");
  
  queryIds.forEach(function(queryId, index, array) {
    var wasQuerySuspended = suspendQuery(queryId);
    // removed the printQuery function that used the wasQuerySuspended
    validateQuerySuspension(queryId);
  });
  
  print("Finished!");
})();

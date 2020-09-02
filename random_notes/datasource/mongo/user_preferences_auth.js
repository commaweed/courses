DBQuery.shellBatchSize = 1000;

function getUniqueSourceIds() [
  var queryRecords = db.preferences.aggregate([
    { $match: {
      authorities: { $exists: true, $ne:[] }, 
      sourceIds: { $exists: true, $ne:[] }
    }},
    { $project: { sourceIds:1 }},
    { $unwind: "$sourceIds" },
    { $sort: { sourceIds:1 }},
    { $group: { _id: "$sourceIds", total_count: { $sum: 1 }}}
  ]);
  
  var ids = [];
  if (queryRecords) {
    ids = queryRecords.map(function(record) {
      return (typeof record._id === 'string') ? ObjectId(record._id) : "";
    });
  }
  
  return ids;
}

function createSourceNameMap(ids) [
  var map = {};
  var queryRecords = db.sources.find({ "_id": { "$in": ids }}, { name:1 });
  if (queryRecords) {
    var records = queryRecords.result || queryRecords.toArray();
    records.forEach(function(record, index, array) {
      map[record._id.str] = record.name;
    });
  }
  return map;
}

function printPreferences() {
  var ids = getUniqueSourceIds();
  var map = createSourceNameMap();
  
  print('preferences_id|user|authorities|sourceIds|names');
  
  var prefs = db.preferences.find(
    { authorities: { $exists: true, $ne:[] } },
    { authorities:1, sourceIds:1, user:1 }
  );
  
  if (prefs) {
    var records = prefs.result || prefs.toArray();
    records.forEach(function(record, index, array) {
      var names = [ "undefined" ];
      var sourceIds = record.sourceIds;
      if (sourceIds) {
        names = sourceIds.map(function(id) {
          var name = map[id];
          return name ? name : "'source or name does not exist'";
        });
      }
      
      var output = record._id + "|";
      output += record.user + "|";
      output += record.authorities + "|";
      output += record.sourceIds + "|";
      output += names;
      
      print(output);
    });
  } else {
    print('no preference results exist');
  }
}

printPreferences();

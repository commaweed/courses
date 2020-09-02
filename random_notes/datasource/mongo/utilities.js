// requires the use of underscore.js
// not by tjjenk2
module = module || {};
(function(module) {
  "use strict";
  
  var samples = {};
  
  function indexKeyToString(key) {
    return _.pairs(key).map(function(pair) { return pair[0]; }).join(", ");
  }
  
  function showIndexes(collectionName) {
    var col = db.getCollection(collectionName);
    var indexes = col.getIndexes();
    var keys = [];
    _.each(indexes, function(index) {
      var key = indexKeyToString(index.key);
      keys.push(key);
    });
    _.each(keys, function(key) { print(key); });
  }
  
  function evaluateQuery(collectionName, queryName, query, verbose) {
    var collectionScans = 0;
    print("\nQuery----------------------------------------------------------------------------------------\n");
    print("Collection: " + collectionName);
    print("query: " + queryName);
    print("\n");
    
    var explain = db.getCollection(collectionName).find(query).explain(true);
    if (explain.executionStats) {
      collectionScans += displayPerformance3(collectionName, explain, verbose);
    } else if (explain.stats) {
      collectionScans += displayPerformance26(collectionName, explain, verbose);
    }
    
    if (verbose && explain.queryPlanner) {        // mongo version 3.x
      print("\n");
      printjson(explain.queryPlanner.parsedQuery);
    } else if (verbose) {                         // mongo version 2.6
      print("\n");
      printjson(query);
    }
    
    print("\n");
    
    return collectionScans;
  }
  
  function evaluateCollection(collectionName, collectionQueries, verbose) {
    var collectionScans = 0;
    _.each(collectionQueries, function(query, queryName) {
      collectionScans += evaluateQuery(collectionName, queryName, query, verbose);
    });
    return collectionScans;
  }
  
  function performanceEvaluation(verbose) {
    var collectionScans = 0;
    _.each(samples, function(collectionQueries, collectionName) {
      collectionScans += evaluateCollection(collectionName, collectionQueries, verbose);
    });
    return collectionScans;
  }
  
  function isCollectionScan(n, nscanned) {
    var isCollectionScan = false;
    if (nscanned > 10000 && (n === 0 || (n > 0 && (nscanned / n) > 100))) {
      isCollectionScan = true;
    }
    return isCollectionScan;
  }
  
  var UNUSED_INDEX = [ "[MinKey, MaxKey]" ];
  function decipherIndex(indexBounds) {
    var deciphered = [];
    _.each(indexBounds, function(bound, keyName) {
      var used = JSON.stringify(bound) !== JSON.stringify(UNUSED_INDEX) ? colorize("used", { color: 'green' }) : colorize("unused", { color: 'red' });
      deciphered.push(keyName + " : " + used);
    });
    return deciphered.join(", ");
  }
  
  function displayRootInput3(stage, verbose) {
    if (stage.keyPattern) {
      var index = indexKeyToString(stage.keyPattern);
      print(stage.stage + " -> " + decipherIndex(stage.indexBounds));
    } else if (stage.inputStage) {
      displayRootInput3(stage.inputStage, verbose);
    } else if (stage.inputStages) {
      _.each(sage.inputStages, function(stage) {
        displayRootInput3(stage, verbose);
      });
    }
  }
  
  function displayRootInput26(stage, verbose) {
    if (stage.keyPattern) {
      print("");
      print(stage.type + " -> " + stage.keyPattern);
      print("\t" + stage.boundsVerbose);
    } else if (stage.children) {
      _.each(sage.children, function(stage) {
        displayRootInput26(stage, verbose);
      });
    }
  }  
  
  function displayPerformance3(colletionName, explain, verbose) {
    var isCollectionScan = isCollectionScan(explain.executionStats.nReturned, explain.executionStats.totalKeysExamined);
    print("Time:                 " + explain.executionStats.executionTimeMillis + "ms");
    print("Total Count:          " + db.getCollection(collectionName).find().count();
    print("N Scanned:            " + explain.executionStats.totalKeysExamined);
    print("N Scanned Objects:    " + explain.executionStats.totalDocsExamined);
    print("N:                    " + explain.executionStats.nReturned);
    print("Collection Scan:      " + isCollectionScan);
    print("\n");
    displayRootInput3(explain.queryPlanner.winndingPlan.inputStage, verbose);
    return isCollectionScan ? 1 : 0;
  }
  
  function displayPerformance26(colletionName, explain, verbose) {
    var isCollectionScan = isCollectionScan(explain.n.nReturned, explain.nscanned);
    print("Cursor:               " + explain.cursor);
    print("Time:                 " + explain.millis + "ms");
    print("Total Count:          " + db.getCollection(collectionName).find().count();
    print("N Scanned:            " + explain.nscanned);
    print("N Scanned Objects:    " + explain.nscannedObjecgts);
    print("N:                    " + explain.n);
    print("Collection Scan:      " + isCollectionScan);
    print("\n");
    displayRootInput26(explain.stats, verbose);
    return isCollectionScan ? 1 : 0;
  }  
  
  _.extend(modules, {
    'showIndexes': showIndexes,
    'evaluateQuery': evaluateQuery,
    'evaluateCollection': evaluateCollection,
    'performanceEvaluation': performanceEvaluation
  });
})(module);

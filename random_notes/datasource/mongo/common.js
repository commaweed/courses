function collectionCopy(collName, newName) {
  var from = db[collName];
  var to = db[newName];
  to.ensureIndex( { _id: 1} );
  var count = 0;
  var cursor = from.find();
  while (cursor.hasNext()) {
    var o = curser.next();
    count++;
    to.save(o);
  }
  return count;
}

function versionExists(version) {
  return db.someVersionCollection.findOne( { version: version } ) !== null;
}

function anyVersionsExist() {
  return db.someVersionCollection.count() > 0;
}

function setVersion(version) {
  db.someVersionCollection.insert( { version: version, updateDate: new Date() } );
}

function updateToVersion(previousVersion, currentVersion, updateFunction) {
  if (versionExists(currentVersion)) {
    throw "ERROR: the database is already on version " + currentVersion;
  }
  if (!versionExists(perviousVersion) && anyVersionsExist()) {
    throw "ERROR: updating to " + currentVersion + " requires that the database be on version " + perviousVersion;
  }  
  
  updateFunction();
  
  setVersion(currentVersion);
}

function addNewFeature(featureName, roles, description) {
  db.features.udate(
    { name: featureName },
    { $set: { name: featureName, roles: roles, description: description }},
    { upseert: true }
  );
}

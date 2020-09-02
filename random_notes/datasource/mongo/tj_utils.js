var tj={};
(function(tj) {
   function blah() {}
      
   function validateId(id) {
      if (!id) throw "Invalid id; it must be defined and cannot be null!";
      if (typeof id !== 'string') throw "Invalid id; it must be a string type!");
      if (id.trim().length === 0) throw "Invalid id; it cannot be empty and must have length > 0!";
   }
   
   function getLayout(id) {
      validateId(id);
      
      var result = {};
      result["_id"] = id;
      
      var cursor = db.layouts.find( { _id:ObjectId(id) } );
      if (cursor.hasNext()) {
         var record = cursor.next();
         
         if (record.name) result["name"] = record.name;
         if (record.model) result["modelCount"] = record.model.length;
         if (record.sourceId) result["sourceId"] = record.sourceId;
      }
      
      return result;
   }
   
   function getSource(id) {
      validateId(id);
      
      var result = {};
      
      var cursor = db.sources.find( { _id:ObjectId(id) } );
      if (cursor.hasNext()) {
         var record = cursor.next();
         
         result["_id"] = record._id;
         if (record.name) result["name"] = record.name;
         if (record.status) result["status"] = record.status;
         
         if (record.views && record.views.length > 0) {
            var viewResult = {};
            
            for (var i=0; i < record.views.length; i++) {
               var currentView = record.views[i];
               viewResults["dataView"] = currentView;
               
               var systemId = currentView.defaultSystemId;
               if (systemId && systemId.trim().length > 0) {
                  currentView["layout"] = getLayout(systemId);
               }
            }
            
            result["dataViews"] = viewResult;
         }
      }      
      
      return result;
   }
   
   function getRoles(user, useRegex) {
      var id = user;
      if (useRegex === true) {
         id = { $regex: ".*" + user + ".*", $options:"i" };
      }
      
      var roles = db.roles.find( { _id: id } );
      return roles ? roles.toArray() : "No user roles exist for user [" + user + "]";
   }
   
   _.extend(tj, {
      'getSource': getSource,
      'getRoles': getRoles,
      'getLayout': getLayout,
      'printSource': function printSource(id) { printjson(getSource(id)); },
      'printRoles': function printRoles(user) { printjson(getRoles(user, true)); }
})(tj);

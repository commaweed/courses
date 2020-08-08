package util;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Represents a query that can be submitted to the server.
 */
public class QueryCriteria {
    private final DBCollection collection;
    private DBObject query;
    private DBObject fields;
    private DBObject sortCriteria;
    private DBObject hint;
    private String hintString;
    private Integer skipRows;
    private Integer limitRows;
    private boolean explain;

    public QueryCriteria(DBCollection collection) {
        if (collection == null) {
            throw new IllegalArgumentException(
                    "Invalid collection; it cannot be null.");
        }
        this.collection = collection;
    }

    /**
     * @return the query
     */
    public DBObject getQuery() {
        return query;
    }

    /**
     * @param query the query to set
     */
    public QueryCriteria setQuery(DBObject query) {
        this.query = query;
        return this;
    }

    /**
     * @return the fields
     */
    public DBObject getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public QueryCriteria setFields(DBObject fields) {
        this.fields = fields;
        return this;
    }

    /**
     * @return the sortCriteria
     */
    public DBObject getSortCriteria() {
        return sortCriteria;
    }

    /**
     * @param sortCriteria the sortCriteria to set
     */
    public QueryCriteria setSortCriteria(DBObject sortCriteria) {
        this.sortCriteria = sortCriteria;
        return this;
    }

    /**
     * @return the hint
     */
    public DBObject getHint() {
        return hint;
    }

    /**
     * @param hint the hint to set
     */
    public QueryCriteria setHint(DBObject hint) {
        if (hintString != null) {
            hintString = null; // override
        }
        this.hint = hint;
        return this;
    }

    /**
     * @return the hintString
     */
    public String getHintString() {
        return hintString;
    }

    /**
     * @param hintString the hintString to set
     */
    public QueryCriteria setHintString(String hintString) {
        if (hint != null) {
            hint = null; // override
        }
        this.hintString = hintString;
        return this;
    }

    /**
     * @return the skipRows
     */
    public Integer getSkipRows() {
        return skipRows;
    }

    /**
     * @param skipRows the skipRows to set
     */
    public QueryCriteria setSkipRows(Integer skipRows) {
        this.skipRows = skipRows;
        return this;
    }

    /**
     * @return the limitRows
     */
    public Integer getLimitRows() {
        return limitRows;
    }

    /**
     * @param limitRows the limitRows to set
     */
    public QueryCriteria setLimitRows(Integer limitRows) {
        this.limitRows = limitRows;
        return this;
    }

    /**
     * @return the explain
     */
    public boolean isExplain() {
        return explain;
    }

    /**
     * @param explain the explain to set
     */
    public QueryCriteria setExplain(boolean explain) {
        this.explain = explain;
        return this;
    }

    /**
     * @return the collection
     */
    public DBCollection getCollection() {
        return collection;
    }

    /**
     * Resets the query.
     * @return
     */
    public QueryCriteria reset() {
        this.setQuery(null);
        this.setExplain(false);
        this.setFields(null);
        this.setHint(null);
        this.setHintString(null);
        this.setLimitRows(null);
        this.setSkipRows(null);
        this.setSortCriteria(null);
        return this;
    }
    
    /**
     * Submits the query to the server.
     * @return A String representing the server results.
     */
    public String submitFind() {
        StringBuilder serverResult = new StringBuilder();
        DBCollection collection = this.getCollection();
        
        serverResult.append("Total Collection Documents [" + collection.count() + "]:\n");

        DBCursor cursor = find();
        
        if (this.isExplain()) {
            DBObject explainedDocument = cursor.explain();
            for (String currentElement : explainedDocument.keySet()) {
                serverResult.append(String.format("%25s: %s%n", currentElement, explainedDocument.get(currentElement)));
            }
        } else {
            try {
                while (cursor.hasNext()) {
                    serverResult.append(cursor.next() + "\n");
                }
                
                serverResult.append("\n");
            } finally {
                cursor.close();
            }
        }       
        return serverResult.toString();
    }
    
    /**
     * Performs a find query and returns the cursor object.
     * @return DBCursor
     */
    public DBCursor find() {
        DBCursor cursor = collection.find(
                this.getQuery(),
                this.getFields()
        ).sort(this.getSortCriteria());
        
        if (this.getHint() != null) {
            cursor.hint(this.getHint());
        } else if (this.getHintString() != null) {
            cursor.hint(this.getHintString());
        }
        
        if (this.getSkipRows() != null) cursor.skip(this.getSkipRows());
        if (this.getLimitRows() != null) cursor.limit(this.getLimitRows());
        return cursor;
    }
}
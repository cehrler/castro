USE castro_db;
DROP PROCEDURE IF EXISTS getDictionary;
CREATE PROCEDURE getDictionary()
SELECT * FROM dictionary;

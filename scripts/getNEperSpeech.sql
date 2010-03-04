USE castro_db;
DROP PROCEDURE IF EXISTS getNEperSpeech;
CREATE PROCEDURE getNEperSpeech(_SPEECH_ID INT)
SELECT NE_INDEX_ID, NE_NAME, NE_TYPE, COUNT FROM NE INNER JOIN speech_NE ON (NE.NE_ID = speech_NE.NE_ID) WHERE speech_NE.SPEECH_ID = _SPEECH_ID ORDER BY NE_TYPE, NE_INDEX_ID;
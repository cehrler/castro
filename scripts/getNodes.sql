DROP PROCEDURE getNodes;

CREATE PROCEDURE getNodes(SINCE DATE, TILL DATE)
SELECT speech.SPEECH_ID, speech.SPEECH_DATE, speech.HEADLINE, speech.REPORT_DATE, author.AUTHOR_NAME, document_type.DOCTYPE_NAME, place.PLACE_NAME, report_nbr.REPORTNBR_NAME, source.SOURCE_NAME 
FROM speech LEFT OUTER JOIN author ON (speech.AUTHOR_ID = author.AUTHOR_ID)
            LEFT OUTER JOIN document_type ON (speech.DOCUMENT_TYPE_ID = document_type.DOCTYPE_ID)
            LEFT OUTER JOIN place ON (place.PLACE_ID = speech.PLACE_ID)
            LEFT OUTER JOIN report_nbr ON (report_nbr.REPORTNBR_ID = speech.REPORT_NBR_ID) 
            LEFT OUTER JOIN source ON (source.SOURCE_ID = speech.SOURCE_ID)
WHERE (speech.SPEECH_DATE > SINCE OR SINCE IS NULL) AND (speech.SPEECH_DATE < TILL OR TILL IS NULL);


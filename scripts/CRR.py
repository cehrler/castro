#!/usr/bin/env python
# encoding: utf-8

'''
Created on 27.02.2010

Generates a similarity matrix for each named entity

@author: Todd Shore
'''

import os, sys, re

# dictionary of all named entities in corpus
entities = {}

class CRRFile:
    def __init__(self):
        # dictionary of all named entities in file
        self.namedEnts = {}

    '''
    Reads in a file with named-entity data and creates an array of named entity types
    '''
    def readFile(self, infile):
        self.infile = open(infile, 'r')    
        STATE = "NULL"
        
        for line in self.infile:
            if line == "<PERSONS>\n":
                STATE = "PERSONS"
                continue
            if line == "</PERSONS>\n":
                STATE = "NULL"
                continue
            if line == "<LOCATIONS>\n":
                STATE = "LOCATIONS"
                continue
            if line == "</LOCATIONS>\n":
                STATE = "NULL"
                continue
            if line == "<ORGANIZATIONS>\n":
                STATE = "ORGANIZATIONS"
                continue
            if line == "</ORGANIZATIONS>\n":
                STATE = "NULL"
                continue
           
            if STATE == "PERSONS":
                tokens = line.rsplit(" ", 1)
                name = normalizeString(tokens[0])
                self.addNamedEnt('PERSONS', name, int(tokens[1]))
           
            if STATE == "LOCATIONS":
                tokens = line.rsplit(" ", 1)
                name = normalizeString(tokens[0])
                self.addNamedEnt('LOCATIONS', name, int(tokens[1]))

            if STATE == "ORGANIZATIONS":
                tokens = line.rsplit(" ", 1)
                name = normalizeString(tokens[0])
                self.addNamedEnt('ORGANIZATIONS', name, int(tokens[1]))

                
    def calculateSimilarities(self):
        # for each entity type dictionary in global dictionary,
        for entType in self.namedEnts:
            typeDictKeys = self.namedEnts[entType].keys()
            # for each entity in dictionary,
            for i in range(len(typeDictKeys)):
                # for each entity in dictionary,
                for j in range(i + 1, len(typeDictKeys)):
                    score = symmetricLongestCommonSubstring(typeDictKeys[i], typeDictKeys[j])
                    entity = self.namedEnts[entType][typeDictKeys[i]]
                    match = typeDictKeys[i]
                    entity.addSimilarityScore(match, score)
                    print score, typeDictKeys[i], "|", typeDictKeys[j] 

    '''
    Checks presence of named entity in dictionary-- if in dictionary, increase count by 1. If not, add it to dictionary beforehand

    @param entType: Type of named entity
    @param namedEnt: Form of named entity
    @param count: count of named entity in document
    '''
    def addNamedEnt(self, entType, namedEnt, count):
        if entType not in self.namedEnts:
            self.namedEnts[entType] = {}
        if namedEnt not in self.namedEnts[entType]:
            self.namedEnts[entType][namedEnt] = NamedEnt(entType)
        self.namedEnts[entType][namedEnt].addCount(count)
    
'''
A named entity in the document

@param type: the entity type (e.g. person, location, organization)
'''      
class NamedEnt:
    def __init__(self, type):
        self.type = type
        self.count = 0
        # dictionary of similarity score of word to every other word in its type
        self.similarityScores = {}
    
    '''
    Adds similarity score of another word to the dictionary
    
    @param match: Word to be matched
    @param score: Similarity score    
    '''
    def addSimilarityScore(self, match, score):
        if match not in self.similarityScores:
            self.similarityScores[match] = 0.0
        self.similarityScores[match] = score

    def addCount(self, value):
        self.count += value
        
    def getType(self):
        return self.type
    
def matchLongestCommonSubstring(string, match):
    longestMatch = 0
    for i in range(len(string)):
        for j in range(1,len(string[i:])+1):
            if match.find(string[i:i+j]) >= 0 and longestMatch <= len(string[i:i+j]):
                longestMatch = len(string[i:i+j])
                if longestMatch == min(len(string),len(match)):
                    return float(longestMatch)/min(len(string),len(match))
    return float(longestMatch)/min(len(string),len(match))

def symmetricLongestCommonSubstring(string1, string2):
    l1 = matchLongestCommonSubstring(string1, string2)
    l2 = matchLongestCommonSubstring(string2, string1)
    return max(l1, l2)
                    
'''
Normalize string by separating substrings starting with an uppercase letter and removing multiple whitespaces

@param string: String to be split
@return: Normalized string
'''        
def normalizeString(string):
    string = re.sub(r'(?<=.)([A-Z])', r' \1', string)
    return re.sub("\s+" , " ", string)
        
if __name__ == '__main__':
    if len(sys.argv) != 2:
        echo("Usage: " + sys.argv[0] + " <infile>")
        exit
    crr = CRRFile()
    crr.readFile(sys.argv[1])
    crr.calculateSimilarities()

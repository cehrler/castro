#!/usr/bin/env python
# encoding: utf-8
"""
untitled.py

Created by Carsten Ehrler on 2010-03-02.
Copyright (c) 2010 __MyCompanyName__. All rights reserved.
"""

import sys
import os

THRESHOLD = 1
SPECTRUM = 2

def main():
	ner_files =  os.popen("ls ../data/1959*.nerd").readlines()
	
	persons = []
	locations = []
	organizations = []
	
	for ner_file in ner_files:
		filename = ner_file.strip()
		print filename
		
		ner_fo = open(filename, 'r')
		content = ner_fo.readlines()
		ner_fo.close()
		
		STATE = "NULL"
		
		for line in content:
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
				persons.append((tokens[0].lower().replace(" ", ""), filename, tokens[0]))
			
			if STATE == "LOCATIONS":
				tokens = line.rsplit(" ", 1)
				locations.append((tokens[0].lower().replace(" ", ""), filename, tokens[0]))
			
			if STATE == "ORGANIZATIONS":
				tokens = line.rsplit(" ", 1)
				organizations.append((tokens[0].lower().replace(" ", ""), filename, tokens[0]))  
	
	per_corefs = analyzeEntities(persons)
	org_corefs = analyzeEntities(organizations)
	loc_corefs = analyzeEntities(locations)
	
	outfile = open("persons.coref", 'w')
	for coref in per_corefs:
		outfile.write(str(coref))
	outfile.close()
	
	outfile = open("organizations.coref", 'w')
	for coref in org_corefs:
		outfile.write(str(coref))
	outfile.close()
	
	outfile = open("locations.coref", 'w')
	for coref in loc_corefs:
		outfile.write(str(coref))
	outfile.close()



class CoRef(object):
	"""docstring for Entity"""
	def __init__(self, name, similarity):
		super(CoRef, self).__init__()
		self.name = name
		self.similarity = similarity
		self.entities = []
		self.original = ""
	
	def __str__(self):
		string = self.name + "\n"
		for (n,f,o) in self.entities:
			string += "\t%s (%s)\n" % (o,f)
		return string
	
	def addEntity(self, name, filename, original):
		self.entities.append((name, filename, original))
	
	def computeSimilarity(self, method, name2):
		similarity = []
		
		for (name, _, _) in self.entities:
			similarity.append(self.similarity(name, name2))
		
		if method == "AVERAGE":
			return sum(similarity)/float(len(similarity))
		if method == "CLOSEST":
			return min(similarity)
		if method == "FURTHEST":
			return max(similarity)

def editDistance(x, y): 
	SUBSTITUTION = 1
	DELETION = 1
	INSERTION = 1
	dist = [[ 0 for j in range(len(y))] for i in range(len(x))]
	
	for i in range(len(x)):
		dist[i][0] = i
	
	for j in range(len(y)):
		dist[0][j] = j
	
	for i in range(1,len(x)):
		for j in range(1,len(y)):
			if x[i:i+1] == y[j:j+1]:
				cost = 0
			else:
				cost = SUBSTITUTION
			dist[i][j] = min(dist[i-1][j] + INSERTION, min(dist[i][j-1] + DELETION, dist[i-1][j-1] + cost))
	
	return dist[len(x)-1][len(y)-1]
	

def matchLongestCommonSubstring(word, match):
	longest_match = 0
	for i in range(len(word)):
		for j in range(1,len(word[i:])+1):
			if match.find(word[i:i+j]) >= 0 and longest_match <= len(word[i:i+j]):
				longest_match = len(word[i:i+j])
				if longest_match == min(len(word),len(match)):
					return float(longest_match)/min(len(word),len(match))
	return float(longest_match)/min(len(word),len(match))

def symmetricLongestCommonSubstring(word1, word2):
	l1 = matchLongestCommonSubstring(word1, word2)
	l2 = matchLongestCommonSubstring(word2, word1)
	return max(l1, l2)               

def spectrumKernel(word1, word2):
	ngram1 = set()
	ngram2 = set()
	
	for i in range(len(word1)-SPECTRUM):
		ngram1.add(word1[i:i+SPECTRUM])
	
	for j in range(len(word2)-SPECTRUM):
		ngram2.add(word2[j:j+SPECTRUM])
	
	return len(ngram1.intersection(ngram2))/float(min(len(ngram1),len(ngram2)))

def analyzeEntities(entities):
	corefs = []
	
	for i in range(len(entities)):
		print i/float(len(entities))
		(name, file_id, original) = entities[i]
		max_sim = 0
		max_cor = None
		for coref in corefs:
			try:
				sim = coref.computeSimilarity("AVERAGE", name)
			except ZeroDivisionError, e:
				sim = 0
			if sim > max_sim:
				max_sim = sim
				max_cor = coref
		
		if max_cor == None or max_sim < THRESHOLD:
			coref = CoRef(name, spectrumKernel)
			coref.addEntity(name, file_id, original)
			corefs.append(coref)
		else:
			max_cor.addEntity(name, file_id, original)
	
	return corefs

if __name__ == '__main__':
	main()


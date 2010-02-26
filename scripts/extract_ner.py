#!/usr/bin/env python
# encoding: utf-8
"""
this script expects the filename to analyze without extensions
for the text file it assumes .text and for the NEs it assumes .ner
the output is written to <FILENAME>.nerd
"""

import sys
import os


def main():
	txt = open(sys.argv[1] + '.text', 'r')
	ner = open(sys.argv[1] + '.ner', 'r')
	
	txt_content = txt.readlines()
	ner_content = ner.readlines()
	
	txt.close()
	ner.close()
	
	ner_content = reduce(str.__add__, ner_content, "").replace("\n", "")
	
	
	# if you want to extract other/additional tags
	# just add the desired tag here
	ner_loc = parseTag("LOCATION", ner_content)
	ner_per = parseTag("PERSON", ner_content)
	ner_org = parseTag("ORGANIZATION", ner_content)
	
	# write the ouput
	
	out = open(sys.argv[1] + ".nerd", 'w')
	
	out.write("<TEXT>\n")
	for line in txt_content:
		out.write("%s" % line)
	out.write("</TEXT>\n")
	out.write("<PERSONS>\n")
	for entry in ner_per:
		out.write("%s %d\n" % (entry, ner_per[entry]))
	out.write("</PERSONS>\n")
	out.write("<LOCATIONS>\n")
	for entry in ner_loc:
		out.write("%s %d\n" % (entry, ner_loc[entry]))
	out.write("</LOCATIONS>\n")
	out.write("<ORGANIZATIONS>\n")
	for entry in ner_org:
		out.write("%s %d\n" % (entry, ner_org[entry]))
	out.write("</ORGANIZATIONS>\n")
	
	out.close()


def parseTag(tag, text):
	tags = {}
	
	tag_beg = "<" + tag + ">"
	tag_end = "</" + tag + ">"
	
	current_index = 0
	while(True):
		index_beg = text.find(tag_beg, current_index)
		index_end = text.find(tag_end, current_index)
		
		if index_beg == -1:
			return tags
		try:
			tags[text[index_beg+len(tag)+2:index_end]] += 1
		except KeyError:
			tags[text[index_beg+len(tag)+2:index_end]] = 1
		
		current_index = index_end + 1

if __name__ == '__main__':
	main()


#!/usr/bin/env python
# encoding: utf-8
'''
Generate a similarity matrix of each named entity to each 
Created on 03.03.2010

@author: Todd Shore
'''

import os, sys

##------------------------------------------------------------------------------
## Programme settings: change these to fit your desired input and output formats
# input file extension
fileExt = '.txt'


##------------------------------------------------------------------------------
## Programme: Edit at your own risk!

class CorefFile:
    def __init__(self, infile):
        self.infile = infile
        # dictionary of each entity in file and its count in the document
        self.entCounts = {}
        
'''
Return if a given file matches given input criteria

@param file: The file to be checked
@param ext: Extension of files to be processed
@param sfx: Suffix of files already processed or otherwise to be excluded from batch
@param exclusions: List of strings, which the presence of any thereof in a filename excludes it from batch (optional)
@return: Whether a file is valid input for formatting or not
'''
def isInfile(file, ext, sfx, exclusions=[]):
    fileNamePair = os.path.splitext(file)
    # regular expression matching output file suffix at end of word
    exclusion = re.compile(sfx)
    # if file has the required ext and does not already contain output file suffix,
    if fileNamePair[1] == ext and not exclusion.search(fileNamePair[0]):
        # for each item in excluded keyword list
            for exclusion in exclusions:
                exclusion = re.compile(exclusion)
                # if the filename base contains exclusion, file fails test
                if exclusion.search(fileNamePair[0]):
                    return False
                    break
                else: continue
            return True
    else: return False
    
'''
Make a list of input files in directory
@param inputDir: Directory to be scanned for eligible files
@param ext: Extension of files to be processed
@param fileClass: Class of input file object(s) to be created
@param exclusions: List of strings, which the presence of any thereof in a filename excludes it from batch (optional)
@return: List of each file to be processed as input
'''
def createInfileList(inputDir, ext, fileClass, exclusions=[]):
    # list of instances of files to be processed
    infileList = []
    for root, dirs, files in os.walk(inputDir):
            for file in files:
                if isInfile(file, ext, outfileSfx, exclusions):
                    # create a new instance of file to be processed
                    infileList.append(fileClass(os.path.join(root, file)))
    return infileList

'''
Parse input and output arguments, allowing for single-file or batch usage and for automatic output file naming

@param args: List of (command-line) arguments
@param fileClass: Class of input file object(s) to be created
'''
def parseIOArgs(args, fileClass):
    if os.path.isdir(args[1]):
            inputDir = args[1]
            # if output argument is specified,
            if len(sys.argv) == 3:
                # output argument is output file suffix
                outfileSfx = args[2]
            infileList = createInfileList(inputDir, fileExt, fileClass)
    # if input argument is a file,
    elif os.path.isfile(args[1]):
        # if output argument is specified,
        if len(sys.argv) == 3:
            # create infile instance with specified output filename
            infileList = [fileClass(args[1], args[2])]
        # else, create infile instance without specified output filename 
        else: infileList = [fileClass(args[1])]
    else:
        exitBadArgs()
    
    return infileList

'''
Echo command usage info and exit
'''
def exitBadArgs():
    print("Usage: " + sys.argv[0] + " <infile|indir> <outfile>")
    sys.exit(1)
    
if __name__ == '__main__':
    if len(sys.argv) < 2 or len(sys.argv) > 3:
        exitBadArgs()
    else: infileList = parseIOArgs(sys.argv, NERFile)

    # infile counter
    infileListCounter = 0
    # for each file input by user (either a specific file or a batch from filesys walk)
    for infile in infileList:
        infileList[infileListCounter].readFile()
        infileListCounter += 1
#!/bin/python

import sys

parse = False
sum = 0
sum = 0
sum = 0
sum = 0
sum = 0
sum = 0
sum = 0
sum = 0
sum = 0
for line in sys.stdin.readlines():
    if line == "<ORGANIZATIONS>\n":
        parse = True
        continue
    if line == "</ORGANIZATIONS>\n":
        parse = False
        continue
    if parse:
        tokens = line.split()
        sum += int(tokens[-1])
print sum

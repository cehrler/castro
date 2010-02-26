#!/bin/sh

cd data

for file in *.meta; do
    name=`basename "${file}" .meta`
    python ../scripts/extract_ner.py ${name}
done

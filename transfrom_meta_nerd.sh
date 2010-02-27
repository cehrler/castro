#!/bin/sh

cd data

for file in *.ner; do
    name=`basename "${file}" .ner`
    python ../scripts/extract_ner.py ${name}
done

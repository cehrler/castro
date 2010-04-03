#!/bin/bash
# @date 2010-04-03
#
# Create database and tables for corpus metadata and similarity matrices. Run before first running the History Explorer application. You will most likely have to change the MySQL password below.

# MySQL username to make databases with (leave alone unless you know what you're doing)
MYSQL_USER=root
# MySQL password for above username (if 'root', use the root password for your OS)
MYSQL_PASSWD=root

./makeDatabase file_id.txt > castro.sql;
./makeNETables NE_id.txt file_id.txt > NE.sql;

run_mysql="mysql -u$MYSQL_USER -p$MYSQL_PASSWD"

setup_db(){
echo "Setting up databases...";
$run_mysql --execute="source ./castro.sql;"
$run_mysql --execute="source ./NE.sql;"
$run_mysql --execute="source ./getNE.sql;"
$run_mysql --execute="source ./getNEperSpeech.sql;"
$run_mysql --execute="source ./getNodes.sql;"
$run_mysql --execute="source ./getNode.sql;"
$run_mysql --execute="source ./getSpeechText.sql;"
$run_mysql --execute="source ./getDictionary.sql;"
} && echo "Database setup finished. If there were no errors, you may now run History Explorer."

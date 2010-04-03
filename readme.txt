History Explorer:
Unlock the secrets of the Past!
-------------------------------------------------------------
VERSION: 2010-04-03

AUTHORS:
- Yevgeni Berzak (jeniabk@gmail.com)
- Carsten Ehrler (carsten.ehrler@gmail.com)
- Michal Richter (michalisek@gmail.com)
- Todd Shore (t.c.shore@gmx.com)

WEBSITE: http://github.com/cehrler/castro

LICENSING:
All rights fully reserved by Yevgeni Berzak, Carsten Ehrler, Michal Richter and Todd Shore. Usage or reference to this work or any part thereof must feature credit to all the authors. Without explicit permission from the authors beforehand, this software, its source and documentation may NOT be:
- Distributed
- Incorporated into other products (either wholly or partially)
- Used to create derived works

Exceptions to the above are granted for Caroline Sporleder and Martin Schreiber (Saarland University, Germany) in specific and for research and teaching at Saarland University in general.

--------------------------------------------------------
REQUIREMENTS:
- Dual-core 2.0GHz CPU (recommended)
- >2GB RAM (recommended)
- Approx. 350MB available hard disk space
- Java (tested on version 1.6.0_15)
- MySQL Server (tested on version 14.14)

INSTALLATION INSTRUCTIONS:
1. Extract the archive containing this readme file into a new directory
2. In the 'scripts' subdirectory inside the new directory, open the script named 'setup_db.sh' in your editor of choice. Enter username and password information for your MySQL server (if set to default user 'root', simply change the value of 'MYSQL_PASSWD' to that of your root-user password, save and exit.
3. Run the script 'setup_db.sh'.
4. In the 'HistoryExplorer' subdirectory, open the file 'castro.conf.template' and enter the same MySQL username/password info you used in step 2. Save as a new file named 'castro.conf'.
5. To run the application, run the script 'historyExplorer' in the subdirectory 'HistoryExplorer'.
6. Have fun!

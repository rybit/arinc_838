#!/bin/bash

# Prevents commits with no Pivotal Tracker ID

denied=$(cat <<"EOF"
Oh snapÉ
 ______   _______  __    _  ___   _______  ______   __  
|      | |       ||  |  | ||   | |       ||      | |  | 
|  _    ||    ___||   |_| ||   | |    ___||  _    ||  | 
| | |   ||   |___ |       ||   | |   |___ | | |   ||  | 
| |_|   ||    ___||  _    ||   | |    ___|| |_|   ||__| 
|       ||   |___ | | |   ||   | |   |___ |       | __  
|______| |_______||_|  |__||___| |_______||______| |__| 


Did you forget a Tracker ID?
Example: git commit -m "I just fixed a bug. [123456]"
EOF
)

result=`grep -e '\[[0-9]\{6,\}\]' $1`
if [ "$result" == "" ];
then
  echo "$denied"
  exit 1
else
  exit 0
fi

#!/bin/bash

if [ $1 = "ubuntu" ]
then
DIRECTORY="/home/osboxes/o2"
cd ~
echo "Checking for git installation..."
apt-cache policy git | grep -q none
if [ $? -eq 0 ]
then
logger -s "ERROR: 'Git' is not installed"
sudo apt-get upgrade
sudo apt-get install-y git
else
logger -s "VERIFIED: git is installed..."
logger -s "Continuing.."
fi

logger -s "Checking for cmake installation..."
apt-cache policy cmake | grep -q none
if [ $? -eq 0 ]
then
logger -s "ERROR: 'CMake' is not installed"
sudo apt-get upgrade
sudo apt-get install -y cmake
else
logger -s "VERIFIED: CMake is installed..."
logger -s "Continuing.."
fi

if [ -d "$DIRECTORY" ];
then
logger -s "Directory exists.."
rm -rf $DIRECTORY
logger -s "Cloning O2 from Git.."
git clone https://github.com/rbdannenberg/o2.git
logger -s "Building O2.."
cd $DIRECTORY
cmake -H. -Bbuild
cmake --build build -- -j3
cd build
make

else
logger -s "Cloning O2 from Git.."
git clone https://github.com/rbdannenberg/o2.git
logger -s "Building O2.."
cd $DIRECTORY
cmake -H. -Bbuild
cmake --build build -- -j3
cd build
make
fi

logger -s "Executing test case.."
if [ -f "$2" ];
then
./$2 > $DIRECTORY/$2.txt 2>&1
else
logger -s "Invalid test case input, Please enter valid test case name.."
fi

fi

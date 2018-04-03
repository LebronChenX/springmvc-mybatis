#!/bin/sh
echo build-only.sh
echo ********************************
echo ${PWD}
cd .. ||  exit 1
mvn clean || exit 1
mvn package || exit 1


#prepare tar package

mv target/gridmarket*.war bin/ || exit 1
cd build || exit 1

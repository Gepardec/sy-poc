#!/bin/bash

SELF_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JBSS_DIR=$SELF_DIR/src/main/bin
CONFIG_DIR=$SELF_DIR/src/main/config

if [ x$1 = x ]; then
	echo "Usage: $0 path/to/wildfly-zip path/to/switchyard-zip [instance name] [port offset] [installation dir] [jboss major varsion]"
	exit 1;	
fi

if [ x$2 = x ]; then
	echo "Usage: $0 path/to/wildfly-zip path/to/switchyard-zip [instance name] [port offset] [installation dir] [jboss major varsion]"
	exit 1;	
fi


if [ x$3 = x ]; then
	INSTANZ="switchyard"

else
	INSTANZ=$3
fi

if [ x$4 = x ]; then
	OFFSET=100

else
	OFFSET=$4
fi

if [ x$5 = x ]; then
	HOME_DIR="~/jboss-switchyard"

else
	HOME_DIR=$5
fi

if [ x$6 = x ]; then
	export FORCE_JBOSS_MAJOR_CODE=8

else
	export FORCE_JBOSS_MAJOR_CODE=$6
fi

echo $INSTANZ

#setup jbss
$JBSS_DIR/setup.sh -i $INSTANZ -z $1 -o $OFFSET -j $HOME_DIR

#export switchyard zip
export SWITCHYARD_ZIP=$2

#configure jboss
~/bin/$INSTANZ configure $CONFIG_DIR




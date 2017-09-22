#!/bin/bash

SELF_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JBSS_DIR=$SELF_DIR/src/main/bin
CONFIG_DIR=$SELF_DIR/src/main/config
# Default values for parameters
DEFAULT_INSTANZ="switchyard"
DEFAULT_BIN_DIR=$HOME/bin
DEFAULT_JBOSS_MAJOR=10
DEFAULT_HOME_DIR="~/jboss-switchyard"
DEFAULT_OFFSET=100

if [ x$1 = x ]; then
	echo "Usage: $0 path/to/wildfly-zip path/to/switchyard-zip [instance name] [port offset] [installation dir] [jboss major varsion] [bin directory]"
	exit 1;
fi

if [ x$2 = x ]; then
	echo "Usage: $0 path/to/wildfly-zip path/to/switchyard-zip [instance name] [port offset] [installation dir] [jboss major varsion] [bin directory]"
	exit 1;
fi


if [ x$3 = x ]; then
	INSTANZ=$DEFAULT_INSTANZ

else
	INSTANZ=$3
fi

if [ x$4 = x ]; then
	OFFSET=$DEFAULT_OFFSET

else
	OFFSET=$4
fi

if [ x$5 = x ]; then
	HOME_DIR=$DEFAULT_HOME_DIR

else
	HOME_DIR=$5
fi

if [ x$6 = x ]; then
	export FORCE_JBOSS_MAJOR_CODE=$DEFAULT_JBOSS_MAJOR

else
	export FORCE_JBOSS_MAJOR_CODE=$6
fi

if [ x$7 = x ]; then
	CUSTOM_BIN_DIR=$DEFAULT_BIN_DIR

else
	CUSTOM_BIN_DIR=$7
fi

echo $INSTANZ

#setup jbss
$JBSS_DIR/setup.sh -i $INSTANZ -z $1 -o $OFFSET -j $HOME_DIR -b $CUSTOM_BIN_DIR

#export switchyard zip
export SWITCHYARD_ZIP=$2

#configure jboss
~/bin/$INSTANZ configure $CONFIG_DIR

#!/bin/bash

# One-Time setup for a new JBoss instance

MY_PATH=$(readlink -f $0)
BIN_DIR=`dirname $MY_PATH`
BASE_DIR=$BIN_DIR/..
CONFIGS_DIR=$BASE_DIR/configs
PRG=`basename $0`

JBOSS7=$BIN_DIR/jboss7

INSTANZ=myjboss
BIN="$HOME/bin"
PORT_OFFSET="0"
JBossPackage="$HOME/Downloads/jboss-eap-6.1.0.Alpha.zip"

#####################################################################
##                              print_usage
#####################################################################
print_usage(){
cat <<EOF 1>&2

usage: $PRG [-h] [-i name] [-j jboss_home] [-b dir] [-o port_offset] [-z jboss_zip]

Options:
    i name: Name of jboss instance ($INSTANZ)
    j jboss_home: JBOSS_HOME ($JB_HOME)
    b bin: Directory where the link to jboss7 script will be created. 
           Should be in PATH. ($BIN)
    o port_offset: Port offset for this instance ($PORT_OFFSET)
    z jboss_zip: JBoss package zip-file.  ($JBossPackage) Must exist!
    h: this help

Function:
	Sets up the basic environment to use JBoss Tools.
	These files will be created by this script:
	      $JBOSS_SKRIPT $RC_FILE
EOF
}

#####################################################################
##                              link_to_jboss7
#####################################################################
link_to_jboss7() {

	ln -s $JBOSS7 $1
}

######################   Optionen bestimmen ###################

while getopts "hi:j:b:o:z:" option
do
    case $option in
      h)
        PRINT_HELP="true";;
      i)
        INSTANZ=$OPTARG;;
      j)
        JB_HOME=$OPTARG;;
      b)
        BIN=$OPTARG;;
      o)
        PORT_OFFSET=$OPTARG;;
      z)
        JBossPackage=$OPTARG;;
      *)
        PRINT_HELP="true";;
    esac
done

shift `expr $OPTIND - 1`


##################### Beginn #########################

JBOSS_SKRIPT=$BIN/$INSTANZ
RC_FILE=$HOME/.${INSTANZ}rc

if [ x$FORCE_JBOSS_MAJOR_CODE = x ]; then
        FORCE_JBOSS_MAJOR_CODE=7
fi

if [ x$JB_HOME = x ]; then
	JB_HOME='$HOME'/jboss-$INSTANZ
fi

if [ x$PRINT_HELP = xtrue ]; then
	print_usage
	exit 0
fi

if [ ! -f "$JBossPackage" ]; then
	echo "JBoss package ($JBossPackage) doesn't exist!" 1>&2
	echo "Download package or use option '-z'" 1>&2
	exit 1
fi

echo "JBOSS_SKRIPT is at $JBOSS_SKRIPT" 
echo "RC_FILE is at $RC_FILE" 
echo
echo Use:
echo 	Instance: $INSTANZ
echo 	JBOSS_HOME: $JB_HOME
echo 	JBossPackage: $JBossPackage
echo 	PORT_OFFSET: $PORT_OFFSET
echo 	ENV_CONFIG: ../environments/example

if [ ! -d "$BIN" ]; then
	mkdir -p $BIN
fi
ln -s $JBOSS7 $JBOSS_SKRIPT

cat <<EOF > $RC_FILE
# Configuration for $INSTANZ
JBOSS_HOME=$JB_HOME
export JBossPackage=$JBossPackage

PORT_OFFSET=$PORT_OFFSET

export ENV_CONFIG=../environments/example
export FORCE_JBOSS_MAJOR_CODE=$FORCE_JBOSS_MAJOR_CODE

EOF

echo
echo Uninstall files with:
echo rm $JBOSS_SKRIPT $RC_FILE
echo
echo Install and start JBoss with:
echo $INSTANZ configure $CONFIGS_DIR/basic_setup
echo $INSTANZ configure $CONFIGS_DIR/database_with_template
echo $INSTANZ help

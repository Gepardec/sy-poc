#!/bin/bash

function checkSuccess {
	RETVAL=$?
	if [ "$RETVAL" != "0" ]
	then
        	echo "==== FAILURE code $RETVAL ===="
		cd $(echo $CURDIR)
       		exit 1
	fi
}

function maven {
	if [ "$SKIP_TESTS" == "true" ]
	then
        	mvn clean install -Dmaven.test.skip=true
		checkSuccess
	else
		mvn clean install
		checkSuccess
	fi
}

function setSystemProperty {
	PROPERTY_NAME=$1
	PROPERTY_VALUE=$2
	if [ "$PROPERTY_VALUE"!="" ]
	then
		echo "Trying to setup $PROPERTY_NAME=$PROPERTY_VALUE system property"
		$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="/system-property=$PROPERTY_NAME:remove"	
		$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="/system-property=$PROPERTY_NAME:add(value=$PROPERTY_VALUE)"
	fi
}

function printHelp {
	echo ""
	echo "================================================================================================================="
	echo "usage: $0 --env=<Path to env> [--all] [--sy-common] [--sy-main] [--sy-simulation] [--configure] [--skip-tests] [--help]"
	echo "-----------------------------------------------------------------------------------------------------------------"
	echo "Parameters"
	echo "-----------------------------------------------------------------------------------------------------------------"
	echo "--env: path to environment configuration (definition of JBOSS_HOME, ADMIN_PORT, MOTION, CONAX_HOME)"
	echo "--all: perform all build and installation steps (excluding jboss configuration)"
	echo "--sy-<component>: perform build and deployment of corresponding prototype component (common, main, simulation)"
	echo "--configure: perform necessary jboss and environment configuration steps (e.g. queues configuration, path to motion, create conax dirs)"
	echo "--skip-tests: use maven with skip test"
	echo "================================================================================================================="
	echo ""
	exit 0
}

BASEDIR=$(dirname $0)

# processing of inputed command
if [ $# == 0 ]
then
	printHelp
fi

export ALL=false

while test $# -gt 0; do
case "$1" in
                --help)
                        printHelp
			;;
		--all)
                        export ALL=true
                        shift
                        ;;
		--env*)
                        export ENV=`echo $1 | sed -e 's/^[^=]*=//g'`
                        shift
			;;
		--sy*)
                        VAR=`echo $1 | sed -r 's/--sy-//g'`
			export sy$VAR=true
			shift
                        ;;
		--configure)
			export CONFIGURE=true
			shift
                        ;;
		--skip-tests)
			export SKIP_TESTS=true
			shift
                        ;;
                *)
printHelp
;;
                        
        esac
done

if [ "$ENV" == "" ]
then
	echo "ENV is not set"
	printHelp
fi

if [[ ! -f $ENV ]]; then
    	echo "$ENV not exists"
	printHelp
fi

source $ENV

if [ "$JBOSS_HOME" == "" ]
then
	echo "JBOSS_HOME is not set"
	printHelp
fi

if [ "$ADMIN_PORT" == "" ]
then
	echo "ADMIN_PORT is not set"
	printHelp
fi

echo "==== START BUILD AND DEPLOY ===="

if [ "$CONFIGURE" == "true" ]
then
	echo "Trying to remove queue \"incomingQueue\""
	$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="/subsystem=messaging/hornetq-server=default/jms-queue=incomingQueue:remove"

	echo "Trying to install queue \"incomingQueue\""
	$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="/subsystem=messaging/hornetq-server=default/jms-queue=incomingQueue:add(entries=[\"/queue/incomingQueue\"])"
	checkSuccess

	echo "Trying to remove queue \"resultQueue\""
	$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="/subsystem=messaging/hornetq-server=default/jms-queue=resultQueue:remove"	
	
	echo "Trying to install queue \"resultQueue\""
	$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="/subsystem=messaging/hornetq-server=default/jms-queue=resultQueue:add(entries=[\"/queue/resultQueue\"])"
	checkSuccess

	setSystemProperty "sy.poc.motion" $MOTION

	if [ "$CONAX_HOME" != "" ]
	then
		echo "Trying to create conax directory"
		echo "mkdir -p $CONAX_HOME/input"
		mkdir -p $CONAX_HOME/input
		checkSuccess

		echo "mkdir -p $CONAX_HOME/ok"
		mkdir -p $CONAX_HOME/ok
		checkSuccess

		echo "mkdir -p $CONAX_HOME/err"
		mkdir -p $CONAX_HOME/err
		checkSuccess

		echo "mkdir -p $CONAX_HOME/output"
                mkdir -p $CONAX_HOME/output
                checkSuccess

		setSystemProperty "sy.poc.conax.dir" $CONAX_HOME		
	fi

	if [ "$SIMULATION_DIR" != "" ]
	then
		setSystemProperty "sy.poc.simulation.dir" $SIMULATION_DIR	
	fi

	if [ "$BATCH_DIR" != "" ]
	then
		echo "Trying to create batch directory"
		echo "mkdir -p $BATCH_DIR"
		mkdir -p $BATCH_DIR
		checkSuccess

		setSystemProperty "sy.poc.batch.dir" $BATCH_DIR
	fi
fi

CURDIR=$(pwd)

cd $BASEDIR
BASEDIR=`pwd`

##########################
#
# Build common
#
##########################
if [ "$sycommon" == "true" ] || [ "$ALL" == "true" ]
then
	cd sy-poc-common
	maven
fi

##########################
#
# Build sy simulation
#
##########################
if [ "$sysimulation" == "true" ] || [ "$ALL" == "true" ]
then	
	cd $BASEDIR
	cd sy-poc-simulation
	maven
	
	$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="undeploy sy-poc-simulation-0.0.1-SNAPSHOT.war"
	$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="deploy target/sy-poc-simulation-0.0.1-SNAPSHOT.war"
	checkSuccess
fi

##########################
#
# Build sy main
#
##########################
if [ "$symain" == "true" ] || [ "$ALL" == "true" ]
then
	cd $BASEDIR
	cd sy-poc-main
	maven
	
	$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="undeploy sy-poc-main-0.0.1-SNAPSHOT.war"
	$JBOSS_HOME/bin/jboss-cli.sh --connect --controller=localhost:$ADMIN_PORT --command="deploy target/sy-poc-main-0.0.1-SNAPSHOT.war"
	checkSuccess
fi

cd $CURDIR
echo "==== FINISHED ===="

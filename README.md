# Switchyard Proof Of Concept Project

## Prerequisites

* Wildfly 8.0.0.Final
* JBoss Switchyard 2.0.0.Alpha
* Unix-style OS. Tested on Linux (Fedora 20)

We used JBoss Switchyard 1.1.0.Final (maven dependencies) for implementation and Unit Tests and Switchyard 2.0.0.Alpha2 as a target platform.

## Purpose

The purpose of the project is to demonstrate how an enterprise application integration scenario can be implemented
using JBoss Switchyard as an integration platform.

## Scenario

## Installation and configuration

* Download Wildfly 8.0.0.Final zip package [here](http://wildfly.org/downloads/)
* Download JBoss Switchyard 2.0.0.Alpha zip package [here](http://switchyard.jboss.org/downloads)
* Clone project's git repository `git clone https://github.com/Gepardec/sy-poc.git`
* Execute `sy-poc-install/sy-poc-install.sh` script.
Script format:
```
sy-poc-install.sh path/to/wildfly-zip path/to/switchyard-zip [instance name] [port offset] [installation dir] [jboss major varsion]
  where
  path/to/wildfly-zip		is the path to the downloaded Wildfly 8.0.0.Final zip package
  path/to/switchyard-zip	is the path to the downloaded JBoss Switchyard 2.0.0.Alpha zip package
  instance					name name of JBoss instance that will be installed
  port offset				port offset for the JBoss instance to be installed
  installation dir			directory where JBoss instance will be installed
  jboss major version		major version of jboss package (7 for AS 7, 8 for Wildfly)
  
Example: ./sy-poc-install.sh ~/Downloads/wildfly-8.0.0.Final.zip ~/Downloads/switchyard-2.0.0.Alpha2-WildFly.zip switchyard 0 ~/jbosses/jboss-switchyard 8
```
* Adjust environment configuration example.env to your execution environment

Variables to be configured:
```
  PROJECT_HOME				directory where project was cloned to
  JBOSS_HOME				directory where JBoss instance is installed
  ADMIN_PORT				9999 for AS 7, 9990 for Wildfly
  MOTION					path to the motion subsystem script (must not be changed)
  CONAX_HOME				path to the conax subsystem home directory
  BATCH_DIR					path to the directory that will be listened for batch files
  SIMULATION_DIR			path to the directory for input and result messages for simulation
```
Only two first variables are needed to be adjusted. The remaining ones can be used as is without changes.
* Configure JBoss for Switchyard PoC Application

`sy-poc-build.sh --env=example.env --configure`

This script will create all necessary system properties and JMS queues in JBoss instance as well as all necessary directories.

The JBoss instance is now ready.

## Build and deployment

For build and deployment of the application the `sy-poc-build.sh` script in the root of project directory is provided.
Script format:
```
/sy-poc-build.sh --env=<Path to env> [--all] [--sy-common] [--sy-main] [--sy-simulation] [--configure] [--skip-tests] [--help]
  --env						path to environment configuration file (definition of JBOSS_HOME, ADMIN_PORT, MOTION, CONAX_HOME ...)
  --all						perform build and deployment of all subprojects
  --sy-<subproject name>:	perform build and deployment of corresponding subproject (common, main or simulation)
  --configure				perform necessary jboss and environment configuration steps
  --skip-tests				use maven with -DskipTests
  --skip-tests				show help dialog
```

Subsystems:

* *common* contains classes that are common for all subprojects like utilities, common interfaces etc. If
some classes are changed in this project the application must be rebuilt with the key `--all`
* *simulation* contains classes, scripts and SY-application for simulating *conax*, *motion* and *incognito* subsystems.
If some classes are changed in this project the application must be rebuilt with the key `--sy-simulation`
* *main* contains main switchyard application. If some classes or configuration classes are changed,
application must be rebuilt with the key --sy-main

Example: `./sy-poc-build.sh --env=default.env --sy-main --skip-tests`

This information can be useful for first run:

* To trigger application it is sufficient to copy a batch file into batch directory.
* By default the batch directory is > $PROJECT_DIR/tmp/batch
* Default batch file with a single message is > sy-poc-main/src/test/resources/single_request.xml
* Default batch file with two messages is > sy-poc-main/src/test/resources/batch_request.xml
* By default the result of execution will be saved under > $PROJECT_DIR/tmp/simulation/result

To control JBoss instance use <instance name> command in shell (switchyard) by default.

For example 

* `switchyard start` starts the server.
* `switchyard log` displays server log.
* `switchyard help` shows help screen.
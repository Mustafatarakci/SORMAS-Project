#!/bin/bash

DIR=$(dirname "$0")
ASADMIN="${DIR}/../../payara5/bin/asadmin"
${ASADMIN} stop-domain --domaindir "${DIR}/../" sormas
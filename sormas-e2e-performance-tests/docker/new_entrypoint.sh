#!/bin/bash
cd /srv/sormas/docker/ || exit
./template_config.sh

head -n100 /srv/sormas/SormasPoc.jmx


cd "${JMETER_HOME}" || exit
/entrypoint.sh "$@"
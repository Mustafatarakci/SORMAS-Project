#!/bin/bash
#
# SORMAS® - Surveillance Outbreak Response Management & Analysis System
# Copyright © 2016-2022 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <https://www.gnu.org/licenses/>.
#

sed -i "s/\$HOST/${HOST}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$PORT/${PORT}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$URL_SCHEME/${URL_SCHEME}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$USER/${USER}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$PASSWORD/${PASSWORD}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$REGION/${REGION}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$DISTRICT/${DISTRICT}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$COMMUNITY/${COMMUNITY}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$NAT_USER/${NAT_USER}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$SUR_OFF/${SUR_OFF}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$REST_USER/${REST_USER}/" /srv/sormas/SormasPoc.jmx
sed -i "s/\$HOSPITAL_INFORMANT/${HOSPITAL_INFORMANT}/" /srv/sormas/SormasPoc.jmx

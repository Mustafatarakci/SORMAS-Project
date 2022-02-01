FROM justb4/jmeter

RUN wget https://www.vinsguru.com/download/87/?_ga=2.188716968.1652712557.1623012940-501989637.1623012940 \
    -O $JMETER_HOME/lib/ext/tag-jmeter-extn-1.1.zip

RUN mkdir /srv/sormas && mkdir -p /srv/out/reports
COPY . /srv/sormas
RUN chmod +x /srv/sormas/docker/new_entrypoint.sh
RUN chmod +x /srv/sormas/docker/template_config.sh
ENTRYPOINT ["/srv/sormas/docker/new_entrypoint.sh"]
CMD ["-n", "-t", "/srv/sormas/SormasPoc.jmx", "-Jmodule=daily-build-scenario", "-l", "/srv/out/results.csv", "-e",  "-o", "/srv/out/reports"]
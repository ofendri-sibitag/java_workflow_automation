FROM openjdk:11

RUN apt-get update && apt-get install -y cron iputils-ping

ENV JOB_DESCRIPTION="My job description"
ENV JOB="my_job"
ENV INSTANCE="my_instance"

WORKDIR /app

COPY *.java /app/

RUN javac *.java

RUN touch /etc/crontab
RUN touch /app/exec_count.txt
RUN echo 0 > /app/exec_count.txt

RUN echo '*/2 * * * * root /usr/local/openjdk-11/bin/java -cp /app Main >> /proc/1/fd/1' >> /etc/crontab
RUN echo '* * * * * root /usr/local/openjdk-11/bin/java -cp /app UpMetricHandler >> /proc/1/fd/1' >> /etc/crontab

CMD ["/bin/bash", "-c", "printenv > /etc/environment && cron -f"]

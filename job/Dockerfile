FROM ubuntu:latest

ENV VERSION=1.2.0

RUN apt-get update && apt-get install -y --no-install-recommends \
    zip \
    unzip \ 
    vim \
    python3 \
    python3-pip \
    && rm -rf /var/lib/apt/lists/*

COPY zip_job.py /tmp/zip_job.py

CMD ["bash", "-c", "echo 'Operating System and Architecture:' && echo '---------------------------------------------------' && uname -a && echo '---------------------------------------------------' && echo 'Verifying file existence in /tmp:' && ls -l /tmp/zip_job.py"]

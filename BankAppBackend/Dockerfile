FROM tomcat:11.0-jdk21-openjdk-bookworm
RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/BankApp.war /usr/local/tomcat/webapps/ROOT.war
COPY server.xml /usr/local/tomcat/conf/server.xml
EXPOSE 9090
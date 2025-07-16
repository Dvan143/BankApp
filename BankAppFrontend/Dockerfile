FROM nginx:stable-alpine3.21-perl
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY html /usr/share/nginx/html/
COPY static /usr/share/nginx/html/
EXPOSE 80
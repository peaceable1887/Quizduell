FROM nginx

RUN rm /etc/nginx/conf.d/default.conf
RUN mkdir /etc/nginx/sites-enabled/

COPY sites-enabled/ /etc/nginx/sites-enabled
COPY nginx.conf /etc/nginx/
FROM nginx:alpine

COPY ./dist /usr/share/nginx/html
COPY ./entrypoint.sh /entrypoint.sh

EXPOSE 80

ENTRYPOINT ["/entrypoint.sh"]
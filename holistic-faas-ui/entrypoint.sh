#!/bin/sh

# Replace environment variables
envsubst < /usr/share/nginx/html/config.template.js > /usr/share/nginx/html/config.js

# Start NGINX
exec nginx -g 'daemon off;'

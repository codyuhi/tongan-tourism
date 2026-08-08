FROM nginx:1.27.5-alpine-slim AS production-stage
COPY ./app /usr/share/nginx/html
EXPOSE 8083
CMD ["nginx", "-g", "daemon off;"]
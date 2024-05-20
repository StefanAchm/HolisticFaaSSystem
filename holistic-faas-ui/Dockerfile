# Use a base image with Node.js
FROM node:14

# Set the working directory
WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the application
RUN npm run build

# Use a minimal nginx image to serve the Vue.js application
FROM nginx:alpine
COPY --from=0 /app/dist /usr/share/nginx/html

# Expose the port that nginx runs on
EXPOSE 80

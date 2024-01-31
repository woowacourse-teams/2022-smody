# Start your image with a node base image
FROM node:18-alpine

RUN rm /usr/local/bin/yarn
RUN rm /usr/local/bin/yarnpkg && npm install -g yarn

# The /app directory should act as the main application directory
WORKDIR /app

# Copy the app package and package-lock.json file
COPY package*.json ./
COPY yarn.lock ./yarn.lock

# Copy local directories to the current local directory of our docker image (/app)
COPY . ./

# Install node packages, install serve, build the app, and remove dependencies at the end
RUN yarn \
    && yarn global add serve \
    && yarn run build:prod \
    && yarn cache clean

EXPOSE 3000

# Start the app using serve command
CMD [ "serve", "-s", "dist" ]
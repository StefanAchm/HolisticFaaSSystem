
# Preparation (initial setup)

### 1. Credentials

- [ ] User adds credentials to the HMS
  - [ ] AWS
    - [x] Basic functionality 
    - [ ] Check Role ARN (does this need to be created by the user or the HMS can create it?)
  - [ ] GCP
    - [x] Basic functionality
    - [ ] Check Service Account (is this the right way to do it?)
  - [ ] Other
- [ ] Credentials handling (where to store, so that this is secure)

### 2. Upload Function to HMS

- [ ] User uploads a function to the HMS
  - [x] With some info (name, description)
  - [ ] Handle file types
    - [x] Zip file for java functions
    - [ ] Other file types (jar, py, etc.)
  - [ ] Handle file sources
    - [x] File upload from local machine --> upload the file to the HMS
    - [ ] File from URL
    - [ ] Other sources (git, etc.)
- [x] HMS stores the function in the database

### 3. Deployment of a function

- [ ] User deploys the function
  - [ ] User selects the function
    - [x] Currently, each deployment will create a new function on the provider side and in the database
  - [ ] User enters the metadata of the function (Name, Description, Provider, Region, etc.)
    - [x] Basic metadata of the function 
    - [ ] Other metadata of the function
  - [x] HMS deploys the function to specified provider with the specified metadata
  - [x] HMS stores the metadata of the function in the database
    - [x] Create new entry for each deployment

# Use Cases

### 1. Deployment of a single function from one to another region
### 2. Deployment of a single function from one to another provider
### 3. Deployment of a single function from one to another user
### 4. Bulk deployment (from deployment file)

### Other Function handling

- [ ] List all functions
- [ ] Delete a function
- [ ] Update a function (redeploy)
- [ ] Get the status of a function (and its relevant services, like buckets etc.)
- [ ] GCP: handle bucket?
- [ ] Upload to HMS: max file size?

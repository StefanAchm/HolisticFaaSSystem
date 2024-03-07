
# Preparation (initial setup)

### 1. Credentials

- [ ] User adds credentials to the HMS
  - [ ] AWS
  - [ ] GCP
  - [ ] Other

### 2. Upload Function to HMS

- [ ] User uploads a function to the HMS
  - [ ] With some metadata (name, description, region, etc.)
    - [ ] ⚠️️TODO⚠️: Probably no metadata at this point, but at the deployment (step 3)
  - [ ] Handle file types
    - [ ] Zip file
    - [ ] Other file types (jar, py, etc.)
  - [ ] Handle file sources
    - [ ] Local file
    - [ ] File from URL
    - [ ] Other sources (git, etc.)
- [ ] HMS stores the metadata of the function in the database

### 3. Deployment of a function

- [ ] User deploys the function
  - [ ] User selects the function
    - [ ] Currently, each deployment will create a new function on the provider side and in the database
  - [ ] User enters the metadata of the function
    - [ ] Name
    - [ ] Description
    - [ ] Provider
    - [ ] Region
    - [ ] Other metadata of the function
  - [ ] HMS deploys the function to specified provider with the specified metadata
  - [ ] HMS stores the metadata of the function in the database
    - [ ] Create new entry for each deployment

# Use Cases

### 1. Deployment of a single function from one to another region
### 2. Deployment of a single function from one to another provider
### 3. Deployment of a single function from one to another user

### Other Function handling

- [ ] List all functions
- [ ] Delete a function
- [ ] Update a function (redeploy)
- [ ] Get the status of a function (and its relevant services, like buckets etc.)
- [ ] GCP: handle bucket?
- [ ] Upload to HMS: max file size?

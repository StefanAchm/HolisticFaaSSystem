# Testing structure of the application

## Preparations

- Make sure to have a clean database
- Make sure to have clean cloud provider accounts
- Make sure to have a clean store (browser)

## 1. User Management

- Register a new user
  - Validate wrong inputs
- Login with the new user
  - Validate wrong inputs
- Add cloud provider credentials (AWS and GCP)
  - Make sure to add correct credential files
- Test also with AWS academy account
  - Therefore, a user needs to add a token for the academy account


## 2. Workflow Management

### 2.1 Create a new workflow from AFCL file

- Upload a valid AFCL file
  - Validate wrong inputs
- Add function implementations (AWS and GCP) to each function 

### 2.2 Create a new workflow from scratch

TODO

### 2.3 Deploy a workflow

- Create a Workflow deployment for one of the cloud providers
- Deploy the workflow
  - Important: check, if the functions are deployed correctly
- Download the new AFCL file
  - Important: check, if the new AFCL file contains the correct ARNS (for AWS) and Function URLs (for GCP)

### 2.4 Migrate a workflow

- Use one of the deployed workflows and migrate it:
  - to another cloud provider (need to select different implementations!)
  - to another region
  - with different memory sizes/timeout values/etc.
  - to another account (needed 2. account for that!)
- Deploy the migrated workflow
  - Important: check, if the functions are deployed correctly
- Download the new AFCL file
  - Important: check, if the new AFCL file contains the correct ARNS (for AWS) and Function URLs (for GCP)
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

### AWS Credentials (properties)
```properties
aws.accessKeyId=YOUR_ACCESS_KEY
aws.secretAccessKey=YOUR_SECRET_KEY

# Optional, you can add a role ARN, eg:
aws.roleArn=YOUR_ROLE_ARN
```

### GCP Credentials (JSON)
```json

{
  "type": "service_account",
  "project_id": "YOUR_PROJECT_ID",
  "private_key_id": "YOUR_PRIVATE_KEY_ID",
  "private_key": "YOUR_PRIVATE_KEY", 
  "client_email": "YOUR_CLIENT_EMAIL",
  "client_id": "YOUR_CLIENT_ID",
  "auth_uri": "...",
  "token_uri": "...",
  "auth_provider_x509_cert_url": "...",
  "client_x509_cert_url": "...",
  "universe_domain": "googleapis.com"
}

```


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
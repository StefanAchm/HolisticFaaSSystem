# How to use this application

## 1. Configure your user account

- In the navigation bar, click on the `Profile` button.
- Upload user credentials for the providers (AWS and GCP) you want to use.
- Click on the `Save` button.

### 1.1. AWS Credentials file (e.g. aws.properties):

1. Click on your user name in the upper right corner and select the `Security credentials` button.
2. Go to your `Access keys` and click on the `Create New Access Key` button.
3. Create the key, download the credentials and store them in a file, like the following example:

```properties
aws.accessKeyId=YOUR_ACCESS_KEY
aws.secretAccessKey=YOUR_SECRET_KEY

# Optional, you can add a role ARN (otherwise the application will create one for you), eg:
aws.roleArn=YOUR_ROLE_ARN
```

### 1.2. GCP Credentials file (e.g. gcp.json):

1. Create a service account in the 'IAM & Admin' section of the GCP console. 
2. Open the service account and click on the `Keys` button.
3. Create a new key and download the JSON file, which contains the credentials, like the following example:

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

### 1.3. AWS Academy account:

- If you are an AWS Academy student you can use the `AWS Session token` button in the navigation bar to set a
  temporary session token. (You receive this token in the AWS Academy portal)

## 2. Create a new Workflow

1. In the navigation bar, click on the `Workflows` button.
2. Click on the 'Add' button.
   - You can now either create a new workflow from scratch or import a Workflow from an AFCL file.
   - NOTE: If you import a Workflow, the system automatically creates the necessary functions for you.
3. Click on the `Save` button.
4. You can now see the new workflow in the list of workflows.

## 3. Upload Function implementations

1. In the navigation bar, click on the `Workflows` button.
2. Select one of the workflows in the list.
3. Click on the `Types` button.
   - You can now see the list of function-types in the workflow and add new function implementations for each type.
   - NOTE: Give the function implementation a name with e.g. the provider name in it (e.g. AWS implementation). This is
       necessary to distinguish between different implementations of the same function-type in the next step.

## 4. Create a Workflow Deployment

### 4.1. Create a new deployment

1. In the navigation bar, click on the `Workflows` button.
2. Select one of the workflows in the list.
3. Click on the `Add` button.
   - You can now give the deployment a name and select all properties (e.g. provider, region, implementation, memory, etc.) for each function in the workflow.
4. Click on the `Save` button.

### 4.2 Migrate from an existing deployment

1. In the navigation bar, click on the `Workflows` button.
2. Select one of the workflows in the list.
3. Click on the `Deployments` button.
4. Select the deployment you want to migrate.
5. Click on the `Migrate` button and choose one of the following options:
   - Migrate `to my account` (if you want to deploy the exast same workflow deployment in your account)
   - Migrate `to AWS` (if you want to deploy the workflow on AWS with your account)
   - Migrate `to GCP` (if you want to deploy the workflow on GCP with your account)
   - Migrate `Custom` (if you want to manually change properties for the deployment)
6. On the new dialog, you can now change the properties for the deployment, give it a name and click on the `Save` button.

## 5. Start the deployment

1. In the navigation bar, click on the `Workflows` button.
2. Select one of the workflows in the list.
3. Click on the `Deployments` button.
4. Select the deployment you want to deploy.
5. Click on the `Deploy` button. 
   - NOTE: This deploys all functions at the same time in paralle, so it may take a while and may lead to errors
   - You can also deploy functions separately, by clicking on the `Deploy` button in the list of functions.
6. You can now see the status of the deployment in the list of functions.
7. After the deployment is finished, you can:
  - Navigate to the function on the provider's website by clicking on the `Open` icon.
  - Download the YAML file with the updated resources by clicking on the `Download` button.



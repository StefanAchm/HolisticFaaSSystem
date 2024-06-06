from storage.pyStorageProvider import pyStorageProvider
from storage.pyObjectInfo import pyObjectInfo
import re
import boto3
import json


class pyUrlParser:

    def parseToPyObjectInfo(input : str, just_bucket : bool = False ): 
        #check if amazon s3
        amazon = pyUrlParser.parseAmazonUrl(input, just_bucket)

        #check if cloud storage
        google = pyUrlParser.parseGoogleUrl(input, just_bucket)

        if amazon is not None:
            return pyObjectInfo(pyStorageProvider.AWS, amazon['bucket'], amazon['file'], amazon['region'])
        if google is not None:
            return pyObjectInfo(pyStorageProvider.GCP, google['bucket'], google['file'], None)

        else: 
            return None

    def parseAmazonUrl(input : str, just_bucket : bool):
        prefix = ""
        if input.startswith("arn:aws:s3"):
            bucket_name = input[len("arn:aws:s3:::"):].split("/")[0]
            prefix = f"arn:aws:s3:::{bucket_name}/"
        elif input.startswith("s3://"):
            bucket_name = input[len("s3://"):].split("/")[0]
            prefix = f"s3://{bucket_name}/"
        elif re.search(".s3.*amazonaws.com/", input) is not None:
            bucket_name = input[len("https://"):].split(".")[0]
            prefix = f'https://{bucket_name}.s3.*amazonaws.com/'
        else: 
            return None

        if not just_bucket:
            file_name = re.sub(prefix, '', input)
        else: 
            file_name = None
        

        region = pyUrlParser.get_aws_region(bucket_name)

        return {'bucket': bucket_name, 'file': file_name, 'region': region}
        

    def parseGoogleUrl(input: str, just_bucket : bool): 
        prefix = ""
        if input.startswith("gs://"):
            prefix = "gs://"
        elif re.match("http://", input) and input[len("http://"):].startswith("storage.cloud.google.com"):
            prefix = "http://storage.cloud.google.com/"

        elif re.match("https://", input) and input[len("https://"):].startswith(("storage.cloud.google.com")):
            prefix = "https://storage.cloud.google.com/"
        else: 
            return None
        bucket_name = input[len(prefix):].split("/")[0]

        if not just_bucket:
            file_name = input[len(f"{prefix}{bucket_name}/"):]
        else: 
            file_name = None

        return {'bucket': bucket_name, 'file': file_name}
    

    def isAmazonParsable(input: str):
        return input.startswith("arn:aws:s3") or input.startswith("s3://") or (re.search(".s3.*amazonaws.com/", input) is not None)

    def isGoogleParsable(input: str):
        return input.startswith("gs://") or input.startswith("http://storage.cloud.google.com/") or input.startswith("https://storage.cloud.google.com/")

    def isLocal(input: str):
        return input.startswith("/")


    def get_aws_region(bucket):
        credentials = json.load(open('/tmp/credentials.json'))
        session = boto3.Session(
            aws_access_key_id = credentials['amazon']['api_access_key'],
            aws_secret_access_key = credentials['amazon']['api_secret_key'],
            aws_session_token = credentials['amazon']['api_session_token']
        )
        s3_client = session.client('s3')
        bucket_location_response = s3_client.get_bucket_location(Bucket=bucket)

        region = bucket_location_response['LocationConstraint']
        if region == None:
            region = "us-east-1"

        return region

    def retrieve_file_name(input_str):
        prefix = ""
        if input_str.startswith("gs://"):
            prefix = "gs://"
        elif re.match("http://", input_str) and input_str[len("http://"):].startswith("storage.cloud.google.com"):
            prefix = "http://storage.cloud.google.com/"
        elif re.match("https://", input_str) and input_str[len("https://"):].startswith(("storage.cloud.google.com")):
            prefix = "https://storage.cloud.google.com/"
        if prefix != "":
            bucket_name = input_str[len(prefix):].split("/")[0]
            prefix = prefix + bucket_name + "/"
        else:
            if input_str.startswith("arn:aws:s3"):
                bucket_name = input_str[len("arn:aws:s3:::"):].split("/")[0]
                prefix = f"arn:aws:s3:::{bucket_name}/"
            elif input_str.startswith("s3://"):
                bucket_name = input_str[len("s3://"):].split("/")[0]
                prefix = f"s3://{bucket_name}/"
            elif re.search(".s3.*amazonaws.com/", input_str) is not None:
                bucket_name = input_str[len("https://"):].split(".")[0]
                prefix = f'https://{bucket_name}.s3.*amazonaws.com/'
        file_name = re.sub(prefix, '', input_str)
        return file_name

from multimethod import multimethod
from storage.pyUrlParser import pyUrlParser
from storage.pyObjectInfo import pyObjectInfo
from storage.driver.pyStorageDriver import pyStorageDriver
from storage.pyStorageProvider import pyStorageProvider
import logging as log
import json

# main class to connect driver and url parser


class pyStorage:

    # this function determines which of the possibilities is at hand
    @multimethod
    def copy(source: str, target: str):
        # local to local is not possible
        if pyUrlParser.isLocal(source) and pyUrlParser.isLocal(target):
            log.error(
                "You cannot copy a local file to a local file. Please submit a correct input.")

        # uploading from local to an amazon or a google bucket
        elif pyUrlParser.isLocal(source) and not pyUrlParser.isLocal(target):
            target_info = pyUrlParser.parseToPyObjectInfo(target)
            if target_info is not None:
                pyStorage.copy(source, target_info)
            else:
                log.error(
                    "Target input could not be parsed and is also not local. Please submit a correct input.")

        # downloading from an amazon or a google bucket to local
        elif not pyUrlParser.isLocal(source) and pyUrlParser.isLocal(target):
            source_info = pyUrlParser.parseToPyObjectInfo(source)
            if source_info is not None:
                pyStorage.copy(source_info, target)
            else:
                log.error(
                    "Source input could not be parsed and is also not local. Please submit a correct input.")

        # copy from an amazon or a google bucket to an amazon or a google bucket
        elif not pyUrlParser.isLocal(source) and not pyUrlParser.isLocal(target):
            source_info = pyUrlParser.parseToPyObjectInfo(source)
            target_info = pyUrlParser.parseToPyObjectInfo(target)
            if source_info is not None:
                if target_info is not None:
                    pyStorage.copy(source_info, target_info)
                else:
                    log.error(
                        "Target input could not be parsed and is also not local. Please submit a correct input.")
            else:
                log.error(
                    "Source input could not be parsed and is also not local. Please submit a correct input.")

        # if all possibilities are not true
        else:
            log.error("System error: Unknown inputs")

    # function gets called when the copy above determines to upload a file
    @multimethod
    def copy(source: str, target_info: pyObjectInfo):
        driver = pyStorageDriver()
        driver.upload_file(source, target_info)

    # function gets called when the copy above determines to download a file
    @multimethod
    def copy(source_info: pyObjectInfo, target: str):
        driver = pyStorageDriver()
        driver.download_file(source_info, target)

    # function gets called when the copy above determines to copy a file
    @multimethod
    def copy(source_info: pyObjectInfo, target_info: pyObjectInfo):
        driver = pyStorageDriver()
        driver.copy_file(source_info, target_info)

    # function to copy a whole bucket
    def copy_bucket(source: str, target: str):
        source_info = pyUrlParser.parseToPyObjectInfo(source, True)
        target_info = pyUrlParser.parseToPyObjectInfo(target, True)
        if source_info is not None:
            if target_info is not None:
                driver = pyStorageDriver()
                driver.copy_bucket(source_info, target_info)
            else:
                log.error(
                    "Target input could not be parsed. Please submit a valid url.")
        else:
            log.error(
                "Source input could not be parsed. Please submit a valid url.")

    # function to create a bucket
    def create_bucket(bucket_name: str, provider: pyStorageProvider, region):
        driver = pyStorageDriver()
        driver.create_bucket(bucket_name, provider, region)

    # function to delete a bucket
    # delete_if_not_empty informs the function if the bucket should only be deleted if it is empty (False)
    # or if it should delete the content of the bucket (True)
    def delete_bucket(bucket: str, delete_if_not_empty: bool = False):
        driver = pyStorageDriver()
        object_to_delete = pyUrlParser.parseToPyObjectInfo(bucket)
        driver.delete_bucket(
            object_to_delete, delete_if_not_empty=delete_if_not_empty)

    # creates a credentials file that includes information to access buckets from a lambda function or a cloud function
    # credentials file should not be included in the layer, as it is less safe than passing parameters to the lambda function
    # default for every parameter is an empty string, so that one can call the function only with the amazon credentials or with the google credentials as well
    def create_cred_file(aws_access_key_id="", aws_secret_key="", aws_session_token="", gcp_client_email="", gcp_private_key="", gcp_project_id=""):
        with open('/tmp/credentials.json', 'w', encoding='utf-8') as credentials_file:
            credentials_file.write(json.dumps(
                {
                    "amazon":
                        {
                            "api_access_key": aws_access_key_id,
                            "api_secret_key": aws_secret_key,
                            "api_session_token": aws_session_token

                        },
                    "google":
                        {
                            "client_email": gcp_client_email,
                            "private_key": gcp_private_key,
                            "project_id": gcp_project_id
                        }
                }))

    def retrieve_file_name(input_str):
        return pyUrlParser.retrieve_file_name(input_str)

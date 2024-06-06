
from storage.pyStorageProvider import pyStorageProvider


class pyObjectInfo:
    def __init__(self, provider: pyStorageProvider, bucket_name: str, file_name: str, region: str = None):
        self.provider = provider
        self.bucket_name = bucket_name
        self.file_name = file_name
        self.region = region
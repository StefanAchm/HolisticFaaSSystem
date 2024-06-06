import json
from time import sleep

def lambda_handler(event, context):
    
    sleep(0.05)
    return {
        'fligth': 'BA2490'
    }
import json
from time import sleep

def lambda_handler(event, context):
    
    sleep(0.2)
    return {
        'iterator': { 'count': 1, 'index': 0, 'step': 1 },
        'passengers': ['KRO99ED12'],
        'count': 1
    }
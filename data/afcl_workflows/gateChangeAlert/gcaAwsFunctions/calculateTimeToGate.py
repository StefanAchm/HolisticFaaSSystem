import json
from time import sleep

def lambda_handler(event, context):
    
    sleep(1)
    return {
        'minutesToGate': 80
    }
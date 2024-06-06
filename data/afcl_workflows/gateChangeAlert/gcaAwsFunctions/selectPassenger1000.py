import json
from time import sleep

def lambda_handler(event, context):
    sleep(0.2)
    count = 1000
    arr = []
    for i in range(0, count):
        arr.append("KRO99ED1" + str(i))
    return {
        'iterator': { 'count': 1000, 'index': 0, 'step': 1 },
        'passengers': arr,
        'count': 1000
    }
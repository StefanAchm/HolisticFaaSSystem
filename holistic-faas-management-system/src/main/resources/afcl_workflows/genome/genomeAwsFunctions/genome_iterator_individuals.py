import json

def lambda_handler(event, context):
 
    if 'iterator' in event.keys():
        index = event['iterator']['index']
        step = event['iterator']['step']
        count = event['iterator']['count']
    else:
        index = 0
        step = 1
        count = 10
        
    index += step
    
    return {
        'iterator': {
            'index' : index,
            'step' : step,
            'count' : count,
            'continue' : index <=count
        },
        "key_input": "ALL.chr22.80000.vcf.gz",
    	"counter": (index-1)*1000+1,
    	"stop": (index)*1000+1,
    	"chromNr": "22",
    	"keys": [
            "chr22n-1-1001.tar.gz",
            "chr22n-1001-2001.tar.gz",
            "chr22n-2001-3001.tar.gz",
            "chr22n-3001-4001.tar.gz",
            "chr22n-4001-5001.tar.gz",
            "chr22n-5001-6001.tar.gz",
            "chr22n-6001-7001.tar.gz",
            "chr22n-7001-8001.tar.gz",
            "chr22n-8001-9001.tar.gz",
            "chr22n-9001-10001.tar.gz"
        ]
    }
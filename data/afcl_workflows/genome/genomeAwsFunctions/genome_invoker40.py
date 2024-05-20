import json
import time
import boto3
from concurrent.futures import ThreadPoolExecutor

def lambda_handler(event, context):
    start_total = time.time()
    lambda_client = boto3.client('lambda')
    
    # Invoke Individuals and sifting
    start_ind_sifting = time.time()
    with ThreadPoolExecutor(max_workers=41) as executor:
        futs = []
        for x in range(0, 41):
            if(x == 40):
                payload_sifting = {
                  "key": "ALL.chr22.phase3_shapeit2_mvncall_integrated_v5.20130502.sites.annotation.vcf.gz",
                  "chromNr": "22"
                }
                
                futs.append(
                    executor.submit(lambda_client.invoke,
                        FunctionName   = "genome_sifting",
                        InvocationType = "RequestResponse",
                        Payload        = bytes(json.dumps(payload_sifting), "utf-8")
                    )
                )
            else:
                payload_individual = {
                  "key_input": "ALL.chr22.80000.vcf.gz",
                  "counter": x*250 + 1,
                  "stop": (x+1)*250 + 1,
                  "chromNr": "22"
                }
                
                futs.append(
                    executor.submit(lambda_client.invoke,
                        FunctionName   = "genome_individual",
                        InvocationType = "RequestResponse",
                        Payload        = bytes(json.dumps(payload_individual), "utf-8")
                    )
                )
    
    results = [ fut.result() for fut in futs ]
    end_ind_sifting = time.time()
    
    # Invoke individuals merge
    start_ind_merge = time.time()
    payload_individuals_merge = {
      "keys": [
          "chr22n-1-251.tar.gz",
          "chr22n-251-501.tar.gz",
          "chr22n-501-751.tar.gz",
          "chr22n-751-1001.tar.gz",
          "chr22n-1001-1251.tar.gz",
          "chr22n-1251-1501.tar.gz",
          "chr22n-1501-1751.tar.gz",
          "chr22n-1751-2001.tar.gz",
          "chr22n-2001-2251.tar.gz",
          "chr22n-2251-2501.tar.gz",
          "chr22n-2501-2751.tar.gz",
          "chr22n-2751-3001.tar.gz",
          "chr22n-3001-3251.tar.gz",
          "chr22n-3251-3501.tar.gz",
          "chr22n-3501-3751.tar.gz",
          "chr22n-3751-4001.tar.gz",
          "chr22n-4001-4251.tar.gz",
          "chr22n-4251-4501.tar.gz",
          "chr22n-4501-4751.tar.gz",
          "chr22n-4751-5001.tar.gz",
          "chr22n-5001-5251.tar.gz",
          "chr22n-5251-5501.tar.gz",
          "chr22n-5501-5751.tar.gz",
          "chr22n-5751-6001.tar.gz",
          "chr22n-6001-6251.tar.gz",
          "chr22n-6251-6501.tar.gz",
          "chr22n-6501-6751.tar.gz",
          "chr22n-6751-7001.tar.gz",
          "chr22n-7001-7251.tar.gz",
          "chr22n-7251-7501.tar.gz",
          "chr22n-7501-7751.tar.gz",
          "chr22n-7751-8001.tar.gz",
          "chr22n-8001-8251.tar.gz",
          "chr22n-8251-8501.tar.gz",
          "chr22n-8501-8751.tar.gz",
          "chr22n-8751-9001.tar.gz",
          "chr22n-9001-9251.tar.gz",
          "chr22n-9251-9501.tar.gz",
          "chr22n-9501-9751.tar.gz",
          "chr22n-9751-10001.tar.gz"
        ],
        "chromNr": 22
    }
    with ThreadPoolExecutor(max_workers=1) as executor:
        fut_individuals_merge = []
        fut_individuals_merge.append(
            executor.submit(lambda_client.invoke,
                FunctionName   = "genome_individuals_merge",
                InvocationType = "RequestResponse",
                Payload        = bytes(json.dumps(payload_individuals_merge), "utf-8")
            )
        )
    results_merge = [ fut.result() for fut in fut_individuals_merge ]
    end_ind_merge = time.time()
    
    # Invoke mutual overlap and frequency
    start_mut_freq = time.time()
    with ThreadPoolExecutor(max_workers=14) as executor:
        pop_arr = ["EUR", "AFR", "EAS", "ALL", "GBR", "SAS", "AMR"]
        
        for x in range(0, 7):
            payload_mut_freq = {
                "chromNr": "22",
                "POP": pop_arr[x],
                "bucketName": "genome-project"
            }
            executor.submit(lambda_client.invoke,
                FunctionName   = "genome_mutual_overlap",
                InvocationType = "RequestResponse",
                Payload        = bytes(json.dumps(payload_mut_freq), "utf-8")
            )
            executor.submit(lambda_client.invoke,
                FunctionName   = "genome_frequency",
                InvocationType = "Event",
                Payload        = bytes(json.dumps(payload_mut_freq), "utf-8")
            )
    # Since after 30 sec we get a timeout, we cannot wait for a response here. So, for freq
    # we do not wait for repsonse on thread, bur check for elements in s3 bucket.
    elements = 0
    while(elements < 15):
        # TODO maybe remove this (but freq takes quite long)
        #time.sleep(2)
        s3 = boto3.client('s3')
        response = s3.list_objects(
                Bucket='genome-project',
                Prefix='output/',
        )
        elements = len(response['Contents'])
    end_mut_freq = time.time()
    
    end_total = time.time()
    return {
        'status': 'finished successfully',
        'totalTime': (end_total-start_total),
        'totalIndividualSifting': (end_ind_sifting - start_ind_sifting),
        'totalIndividualMerge': (end_ind_merge - start_ind_merge),
        'totalMutFreq': (end_mut_freq - start_mut_freq)
    }

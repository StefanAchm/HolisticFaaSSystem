import json
import time
import boto3
from concurrent.futures import ThreadPoolExecutor

def lambda_handler(event, context):
    start_total = time.time()
    lambda_client = boto3.client('lambda')
    
    # Invoke Individuals and sifting
    start_ind_sifting = time.time()
    with ThreadPoolExecutor(max_workers=101) as executor:
        futs = []
        for x in range(0, 101):
            if(x == 100):
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
                  "counter": x*100 + 1,
                  "stop": (x+1)*100 + 1,
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
          "chr22n-1-101.tar.gz",
          "chr22n-101-201.tar.gz",
          "chr22n-201-301.tar.gz",
          "chr22n-301-401.tar.gz",
          "chr22n-401-501.tar.gz",
          "chr22n-501-601.tar.gz",
          "chr22n-601-701.tar.gz",
          "chr22n-701-801.tar.gz",
          "chr22n-801-901.tar.gz",
          "chr22n-901-1001.tar.gz",
          "chr22n-1001-1101.tar.gz",
          "chr22n-1101-1201.tar.gz",
          "chr22n-1201-1301.tar.gz",
          "chr22n-1301-1401.tar.gz",
          "chr22n-1401-1501.tar.gz",
          "chr22n-1501-1601.tar.gz",
          "chr22n-1601-1701.tar.gz",
          "chr22n-1701-1801.tar.gz",
          "chr22n-1801-1901.tar.gz",
          "chr22n-1901-2001.tar.gz",
          "chr22n-2001-2101.tar.gz",
          "chr22n-2101-2201.tar.gz",
          "chr22n-2201-2301.tar.gz",
          "chr22n-2301-2401.tar.gz",
          "chr22n-2401-2501.tar.gz",
          "chr22n-2501-2601.tar.gz",
          "chr22n-2601-2701.tar.gz",
          "chr22n-2701-2801.tar.gz",
          "chr22n-2801-2901.tar.gz",
          "chr22n-2901-3001.tar.gz",
          "chr22n-3001-3101.tar.gz",
          "chr22n-3101-3201.tar.gz",
          "chr22n-3201-3301.tar.gz",
          "chr22n-3301-3401.tar.gz",
          "chr22n-3401-3501.tar.gz",
          "chr22n-3501-3601.tar.gz",
          "chr22n-3601-3701.tar.gz",
          "chr22n-3701-3801.tar.gz",
          "chr22n-3801-3901.tar.gz",
          "chr22n-3901-4001.tar.gz",
          "chr22n-4001-4101.tar.gz",
          "chr22n-4101-4201.tar.gz",
          "chr22n-4201-4301.tar.gz",
          "chr22n-4301-4401.tar.gz",
          "chr22n-4401-4501.tar.gz",
          "chr22n-4501-4601.tar.gz",
          "chr22n-4601-4701.tar.gz",
          "chr22n-4701-4801.tar.gz",
          "chr22n-4801-4901.tar.gz",
          "chr22n-4901-5001.tar.gz",
          "chr22n-5001-5101.tar.gz",
          "chr22n-5101-5201.tar.gz",
          "chr22n-5201-5301.tar.gz",
          "chr22n-5301-5401.tar.gz",
          "chr22n-5401-5501.tar.gz",
          "chr22n-5501-5601.tar.gz",
          "chr22n-5601-5701.tar.gz",
          "chr22n-5701-5801.tar.gz",
          "chr22n-5801-5901.tar.gz",
          "chr22n-5901-6001.tar.gz",
          "chr22n-6001-6101.tar.gz",
          "chr22n-6101-6201.tar.gz",
          "chr22n-6201-6301.tar.gz",
          "chr22n-6301-6401.tar.gz",
          "chr22n-6401-6501.tar.gz",
          "chr22n-6501-6601.tar.gz",
          "chr22n-6601-6701.tar.gz",
          "chr22n-6701-6801.tar.gz",
          "chr22n-6801-6901.tar.gz",
          "chr22n-6901-7001.tar.gz",
          "chr22n-7001-7101.tar.gz",
          "chr22n-7101-7201.tar.gz",
          "chr22n-7201-7301.tar.gz",
          "chr22n-7301-7401.tar.gz",
          "chr22n-7401-7501.tar.gz",
          "chr22n-7501-7601.tar.gz",
          "chr22n-7601-7701.tar.gz",
          "chr22n-7701-7801.tar.gz",
          "chr22n-7801-7901.tar.gz",
          "chr22n-7901-8001.tar.gz",
          "chr22n-8001-8101.tar.gz",
          "chr22n-8101-8201.tar.gz",
          "chr22n-8201-8301.tar.gz",
          "chr22n-8301-8401.tar.gz",
          "chr22n-8401-8501.tar.gz",
          "chr22n-8501-8601.tar.gz",
          "chr22n-8601-8701.tar.gz",
          "chr22n-8701-8801.tar.gz",
          "chr22n-8801-8901.tar.gz",
          "chr22n-8901-9001.tar.gz",
          "chr22n-9001-9101.tar.gz",
          "chr22n-9101-9201.tar.gz",
          "chr22n-9201-9301.tar.gz",
          "chr22n-9301-9401.tar.gz",
          "chr22n-9401-9501.tar.gz",
          "chr22n-9501-9601.tar.gz",
          "chr22n-9601-9701.tar.gz",
          "chr22n-9701-9801.tar.gz",
          "chr22n-9801-9901.tar.gz",
          "chr22n-9901-10001.tar.gz"
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

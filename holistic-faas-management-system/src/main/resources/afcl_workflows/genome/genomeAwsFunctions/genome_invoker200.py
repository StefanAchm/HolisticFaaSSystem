import json
import time
import boto3
from concurrent.futures import ThreadPoolExecutor

def lambda_handler(event, context):
    start_total = time.time()
    lambda_client = boto3.client('lambda')
    
    # Invoke Individuals and sifting
    start_ind_sifting = time.time()
    with ThreadPoolExecutor(max_workers=201) as executor:
        futs = []
        for x in range(0, 201):
            if(x == 200):
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
                  "counter": x*50 + 1,
                  "stop": (x+1)*50 + 1,
                  "chromNr": "22"
                }
                
                futs.append(
                    executor.submit(lambda_client.invoke,
                        FunctionName   = "genome_individual",
                        InvocationType = "RequestResponse",
                        Payload        = bytes(json.dumps(payload_individual), "utf-8")
                    )
                )
    # Since after 30 sec we get a timeout, we cannot wait for a response here. So, for freq
    # we do not wait for repsonse on thread, bur check for elements in s3 bucket.
    elements = 0
    while(elements < 202):
        # TODO maybe remove this (but freq takes quite long)
        #time.sleep(2)
        s3 = boto3.client('s3')
        response = s3.list_objects(
                Bucket='genome-project',
                Prefix='tmp/',
        )
        elements = len(response['Contents'])
    end_ind_sifting = time.time()
    # Invoke individuals merge
    start_ind_merge = time.time()
    payload_individuals_merge = {
      "keys": [
          "chr22n-1-51.tar.gz",
          "chr22n-51-101.tar.gz",
          "chr22n-101-151.tar.gz",
          "chr22n-151-201.tar.gz",
          "chr22n-201-251.tar.gz",
          "chr22n-251-301.tar.gz",
          "chr22n-301-351.tar.gz",
          "chr22n-351-401.tar.gz",
          "chr22n-401-451.tar.gz",
          "chr22n-451-501.tar.gz",
          "chr22n-501-551.tar.gz",
          "chr22n-551-601.tar.gz",
          "chr22n-601-651.tar.gz",
          "chr22n-651-701.tar.gz",
          "chr22n-701-751.tar.gz",
          "chr22n-751-801.tar.gz",
          "chr22n-801-851.tar.gz",
          "chr22n-851-901.tar.gz",
          "chr22n-901-951.tar.gz",
          "chr22n-951-1001.tar.gz",
          "chr22n-1001-1051.tar.gz",
          "chr22n-1051-1101.tar.gz",
          "chr22n-1101-1151.tar.gz",
          "chr22n-1151-1201.tar.gz",
          "chr22n-1201-1251.tar.gz",
          "chr22n-1251-1301.tar.gz",
          "chr22n-1301-1351.tar.gz",
          "chr22n-1351-1401.tar.gz",
          "chr22n-1401-1451.tar.gz",
          "chr22n-1451-1501.tar.gz",
          "chr22n-1501-1551.tar.gz",
          "chr22n-1551-1601.tar.gz",
          "chr22n-1601-1651.tar.gz",
          "chr22n-1651-1701.tar.gz",
          "chr22n-1701-1751.tar.gz",
          "chr22n-1751-1801.tar.gz",
          "chr22n-1801-1851.tar.gz",
          "chr22n-1851-1901.tar.gz",
          "chr22n-1901-1951.tar.gz",
          "chr22n-1951-2001.tar.gz",
          "chr22n-2001-2051.tar.gz",
          "chr22n-2051-2101.tar.gz",
          "chr22n-2101-2151.tar.gz",
          "chr22n-2151-2201.tar.gz",
          "chr22n-2201-2251.tar.gz",
          "chr22n-2251-2301.tar.gz",
          "chr22n-2301-2351.tar.gz",
          "chr22n-2351-2401.tar.gz",
          "chr22n-2401-2451.tar.gz",
          "chr22n-2451-2501.tar.gz",
          "chr22n-2501-2551.tar.gz",
          "chr22n-2551-2601.tar.gz",
          "chr22n-2601-2651.tar.gz",
          "chr22n-2651-2701.tar.gz",
          "chr22n-2701-2751.tar.gz",
          "chr22n-2751-2801.tar.gz",
          "chr22n-2801-2851.tar.gz",
          "chr22n-2851-2901.tar.gz",
          "chr22n-2901-2951.tar.gz",
          "chr22n-2951-3001.tar.gz",
          "chr22n-3001-3051.tar.gz",
          "chr22n-3051-3101.tar.gz",
          "chr22n-3101-3151.tar.gz",
          "chr22n-3151-3201.tar.gz",
          "chr22n-3201-3251.tar.gz",
          "chr22n-3251-3301.tar.gz",
          "chr22n-3301-3351.tar.gz",
          "chr22n-3351-3401.tar.gz",
          "chr22n-3401-3451.tar.gz",
          "chr22n-3451-3501.tar.gz",
          "chr22n-3501-3551.tar.gz",
          "chr22n-3551-3601.tar.gz",
          "chr22n-3601-3651.tar.gz",
          "chr22n-3651-3701.tar.gz",
          "chr22n-3701-3751.tar.gz",
          "chr22n-3751-3801.tar.gz",
          "chr22n-3801-3851.tar.gz",
          "chr22n-3851-3901.tar.gz",
          "chr22n-3901-3951.tar.gz",
          "chr22n-3951-4001.tar.gz",
          "chr22n-4001-4051.tar.gz",
          "chr22n-4051-4101.tar.gz",
          "chr22n-4101-4151.tar.gz",
          "chr22n-4151-4201.tar.gz",
          "chr22n-4201-4251.tar.gz",
          "chr22n-4251-4301.tar.gz",
          "chr22n-4301-4351.tar.gz",
          "chr22n-4351-4401.tar.gz",
          "chr22n-4401-4451.tar.gz",
          "chr22n-4451-4501.tar.gz",
          "chr22n-4501-4551.tar.gz",
          "chr22n-4551-4601.tar.gz",
          "chr22n-4601-4651.tar.gz",
          "chr22n-4651-4701.tar.gz",
          "chr22n-4701-4751.tar.gz",
          "chr22n-4751-4801.tar.gz",
          "chr22n-4801-4851.tar.gz",
          "chr22n-4851-4901.tar.gz",
          "chr22n-4901-4951.tar.gz",
          "chr22n-4951-5001.tar.gz",
          "chr22n-5001-5051.tar.gz",
          "chr22n-5051-5101.tar.gz",
          "chr22n-5101-5151.tar.gz",
          "chr22n-5151-5201.tar.gz",
          "chr22n-5201-5251.tar.gz",
          "chr22n-5251-5301.tar.gz",
          "chr22n-5301-5351.tar.gz",
          "chr22n-5351-5401.tar.gz",
          "chr22n-5401-5451.tar.gz",
          "chr22n-5451-5501.tar.gz",
          "chr22n-5501-5551.tar.gz",
          "chr22n-5551-5601.tar.gz",
          "chr22n-5601-5651.tar.gz",
          "chr22n-5651-5701.tar.gz",
          "chr22n-5701-5751.tar.gz",
          "chr22n-5751-5801.tar.gz",
          "chr22n-5801-5851.tar.gz",
          "chr22n-5851-5901.tar.gz",
          "chr22n-5901-5951.tar.gz",
          "chr22n-5951-6001.tar.gz",
          "chr22n-6001-6051.tar.gz",
          "chr22n-6051-6101.tar.gz",
          "chr22n-6101-6151.tar.gz",
          "chr22n-6151-6201.tar.gz",
          "chr22n-6201-6251.tar.gz",
          "chr22n-6251-6301.tar.gz",
          "chr22n-6301-6351.tar.gz",
          "chr22n-6351-6401.tar.gz",
          "chr22n-6401-6451.tar.gz",
          "chr22n-6451-6501.tar.gz",
          "chr22n-6501-6551.tar.gz",
          "chr22n-6551-6601.tar.gz",
          "chr22n-6601-6651.tar.gz",
          "chr22n-6651-6701.tar.gz",
          "chr22n-6701-6751.tar.gz",
          "chr22n-6751-6801.tar.gz",
          "chr22n-6801-6851.tar.gz",
          "chr22n-6851-6901.tar.gz",
          "chr22n-6901-6951.tar.gz",
          "chr22n-6951-7001.tar.gz",
          "chr22n-7001-7051.tar.gz",
          "chr22n-7051-7101.tar.gz",
          "chr22n-7101-7151.tar.gz",
          "chr22n-7151-7201.tar.gz",
          "chr22n-7201-7251.tar.gz",
          "chr22n-7251-7301.tar.gz",
          "chr22n-7301-7351.tar.gz",
          "chr22n-7351-7401.tar.gz",
          "chr22n-7401-7451.tar.gz",
          "chr22n-7451-7501.tar.gz",
          "chr22n-7501-7551.tar.gz",
          "chr22n-7551-7601.tar.gz",
          "chr22n-7601-7651.tar.gz",
          "chr22n-7651-7701.tar.gz",
          "chr22n-7701-7751.tar.gz",
          "chr22n-7751-7801.tar.gz",
          "chr22n-7801-7851.tar.gz",
          "chr22n-7851-7901.tar.gz",
          "chr22n-7901-7951.tar.gz",
          "chr22n-7951-8001.tar.gz",
          "chr22n-8001-8051.tar.gz",
          "chr22n-8051-8101.tar.gz",
          "chr22n-8101-8151.tar.gz",
          "chr22n-8151-8201.tar.gz",
          "chr22n-8201-8251.tar.gz",
          "chr22n-8251-8301.tar.gz",
          "chr22n-8301-8351.tar.gz",
          "chr22n-8351-8401.tar.gz",
          "chr22n-8401-8451.tar.gz",
          "chr22n-8451-8501.tar.gz",
          "chr22n-8501-8551.tar.gz",
          "chr22n-8551-8601.tar.gz",
          "chr22n-8601-8651.tar.gz",
          "chr22n-8651-8701.tar.gz",
          "chr22n-8701-8751.tar.gz",
          "chr22n-8751-8801.tar.gz",
          "chr22n-8801-8851.tar.gz",
          "chr22n-8851-8901.tar.gz",
          "chr22n-8901-8951.tar.gz",
          "chr22n-8951-9001.tar.gz",
          "chr22n-9001-9051.tar.gz",
          "chr22n-9051-9101.tar.gz",
          "chr22n-9101-9151.tar.gz",
          "chr22n-9151-9201.tar.gz",
          "chr22n-9201-9251.tar.gz",
          "chr22n-9251-9301.tar.gz",
          "chr22n-9301-9351.tar.gz",
          "chr22n-9351-9401.tar.gz",
          "chr22n-9401-9451.tar.gz",
          "chr22n-9451-9501.tar.gz",
          "chr22n-9501-9551.tar.gz",
          "chr22n-9551-9601.tar.gz",
          "chr22n-9601-9651.tar.gz",
          "chr22n-9651-9701.tar.gz",
          "chr22n-9701-9751.tar.gz",
          "chr22n-9751-9801.tar.gz",
          "chr22n-9801-9851.tar.gz",
          "chr22n-9851-9901.tar.gz",
          "chr22n-9901-9951.tar.gz",
          "chr22n-9951-10001.tar.gz"
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

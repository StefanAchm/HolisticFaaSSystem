import json

def lambda_handler(event, context):
    pop_arr = ["EUR", "AFR", "EAS", "ALL", "GBR", "SAS", "AMR"]
    continue_ = True
    if 'POP' in event.keys():
        if event['POP'] == 'AMR':
            continue_ = False
            pop = "AMR"
        else:
            for i in range(len(pop_arr)):
                if pop_arr[i] == event['POP']:
                    pop = pop_arr[i+1]
    else:
        pop = "EUR"
    return {
      "chromNr": "22",
      "POP": pop,
      "bucketName": "genome-project",
      "continue": continue_
    }
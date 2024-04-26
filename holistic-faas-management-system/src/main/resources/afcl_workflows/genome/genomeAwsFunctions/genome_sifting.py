import json
import boto3
import io
import gzip

def lambda_handler(event, context):
    BUCKET_NAME = 'genome-project' # replace with your bucket name
    key_datafile = event['key'] #some tar
    c=event['chromNr']
    
    s3 = boto3.resource('s3')
    
    content = get_data_file(s3,BUCKET_NAME, "input/"+key_datafile)
    print(content[0])

    nocomment = []
    lineNumber=0
    for x in content:
        if len(x)>0 and not x[0]=="#":
            lineNumber=lineNumber+1
            if "rs" in x and ("deleterious" in x or "tolerated" in x):
                nocomment.append([lineNumber] + x.rstrip().split("\t"))
                
    		
    
    for i in range(len(nocomment)):#len(nocomment)
    	info = nocomment[i][8].split("|")
    	try:	
    		sift = info[16].split("(")[1].split(")")[0]
    	except:
    		sift = ""
    	try:
    		phenotype = info[17].split("(")[1].split(")")[0]
    	except:
    		phenotype = ""
    	nocomment[i]=[nocomment[i][0],nocomment[i][3],info[4],sift,phenotype]

    print(nocomment[0])
    
    for i in range(len(nocomment)):
    	nocomment[i] = "\t".join(str(e) for e in nocomment[i])
    nocomment="\n".join(nocomment)
    
    key = "tmp/sifted.SIFT.chr"+str(c)+".txt"
    key = save_concat_tar(s3, key, BUCKET_NAME, nocomment, c)
    
    
    return {
        'statusCode': 200,
        'body': key
    }

def get_data_file(s3,BUCKET_NAME,key_datafile):
    object = s3.Object(BUCKET_NAME, key_datafile)
    bytes = io.BytesIO(object.get()['Body'].read())
    with gzip.open(bytes, 'rb') as f:
        file_content = f.read().decode("utf-8") 

    return file_content.split('\n')
    
def save_concat_tar(s3, key, BUCKET_NAME, content, c):
    file_out = io.BytesIO(content.encode())
    
    object = s3.Object(BUCKET_NAME, key)
    object.put(Body=file_out.getvalue())
    return  key
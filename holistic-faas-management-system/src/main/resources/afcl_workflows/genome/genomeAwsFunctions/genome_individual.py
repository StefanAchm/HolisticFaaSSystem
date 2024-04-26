import json
import boto3
import botocore
import tarfile
import io
import zlib
import gzip

def lambda_handler(event, context):
    BUCKET_NAME = 'genome-project' # replace with your bucket name
    key_datafile = event['key_input'] #ALL.chr21.80000.vcf.gz
    counter = int(event['counter'])
    stop = int(event['stop'])
    c=int(event['chromNr'])
    
    
    s3 = boto3.resource('s3')
   
    #save_string_in_byte = event.body.encode('ascii')
    try:
        columns = readColumns(s3, "input/columns.txt", BUCKET_NAME)
        print(len(columns),columns[:5])
        
        lines = readGzip(s3, "input/"+key_datafile, BUCKET_NAME)
        
        lines = lines[counter-1:stop-1]
        nocomment = []
        for x in lines:
        	if not x[0]=="#":
        		nocomment.append(x.rstrip().split("\t"))
        #print(nocomment[:5])
        
        gnomes = doIndividual(nocomment)

        tar = createTarWithGnomes(gnomes,columns,c)
        
        key_result = "tmp/chr"+str(c)+"n-"+str(counter)+"-"+str(stop)+".tar.gz"#chr21n-1-1001.tar.gz
        save_tar_to_s3(s3, key_result, BUCKET_NAME,tar)
        
        return_value = {
            'statusCode': 200,
            'body': key_result
        }
    except botocore.exceptions.ClientError as e:
        msg = "Some Error is thrown!"
        if e.response['Error']['Code'] == "404":
            msg = "The object does not exist."
        
        return_value = {
            'statusCode': e.response['Error']['Code'],
            'body': json.dumps(msg)
        }
        
    return return_value

def doIndividual(nocomment):
    individuals=0
    gnomes=[]
    for j in range(9,2513):#2513
        gnome=[]
        for i in range(len(nocomment)):#len(nocomment)
            e = nocomment[i]
            cs = e[j].split("|")
            val = e[7].split(";")[8].split("=")[1]
            	
            try:	
                ##could be two values then always bigger like in gnome project
                val = float(val)	
    			
                if(val>=0.5 and int(cs[0])==0) or (val<0.5 and int(cs[0])==1):
                    tmp=[e[1],e[2],e[3],e[4],val]
                    gnome.append(tmp)
                    individuals= individuals + 1
            except ValueError:
                if(int(cs[0])==1):
                    individuals= individuals + 1
                    tmp=[e[1],e[2],e[3],e[4],val]
                    gnome.append(tmp)
        gnomes.append(gnome)
    print("Found " + str(individuals) + " individuals!")
    return gnomes

def createTarWithGnomes(gnomes,columns,c):
    file_out = io.BytesIO()
    tar = tarfile.open(mode = "w:gz", fileobj = file_out)
    for i in range(len(gnomes)):
        for j in range(len(gnomes[i])):
            gnomes[i][j] = "\t".join(str(e) for e in gnomes[i][j])
        gnomes[i]="\n".join(gnomes[i])
            
        string = io.BytesIO(gnomes[i].encode())
        info = tarfile.TarInfo(name="chr"+str(c)+"."+columns[i+9])
        info.size=len(string.getbuffer())
        tar.addfile(tarinfo=info, fileobj=string)
    tar.close()
    return file_out.getvalue()

def readGzip(s3,key_datafile, BUCKET_NAME):
    #Read data
    object = s3.Object(BUCKET_NAME, key_datafile)
    bytes = io.BytesIO(object.get()['Body'].read())
    #file_content = bytes.decode("utf-8")
    with gzip.open(bytes, 'rb') as f:
        file_content = f.read().decode("utf-8") 

    return file_content.split('\n')
    
    
def readColumns(s3,key_columnsfile, BUCKET_NAME):
    ##Read columns
    object = s3.Object(BUCKET_NAME, key_columnsfile)
    bytes = object.get()['Body'].read()
    file_content = bytes.decode("utf-8") 
    return file_content.rstrip().split("\t")
    
    
def save_tar_to_s3(s3,key_result, BUCKET_NAME,tar):
    object = s3.Object(BUCKET_NAME, key_result)
    object.put(Body=tar)
    
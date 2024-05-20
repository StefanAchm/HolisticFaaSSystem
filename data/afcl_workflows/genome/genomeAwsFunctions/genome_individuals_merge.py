import json
import boto3
import tarfile, io

def lambda_handler(event, context):
    BUCKET_NAME = 'genome-project' # replace with your bucket name
    
    # for genome200 (consider return)
    key_datafiles = ["chr22n-1-51.tar.gz",
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
      "chr22n-9951-10001.tar.gz"]
    c = 22
    
    # for others (consider return)
    '''key_datafiles = event['keys'] #some tars
    print(key_datafiles)
    c=event['chromNr']'''
    
    s3 = boto3.resource('s3')
    columns = readColumns(s3, "columns.txt", BUCKET_NAME)
    
    
    tars=[]
    for key in key_datafiles:
        tars.append(readTar(s3, key, BUCKET_NAME))
    
    gnomes=[]
    for i in range(2504):
        all_from_one_gnome = []
        for j in range(len(tars)):
            all_from_one_gnome.extend(tars[j][i])
        gnomes.append(all_from_one_gnome)
    
    print(len(gnomes), len(gnomes[0]))
    
    result_key = "tmp/chr"+str(c)+"n.tar.gz"
    save_concat_tar(s3, result_key, BUCKET_NAME, gnomes, columns , c)
    return {
        'statusCode': 200,
        'body': result_key
    }

def readColumns(s3,key_columnsfile, BUCKET_NAME):
    ##Read columns
    object = s3.Object(BUCKET_NAME, "input/"+key_columnsfile)
    bytes = object.get()['Body'].read()
    file_content = bytes.decode("utf-8") 
    return file_content.rstrip().split("\t")
    
def readTar(s3,key_gnomefile, BUCKET_NAME):
    print("---------", key_gnomefile)
    object = s3.Object(BUCKET_NAME, "tmp/"+key_gnomefile)
    bytes = object.get()['Body'].read()
    
    file_like_object = io.BytesIO(bytes)
    tar = tarfile.open(mode = "r:gz",fileobj=file_like_object)
    files = []
    for member in tar.getmembers():
        f = tar.extractfile(member)
        if f == None:
            #files.append([])
            continue
        #f=f.read()
        f= f.read().decode("utf-8").split("\n")
        files.append(f)
    if(not len(files)==2504):
        print("Tar file has no enought files!")
    return files
    
def save_concat_tar(s3, key, BUCKET_NAME, gnomes, columns, c):
    file_out = io.BytesIO()
    tar = tarfile.open(mode = "w:gz", fileobj = file_out)
    for i in range(len(gnomes)):
        gnomes[i]="\n".join(gnomes[i])
        string = io.BytesIO(gnomes[i].encode())
        info = tarfile.TarInfo(name="chr"+str(c)+"."+columns[i+9])
        info.size=len(string.getbuffer())
        tar.addfile(tarinfo=info, fileobj=string)
    tar.close()
    
    
    object = s3.Object(BUCKET_NAME, key)
    object.put(Body=file_out.getvalue())
    return  key
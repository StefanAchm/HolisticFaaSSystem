import json
import os
import subprocess
from multiprocessing import Process, Pipe
from Inspector import *
from storage.pyStorage import pyStorage
import re
import time


def create_chunk(file, written_file, start_line, end_line, input_file_lines, i, conn, result_dict):
    # Import the module and collect data
    
    print('Creating chunk {}: start: {} end: {}'.format(i, start_line, end_line))

    # Replace all lines with null character except first line and chunk's range
    os.system('sed \'1! {' + str(start_line) + ',' + str(
    end_line) + '! s/./N/g }\' /tmp/' + file + ' > /tmp/chunks/chunk' + str(i) + '.fasta')

    # Check if chunk has same size as original file (not needed? sed is perfect)
    result = subprocess.check_output('wc -l /tmp/chunks/chunk' + str(i) + '.fasta',
		                         shell=True)
    chunk_lines = int(result.split(b' ')[0])
    if chunk_lines != input_file_lines:
        raise Exception('Splitting error!')

    # Store chunk in S3
    start = int(round(time.time() * 1000))
    pyStorage.copy("/tmp/chunks/chunk" + str(i) + ".fasta", written_file)
    end = int(round(time.time() * 1000))
    result_dict["up_chunk" + str(i) + ".fasta"] = (end - start)
    result_dict["up_ALL"] = (end - start)
    
    print("Successfully written chunk file: {}.".format(written_file))
    conn.send(result_dict)
    conn.close()

    
def main(request):
    event = request.get_json()
    
    start_all = int(round(time.time() * 1000))
    
    pyStorage.create_cred_file(aws_access_key_id=event['aws_access_key_id'], aws_secret_key=event['aws_secret_key'], aws_session_token=event['aws_session_token'],
                               gcp_client_email=event['gcp_client_email'], gcp_private_key=event['gcp_private_key'], gcp_project_id=event['gcp_project_id'])


    print("Running split with fasta: {}. Folders (optional): {}".format(
        event["fasta"],
        event["folders"] if "folders" in event.keys() else "Not specified"))

    number_chunks = int(float(event["chunks"]))
    folderindex = int(float(event["folderindex"]))
    
    bucket = event["output_buckets"][0]

    fastaFolder = bucket + event["fastaFolder"]
    folders =  fastaFolder + "/" + event["folders"]

    print("Using folders: {}".format(folders))

    file = event["fasta"]
    file_to_store = pyStorage.retrieve_file_name(file)
    
    result_dict = {}

    os.system('mkdir /tmp/chunks')

    # Load input
    start = int(round(time.time() * 1000))
    pyStorage.copy(file, "/tmp/" + file_to_store)
    end = int(round(time.time() * 1000))
    result_dict["dl_" + file_to_store] = (end - start)
    result_dict["dl_ALL"] = (end - start)
    
    # Get line count of input file
    result = subprocess.check_output('wc -l /tmp/' + file_to_store, shell=True)
    input_file_lines = int(result.split(b' ')[0])

    # Get char count of input file
    result = subprocess.check_output('wc --chars /tmp/' + file_to_store, shell=True)
    input_file_characters = int(result.split(b' ')[0])

    print('Input has {} lines and {} characters'.format(input_file_lines, input_file_characters))

    # Calculate lines of chunks with start/end line per chunk
    # First line is the same for every chunk only other lines are replaced
    lines_per_chunk = (input_file_lines - 1) // number_chunks
    line_modulo = (input_file_lines - 1) % number_chunks

    if lines_per_chunk < 1:
        raise Exception("To many chunks!")

    print('Each chunk has {} lines and first {} chunks have one more'.format(lines_per_chunk, line_modulo))

    start_line = 2 + (folderindex - 1) * lines_per_chunk
    written_files = list()

    jobs = []
#    for i in range(number_chunks):
    i = 0
    end_line = start_line + lines_per_chunk - 1
    if i < line_modulo:
        end_line += 1
        
    written_file = folders + '/chunk.fasta'
        
    written_files.append(written_file)
    parent_conn, child_conn = Pipe()
    p = Process(target=create_chunk, args=(file_to_store, written_file, start_line, end_line, input_file_lines, i, child_conn, result_dict), name='create_chunk')
    
    jobs.append(p)
    
    p.start()
    result_dict = parent_conn.recv()
    start_line = end_line + 1

    for job in jobs:
        job.join()
            
    result = subprocess.check_output('ls -a /tmp/chunks/', shell=True).decode('ASCII')

    print("Written files: {}".format(written_files))
    
    result_dict["fasta"] = written_file
    result_dict["fastaFile"] = fastaFolder
    result_dict["aws_access_key_id"] = event["aws_access_key_id"]
    result_dict["aws_secret_key"] = event["aws_secret_key"]
    result_dict["aws_session_token"] = event["aws_session_token"]
    result_dict["gcp_client_email"] = event["gcp_client_email"]
    result_dict["gcp_private_key"] = event["gcp_private_key"]
    result_dict["gcp_project_id"] = event["gcp_project_id"]
    result_dict["output_buckets"] = event["output_buckets"]

    end_all = int(round(time.time() * 1000))
    result_dict["work_ALL"] = (end_all - start_all) - result_dict["dl_ALL"] - result_dict["up_ALL"]

    return result_dict

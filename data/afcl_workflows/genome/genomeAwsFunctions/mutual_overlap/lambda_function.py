import json
import time
from random import sample
import os
import io
import tarfile
import os.path
import itertools
import argparse
import collections
from collections import Counter
import boto3

import numpy as np
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.colors as colors

def lambda_handler(event, context):
    c=event['chromNr']
    POP=event['POP']

    n_runs=1

    BUCKET_NAME = event['bucketName']
    s3 = boto3.resource('s3')


    font = {'family':'serif', 'size':14   }
    plt.rc('font', **font)

    rd = ReadData()
    res = Results()
    wr = WriteData()
    pd = PlotData()
    
    half_indpairsfile = './output_no_sift/individual_half_pairs_overlap_chr' + str(c) + '_sNO-SIFT_' + POP + '.txt'
    total_indpairsfile = './output_no_sift/total_individual_pairs_overlap_chr' + str(c) + '_sNO-SIFT_' + POP + '.txt'
    genepairsfile = './output_no_sift/gene_pairs_count_chr' + str(c) + '_sNO-SIFT_' + POP + '.txt'
    random_indpairsfile = './output_no_sift/100_individual_overlap_chr' + str(c) + '_sNO-SIFT_' + POP + '.txt'

    colormap = './plots_no_sift/colormap_distribution_c' + str(c) + '_sNO-SIFT_' + POP + '.png'
    half_overlap = './plots_no_sift/half_distribution_c' + str(c) + '_sNO-SIFT_' + POP + '.png'
    total_overlap = './plots_no_sift/total_distribution_c' + str(c) + '_sNO-SIFT_' + POP + '.png'
    random_overlap = './plots_no_sift/100_distribution_c' + str(c) + '_sNO-SIFT_' + POP + '.png'
    
    total_mutations_filename = './output_no_sift/total_mutations_individual' + str(c) + '_sNO-SIFT' + '_' + POP + '.txt'
    random_mutations_filename = './output_no_sift/random_mutations_individual' + str(c) + '_sNO-SIFT' + '_' + POP 
    
    mutation_index_array_file = './output_no_sift/mutation_index_array' + str(c) + '_sNO-SIFT_' + POP + '.txt'
    
    map_variations_file = './output_no_sift/map_variations' + str(c) + '_sNO-SIFT_' + POP + '.txt'
    


    ids = rd.read_names(POP, s3, BUCKET_NAME)
    n_pairs = len(ids)/2

    rs_numbers, map_variations = rd.read_rs_numbers('tmp/sifted.SIFT.chr' + str(c) + '.txt', s3, BUCKET_NAME)
    mutation_index_array, total_mutations, total_mutations_list = rd.read_individuals(ids, c, rs_numbers, s3, BUCKET_NAME)

    file_out = io.BytesIO()
    tar = tarfile.open(mode = "w:gz", fileobj = file_out)
    
    wr.write_total_indiv(total_mutations_filename, total_mutations,tar)
    wr.write_total_indiv(map_variations_file, map_variations, tar)
   
    #cross-correlations mutations overlapping
    half_pairs_overlap = res.half_pair_individuals(mutation_index_array)
    total_pairs_overlap, simetric_overlap = res.total_pair_individuals(mutation_index_array)
    random_pairs_overlap = res.pair_individuals(mutation_index_array, n_runs)
    
    wr.write_mutation_index_array(mutation_index_array_file, mutation_index_array,tar)
    wr.write_pair_individuals(half_indpairsfile, half_pairs_overlap, tar)
    wr.write_pair_individuals(total_indpairsfile, total_pairs_overlap, tar)
    wr.write_pair_individuals(random_indpairsfile, random_pairs_overlap, tar)
    
    pd.individual_overlap(POP, c, n_runs, half_pairs_overlap, half_overlap, tar)
    pd.individual_overlap(POP, c, n_runs, simetric_overlap, total_overlap, tar)
    pd.individual_overlap(POP, c, n_runs, random_pairs_overlap, random_overlap, tar)
    pd.total_colormap_overlap(POP, total_pairs_overlap, colormap, tar)

    #list of frecuency of mutations in 26 individuals
    random_mutations_list=res.group_indivuals(total_mutations_list, n_runs)
    wr.write_random_mutations_list(n_runs, random_mutations_filename, random_mutations_list,tar)

    # gen overlapping
    gene_pair_list = res.gene_pairs(mutation_index_array)
    wr.write_total_indiv(genepairsfile, gene_pair_list,tar)

    tar.close()
    result_key='output/chr%s-%s.tar.gz' % (c, POP)
    object = s3.Object(BUCKET_NAME, result_key)
    object.put(Body=file_out.getvalue())
 
    
    return {
        'statusCode': 200,
        'body': result_key
    }
    
    
class ReadData :
#reads POP + columns makes set
    def read_names(self, POP,s3,BUCKET_NAME) :
        object = s3.Object(BUCKET_NAME, "input/" + POP)
        bytes = object.get()['Body'].read()
        file_content = bytes.decode("utf-8")
        text = file_content.split()
        all_ids = text[0:]
        
        object = s3.Object(BUCKET_NAME, "input/columns.txt")
        bytes = object.get()['Body'].read()
        file_content = bytes.decode("utf-8") 
        genome_ids = file_content.split()
        
        ids = list(set(all_ids) & set(genome_ids))
        return ids

#reads siftfile
    def read_rs_numbers(self, siftfile,s3,BUCKET_NAME) :
        rs_numbers = []
        variations = {}
        map_variations = {}
        all_variations = []
        object = s3.Object(BUCKET_NAME, siftfile)
        bytes = object.get()['Body'].read()
        sift_file = bytes.decode("utf-8").split("\n")
        for item in sift_file:
            item = item.split()
            if len(item) > 2:
	            rs_numbers.append(item[1])
        	    map_variations[item[1]] = item[2]
        print(rs_numbers)
        return rs_numbers, map_variations
    
#reads different cromes
    def read_individuals(self, ids, c, rs_numbers, s3, BUCKET_NAME) :
        mutation_index_array = []
        total_mutations={}  
        total_mutations_list =[]
        
        object = s3.Object(BUCKET_NAME, 'tmp/chr' + str(c) + 'n.tar.gz')
        bytes = io.BytesIO(object.get()['Body'].read())
        inputtar = tarfile.open(mode = "r:gz", fileobj = bytes)

        for member in inputtar.getmembers():
            #item={}
            #item['name']=member.name
            f = inputtar.extractfile(member)
            if f==None:
                continue
            for name in ids:
                if name in member.name:
                    break;
            
            text = f.read().decode("utf-8").split()
            
            sifted_mutations = list(set(rs_numbers).intersection(text))
            mutation_index_array.append(sifted_mutations)
            total_mutations[name]= len(sifted_mutations)
            total_mutations_list.append(len(sifted_mutations))
        print ('mutation index array for %s : %s' % ( ids[0], mutation_index_array[0]))
        print ('total_len_mutations for %s : %s' % ( ids[0], total_mutations[ids[0]]))
        #print('total_mutations_list is %s ' % total_mutations_list)
        return mutation_index_array, total_mutations, total_mutations_list

class Results :

    def group_indivuals(self, total_mutations_list, n_runs) :
        n_group = 26
        random_mutations_list= []
        for run in range(n_runs):
            random_mutations_list.append(sample(total_mutations_list, n_group))
        return random_mutations_list

    def pair_individuals(self, mutation_index_array, n_runs) :
        n_p = len(mutation_index_array)
        n_pairs = int(round(n_p/2))
        list_p = np.linspace(0, n_p - 1, n_p).astype(int)
        pairs_overlap = np.zeros((n_runs, n_pairs))
        for run in range(n_runs) :
            randomized_list = sample(list(list_p) , n_p)
            for pq in range(n_pairs) :
                array1 = mutation_index_array[randomized_list[2*pq]]
                array2 = mutation_index_array[randomized_list[2*pq]]
                pair_array = set(array1) & set(array2)
                pairs_overlap[run][pq] = len(pair_array)

        return pairs_overlap

    def total_pair_individuals (self, mutation_index_array) :
        n_p = len(mutation_index_array)
        total_pairs_overlap = np.zeros((n_p, n_p))
        simetric_overlap = np.zeros((n_p, n_p))
        for run in range(n_p):
                        array1 = mutation_index_array[run]
                        start = run +1
                        for pq in range(start, n_p) :
                                array2 = mutation_index_array[pq]
                                pairs_array = set(array1) & set(array2)
                                total_pairs_overlap[run][pq]=len(pairs_array)
                                simetric_overlap[run][pq] = len(pairs_array)
                                simetric_overlap[pq][run]= len(pairs_array)

        return total_pairs_overlap , simetric_overlap

    def half_pair_individuals(self, mutation_index_array) :
        n_p = len(mutation_index_array)
        n_pairs = int(round(n_p/2))
        pairs_overlap = np.zeros((n_pairs, n_pairs))
        for run in range(n_pairs):
            array1 = mutation_index_array[run]
            index =0
            for pq in range(n_pairs+1, n_p):
                array2 = mutation_index_array[pq]
                pairs_array = set(array1) & set(array2)
                pairs_overlap[run][index]=len(pairs_array)

        return pairs_overlap

    def gene_pairs(self, mutation_index_array):
        n_p = len(mutation_index_array)
        gene_pair_list = {}
        for pp in range(n_p) :  
            pairs = itertools.combinations(mutation_index_array[pp], 2)
            for pair in pairs :
                key = str(pair)
                if key not in gene_pair_list : gene_pair_list[key] = 1
                else : gene_pair_list[key] += 1

        return gene_pair_list

class PlotData :        

    def individual_overlap(self, POP, c, n_runs, pairs_overlap, outputFile, tar):
        print('plotting cross matched number of individuals:%s '% len(pairs_overlap))
        pairs_overlap = np.array(pairs_overlap)     

        min_p = np.min(pairs_overlap)
        max_p = np.max(pairs_overlap)
        nbins = int(max_p) + 1
        n_runs = len(pairs_overlap)

        nbins = int(np.max(pairs_overlap))
        bin_centres = np.linspace(0, nbins, nbins)
        bin_edges = np.linspace(-0.5, nbins + 0.5, nbins + 1)

        fig = plt.figure(frameon=False, figsize=(10, 9))
        ax = fig.add_subplot(111)
        hists = []
        max_h = 0
        for run in range(n_runs) :
            h, edges = np.histogram(pairs_overlap[run], bins = bin_edges)
            ax.plot(bin_centres, h, alpha = 0.5)
            if len(h) > 0:
                max_h = max(max_h, max(h))

        plt.xlabel('Number of overlapping gene mutations', fontsize = 24)
        plt.ylabel(r'frequency', fontsize = 28)
        text1 = 'population ' + POP + '\n chromosome ' + str(c) + '\n SIFT < NO-SIFT \n' + str(n_runs) + ' runs'
        plt.text(.95, .95, text1, fontsize = 24, verticalalignment='top', horizontalalignment='right', transform = ax.transAxes)
        string = io.BytesIO()
        plt.savefig(string)
        plt.close()
        info = tarfile.TarInfo(name=outputFile)
        info.size=len(string.getbuffer())
        string.seek(0)
        tar.addfile(tarinfo=info, fileobj=string)

    def total_colormap_overlap(self, POP, total_pairs_overlap, outputFile, tar):
        print('plotting colormap number of individuals: %s' % len(total_pairs_overlap))
        fig = plt.figure()
        cmap = colors.ListedColormap(['blue','black','red', 'green', 'pink'])
        img = plt.imshow(total_pairs_overlap,interpolation='nearest', cmap = cmap, origin='lower')
        plt.colorbar(img,cmap=cmap)
        string = io.BytesIO()
        plt.savefig(string)  
        plt.close()
        info = tarfile.TarInfo(name=outputFile)
        info.size=len(string.getbuffer())
        string.seek(0)
        tar.addfile(tarinfo=info, fileobj=string)


class WriteData :
    def saveText(self, filename, text, tar):
        string = io.BytesIO(text.encode())
        info = tarfile.TarInfo(name=filename)
        info.size=len(string.getbuffer())
        tar.addfile(tarinfo=info, fileobj=string)

    def write_pair_individuals(self, indpairsfile, pairs_overlap, tar) : 
        string = io.BytesIO()
        np.savetxt(string, pairs_overlap, fmt = '%i')
        info = tarfile.TarInfo(name=indpairsfile)
        info.size=len(string.getbuffer())
        string.seek(0)
        tar.addfile(tarinfo=info, fileobj=string)

    def write_total_indiv(self, total_mutations_filename, total_mutations, tar) :
        s=""
        for key, count in total_mutations.items() :
            s=s+key + '\t' + str(count) + '\n'
        self.saveText(total_mutations_filename, s, tar)
    
    def write_random_mutations_list(self, n_runs, random_mutations_filename, random_mutations_list,tar) :
        for run in range(n_runs):
            filename= random_mutations_filename +'_run_' + str(run) + '.txt'
            text= '\n'.join([str(item) for item in random_mutations_list[run]])
            self.saveText(filename, text, tar)
    
    def write_mutation_index_array(self, mutation_index_array_file, mutation_index_array, tar):
        text = '\n'.join(str(x) for x in mutation_index_array)
        self.saveText(mutation_index_array_file, text, tar)


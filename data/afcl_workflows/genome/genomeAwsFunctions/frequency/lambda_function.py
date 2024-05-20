import json
import boto3
import numpy as np
import io
import tarfile
from random import sample
import os.path
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.colors as colors
import matplotlib.cm as cm
import itertools
import argparse
import collections
from collections import Counter


class ReadData :
	def read_names(self, POP,s3,BUCKET_NAME) :
		object = s3.Object(BUCKET_NAME, "input/"+POP)
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


	def read_rs_numbers(self, siftfile,s3,BUCKET_NAME) :
		rs_numbers = []
		variations = {}
		map_variations = {}
		all_variations = []
		object = s3.Object(BUCKET_NAME, "tmp/"+siftfile)
		bytes = object.get()['Body'].read()
		sift_file = bytes.decode("utf-8").split("\n")

		for item in sift_file:
			item = item.split()
			if len(item) > 2:
				rs_numbers.append(item[1])
				map_variations[item[1]] = item[2]
		return rs_numbers, map_variations
	
	def read_individuals(self, ids, rs_numbers, c ,s3,BUCKET_NAME) :
		mutation_index_array = []
		object = s3.Object(BUCKET_NAME, 'tmp/chr' + str(c) + 'n.tar.gz')
		bytes = io.BytesIO(object.get()['Body'].read())
		inputtar = tarfile.open(mode = "r:gz", fileobj = bytes)
		for member in inputtar.getmembers():
			f = inputtar.extractfile(member)
			if f==None:
				continue
			text = f.read().decode("utf-8").split()
			sifted_mutations = list(set(rs_numbers).intersection(text))
			mutation_index_array.append(sifted_mutations)
		return mutation_index_array

class Results :

	def overlap_ind(self, ids, n_runs, n_indiv, mutation_index_array):
		n_p = len(mutation_index_array)
		list_p = np.linspace(0, n_p - 1, n_p).astype(int)
		mutation_overlap = []
		random_indiv = []
		for run in range(n_runs) :
			randomized_list = sample(list(list_p), n_p)
			result = Counter()
			r_ids=[]
			for pq in range(n_indiv):
				if 2*pq >= len(randomized_list):
					break
				if randomized_list[2*pq] >= len(ids):
					break
				b_multiset = collections.Counter(mutation_index_array[randomized_list[2*pq]])
				r_ids.append(ids[randomized_list[2*pq]])
				result = result + b_multiset
			random_indiv.append(r_ids)
			mutation_overlap.append(result)
		return mutation_overlap, random_indiv
	
	def histogram_overlap(self, n_runs, mutation_overlap):
		histogram_overlap= []
		for run in range(n_runs):
			final_counts = [count for item, count in mutation_overlap[run].items()]
			histogram_overlap.append(collections.Counter(final_counts))
		return histogram_overlap			

class PlotData :		

	def plot_histogram_overlap(self, POP, n_runs, histogram_overlap, outputFile, tar):
		for run in range(n_runs):
			output = outputFile + str(run) + '.png'
			final_counts = [count for item, count in histogram_overlap[run].items()]
			N = len( final_counts )
			x = range( N )
			width = 1/1.5
			bar1=plt.bar( x, final_counts, width, color="grey" )
			plt.ylabel( 'Mutations' )
			plt.xlabel('Individuals')
			plt.xticks( np.arange( 1,N+1 ) )
			string = io.BytesIO()
			plt.savefig(string)
			plt.close()
			info = tarfile.TarInfo(name=output)
			info.size=len(string.getbuffer())
			string.seek(0)
			tar.addfile(tarinfo=info, fileobj=string)
	

class WriteData :
	def saveText(self, filename, text, tar):
		string = io.BytesIO(text.encode())
		info = tarfile.TarInfo(name=filename)
		info.size=len(string.getbuffer())
		tar.addfile(tarinfo=info, fileobj=string)

	def write_histogram_overlap(self, n_runs, n_indiv, histogram_overlapfile, histogram_overlap, tar) :	
		for run in range(n_runs):
			overlapfile = histogram_overlapfile + str(run) + '.txt'
			s='Number Individuals - Number Mutations  \n'
			for i in range(1,n_indiv+1):
				if i in histogram_overlap[run]:
					s=s+str(i) + '-' + str(histogram_overlap[run][i]) + '\n'
				else:
					s=s+str(i) + '-' + str(0) + '\n'
			self.saveText(overlapfile, s, tar)
				
	def write_mutation_overlap(self, n_runs, mutation_overlapfile, mutation_overlap, tar) :	
		for run in range(n_runs):
			overlapfile = mutation_overlapfile + str(run) + '.txt'
			s="Mutation Index- Number Overlapings \n"
			for key, count in mutation_overlap[run].items() :
				s=s+key + '-' + str(count) + '\n'
			self.saveText(overlapfile, s, tar)
	
	def write_random_indiv(self, n_runs, randomindiv_file, random_indiv, tar) :
		for run in range(n_runs):
			randomfile = randomindiv_file + str(run) + '.txt'
			text= '\n'.join([str(item) for item in random_indiv[run]])
			self.saveText(randomfile, text, tar)
	
	def write_mutation_index_array(self, mutation_index_array_file, mutation_index_array, tar):
		text = '\n'.join(str(x) for x in mutation_index_array)
		self.saveText(mutation_index_array_file, text, tar)
	
	def write_map_variations(self, map_variations_file, map_variations, tar) :
		s=""
		for key, count in map_variations.items() :
			s=s+key + '\t' + str(count) + '\n'
		self.saveText(map_variations_file, s, tar)


def lambda_handler(event, context):
	c = event['chromNr']
	POP = event['POP']

	n_runs = 1000
	n_indiv= 52

	BUCKET_NAME = event['bucketName']
	s3 = boto3.resource('s3')

	font = {'family':'serif',
	    'size':14   }
	plt.rc('font', **font)

	rd = ReadData()
	res = Results()
	wr = WriteData()
	pd = PlotData()
	
	histogram_overlapfile = './output_no_sift/Histogram_mutation_overlap_chr' + str(c) + '_sNO-SIFT_' + POP + '_'
	mutation_overlapfile = './output_no_sift/Mutation_overlap_chr' + str(c) + '_sNO-SIFT_' + POP + '_'
	mutation_index_array_file = './output_no_sift/mutation_index_array' + str(c) + '_sNO-SIFT_' + POP + '.txt'
	histogram_overlap_plot = './plots_no_sift/Frequency_mutations' + str(c) + '_sNO-SIFT_' + POP 
	map_variations_file = './output_no_sift/map_variations' + str(c) + '_sNO-SIFT_' + POP + '.txt'
	randomindiv_file = './output_no_sift/random_indiv' + str(c) + '_sNO-SIFT_' + POP + '_'


	ids = rd.read_names(POP,s3,BUCKET_NAME)
	n_pairs = len(ids)/2
	

	rs_numbers, map_variations = rd.read_rs_numbers('sifted.SIFT.chr' + str(c) + '.txt',s3,BUCKET_NAME)
	mutation_index_array = rd.read_individuals(ids, rs_numbers, c, s3,BUCKET_NAME)

	# gen final output
	file_out = io.BytesIO()
	tar = tarfile.open(mode = "w:gz", fileobj = file_out)

	wr.write_map_variations(map_variations_file, map_variations, tar)
	wr.write_mutation_index_array(mutation_index_array_file, mutation_index_array, tar)
	
	mutation_overlap, random_indiv= res.overlap_ind(ids, n_runs, n_indiv, mutation_index_array)

	histogram_overlap= res.histogram_overlap(n_runs, mutation_overlap)
	wr.write_mutation_overlap(n_runs, mutation_overlapfile, mutation_overlap, tar)
	wr.write_histogram_overlap(n_runs, n_indiv, histogram_overlapfile, histogram_overlap, tar)
	wr.write_random_indiv(n_runs, randomindiv_file, random_indiv, tar)
	
	pd.plot_histogram_overlap(POP, n_runs, histogram_overlap, histogram_overlap_plot, tar)

	tar.close()
	result_key='output/chr%s-%s-freq.tar.gz' % (c, POP)
	object = s3.Object(BUCKET_NAME, result_key)
	object.put(Body=file_out.getvalue())

	return {
		'statusCode': 200,
		'body': json.dumps(result_key)
		}

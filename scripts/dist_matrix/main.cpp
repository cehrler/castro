#include <iostream>
#include <list>
#include <set>
#include <algorithm>

static int PSPECTRUM = 2;
static double THRESHOLD = 1;

using namespace std;

double spectrumKernel(const string& word1, const string& word2) {
	set<string> set1 = set<string>();
	set<string> set2 = set<string>();
	
	int length = word1.size()-PSPECTRUM;
	for(int i = 0; i <= length; ++i) {
		set1.insert(word1.substr(i,PSPECTRUM));
	}
	
	length = word2.size()-PSPECTRUM;
	for(int i = 0; i <= length; ++i) {
		set2.insert(word2.substr(i,PSPECTRUM));
	}
	
	int result = 0;
	
	set<string>::const_iterator first1 = set1.begin();
	set<string>::const_iterator last1 = set1.end();
	
	set<string>::const_iterator first2 = set2.begin();
	set<string>::const_iterator last2 = set2.end();
	
	while (first1!=last1 && first2!=last2)
	{
		if(*first1<*first2){
			++first1;
		}
		else if(*first2<*first1) {
			++first2;
		}
		else {
			result++;
			first1++;
			first2++;
		}
	}
	return double(result) / double((min(set1.size(), set2.size())));
}

int main (int argc, char * const argv[]) {
	string input_line;
	string entity;
	int delim;
	int id;
	
	list<string> entities = list<string>();
	
    while(cin) {		
		getline(cin, input_line);
		delim = input_line.find_first_of(" ");
		id = atoi(input_line.substr(0, delim).c_str());
		entity = input_line.substr(delim+1);
		
		entities.push_back(entity);
	}
	
	list<list<string> > spectrums = list<list<string> >();
	
	list<string> current;
	
	for(list<string>::iterator it = entities.begin(); it != entities.end(); ++it) {
		current = list<string>();
		
		for(int i = 0; i <= it->length()-PSPECTRUM; ++i) {
			current.push_back(it->substr(i,PSPECTRUM));
		}
		
		//sort(current.begin(), current.end());
		
		spectrums.push_back(current);
	}

	double result;
	int jj = 0;
	int ii = 0;
	double count = 0;
	double total = spectrums.size();
	
	list<int> idx = list<int>();
	list<int> idy = list<int>();
	list<double> sim = list<double>();

	list<string>::const_iterator first1;
	list<string>::const_iterator last1;
		
	list<string>::const_iterator first2;
	list<string>::const_iterator last2;
	
	for(list<list<string> >::const_iterator  i = spectrums.begin(); i != spectrums.end(); ++i) {
		jj = ii;
		cout << "done " << count/total*100. << "\n";
		for(list<list<string> >::const_iterator j = list<list<string> >::const_iterator(i); j != spectrums.end(); ++j) {
			
			result = 0.0;
			
			first1 = i->begin();
			last1 = i->end();
			first2 = j->begin();
			last2 = j->end();
			
			while (first1!=last1 && first2!=last2)
			{
				if(*first1<*first2){
					++first1;
				}
				else if(*first2<*first1) {
					++first2;
				}
				else {
					result++;
					first1++;
					first2++;
				}
			}
			if(result >= THRESHOLD) {
				idx.push_back(ii);
				idy.push_back(jj);
				sim.push_back(result);
			}
			jj++;
		}
		ii++;
		count++;
	}
	
	list<int>::const_iterator l1 = idx.begin();
	list<int>::const_iterator l2 = idy.begin();
	list<double>::const_iterator l3 = sim.begin();
	
	while(l1 != idx.end()) {
		cout << *l1 << " " << *l2 << " " << *l3 << "\n";
		l1++;
		l2++;
		l3++;
	}
		
	
    return 0;
}



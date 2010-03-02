#include <iostream>
#include <fstream>
#include <vector>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

using namespace std;


double StrToDouble(const string &s)
{
	double ret = 0;
	bool dot = false;
	size_t pom = 0;
	for (size_t i = 0; i < s.length(); i++)
	{
		if (s[i] == '.') { dot = true; }
		else
		if (dot == false)
		{
			ret = ret * 10;
			ret = ret + s[i] - '0';
		}
		else
		{
			pom++;
			double pom2 = 1;
			for (size_t j = 0; j < pom; j++) { pom2 = pom2 / 10; }
			ret = ret + pom2 * (s[i] - '0');

		}
	}

	return ret;
}

int main(int argc, char** argv)
{
	ifstream ifs(argv[1]);
	ofstream ofs(argv[2], ios::out | ios::binary);
	string s;
	string s1;
	double maxVal = 0;
	vector<vector<double>* > indMat;

	if (!ifs)
	{
		cerr << "Can't open ifs\n";
		return 1;
	}

	if (!ofs)
	{
		cerr << "Can't open ofs\n";
		return 1;
	}

	while (std::getline(ifs, s))
	{
		int lastIndex = 0;

		vector<double> *vec = new vector<double>();

		for (size_t i = 0; i < s.length(); i++)
		{
			if (s[i] == ' ')
			{
				string pomS = s.substr(lastIndex, i - lastIndex);
				lastIndex = i + 1;
				vec->push_back(StrToDouble(pomS));
			}
		}

		indMat.push_back(vec);

	}

	vector<vector<double>* > simMat;

	cerr << "indMat.size" << indMat.size() << endl;

	for (size_t i = 0; i < indMat.size(); i++)
	{
		if (i % 10 == 0) cerr << "counting: " << i << endl;
		vector<double> *vec1 = indMat[i];
		vector<double> *vecSim = new vector<double>();
		for (size_t j = 0; j < indMat.size(); j++)
		{
			double sum = 0;
			vector<double> *vec2 = indMat[j];

			for (size_t k = 0; k < vec1->size(); k++)
			{
				sum += ((*vec1)[k] - (*vec2)[k]) * ((*vec1)[k] - (*vec2)[k]);
			}

			sum = sqrt(sum);
			if (sum > maxVal) maxVal = sum;
			vecSim->push_back(sum);
		}

		simMat.push_back(vecSim);
	}

	cerr << "simMat.size" << simMat.size() << endl;
	cerr << "maxVal = " << maxVal << endl;
	int simMatSize = simMat.size();
	ofs.write( (char*)(&simMatSize), sizeof(int));
	for (size_t i = 0; i < simMat.size(); i++)
	{
		for (size_t j = 0; j < simMat[i]->size(); j++)
		{
			if (j != 0) cout << " ";
			ofs.write( (char*)(&((*(simMat[i]))[j])), sizeof(double) );
		}
	}
	ofs.close();
	return 0;
}

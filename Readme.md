Contains the solutions for two problems:

1) Producer Consumer Problem.txt -> Has the explanation and details about the producer consumer problem described below,

2) The Programming exercise has the following components:

DayEntity -> class the holds the data for a particular day, counts the hits for sites as key value pair (siteid, count)
			 
LogParser -> Main class that parses a given log file, contains the data as key value pair (day, list of sites with count)
ValueSorter -> Sorts a given map based on value 

Space complexity analysis:
N -> Number of unique websites
K -> number of days & hit counts

since N >> K, the space complexity is in the order of N - O(N)

Time complexity analysis:
N -> total number of sites 
K -> total number of days and count

total number of records in the file worst case: N*K*K => each day has N sites with K counts with a total of K days 

Parsing the data and storing them in a map: O(N*K*K)

For each day K, we are sorting the N number of sites based on their hit value which will take O(N log N) for sorting, summarizing to O(K*N log N) 

Total time complexity => O(N*K*K) + O(K*N log N) 

since K << N, neglecting K as a constant will give us => O(N) + O(N log N) => O(N log N) 


Running instructions:

compile the program as "javac FileParser.java"
run the program with the input file: "java FileParser C:/test/test.txt"



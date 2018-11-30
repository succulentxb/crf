#include <map>
#include <string>
#include <iostream>
#include <vector>

using namespace std;

void test(map<char, int> &b);

int main(int argc, char const *argv[])
{
    
    map<char, map<char, int> > a;
    map<char, int> b = {{'a', 0}, {'b', 1}};
    
    pair<char, int> *n = new pair<char, int>('a', 0);
    map<char, int> *c = new map<char, int>();
    vector<int> d;
    string m = "aaaa";
    int e[3] = {0, 0};
    d.push_back(1);
    //b.insert(make_pair('b', 100));
    
    b.insert(make_pair('b', 3));
    test(b);
    c->insert(make_pair('c', 100));
    cout << b['b'] << endl;
    int size = b.size();
    
    cout << 1 << endl;
    return 0;
}

void test(map<char, int> &b) {
    b.insert(make_pair('b', 2));
    b.insert(make_pair('b', 1));
}

#include <map>
#include <vector>
#include <unordered_map>
#include "utils.h"

using namespace std;

class Features {
private:
    vector<string> raw_templates;
    map<string, unordered_map<string, int*>> feature_funcs;
public:
    Features(vector<string> &raw_templates);
    void generate_featrues_of(const map<char, char> &tagged_seq);
    int* get_features_val(const vector<char> &sequence, const int index_of_seq);
    void train_features_val(const map<char, char> &tagged_seq);
};

Features::Features(vector<string> &raw_templates) {
    this->raw_templates = vector<string>(raw_templates);
    for (string temp : this->raw_templates) 
        this->feature_funcs.emplace(temp, new unordered_map<string, int*>());
}

/**
 * Generate features functions according to the input sequence.
 * para: sequence
 * format: [('今', 'B'), ('天', 'E'), ('是', 'S'), ('晴', 'B'), ('天', 'E')]
*/
void Features::generate_featrues_of(const map<char, char> &tagged_seq) {
    int seq_size = tagged_seq.size();
    for (int i = 0; i < seq_size; i++) {
        for (string raw_temp : this->raw_templates) {
            vector<string> temps = split(raw_temp, '/');
            for (string temp : temps) {
                
            }
        }
    }
}

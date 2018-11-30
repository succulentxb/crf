#include <map>
#include <vector>
#include <unordered_map>
#include "utils.h"

using namespace std;

/**
 * class Features, store feature functions and values
 * 
 * attributes: raw_templates
 * usage: store all the raw templates from file
 * content format: ["%x[1,0]", "%x[0,0]"]
 * 
 * attributes: feature_funcs
 * usage: use map to store feature function values, use a int array to store different tag weights
 * content format: {"%x[0,0]": {"字": int[4]}}
 * 
 * attributes: tag2index_map
 * usage: use map to construct a relation between tag and index
 * content format: {'S': 0, 'B': 1, 'I': 2, 'E': 3}
*/
class Features {
private:
    vector<string> raw_templates;
    map<string, unordered_map<string, int*>> feature_funcs;
    map<char, int> tag2index_map;
public:
    Features(const vector<string> &raw_templates, const vector<char> &tags);
    void generate_featrues_of(const vector<char> &word_seq);
    int* get_features_val(const vector<char> &word_seq, const int index_of_seq);
    void train_features_val(const vector<char> &word_seq, const vector<char> &tag_seq, const vector<char> &esti_seq);
};

Features::Features(const vector<string> &raw_templates, const vector<char> &tags) {
    this->raw_templates = vector<string>(raw_templates);
    for (string temp: this->raw_templates) {
        unordered_map<string, int*> word_map;
        this->feature_funcs.emplace(temp, word_map);
    }
        
    int index = 0;
    for (char tag: tags)
        this->tag2index_map.emplace(tag, index++);
}

/**
 * Generate features functions according to the input sequence.
 * para: sequence
 * format: [('今', 'B'), ('天', 'E'), ('是', 'S'), ('晴', 'B'), ('天', 'E')]
*/
void Features::generate_featrues_of(const vector<char> &word_seq) {
    // const auto word_seq = seq_map2vec(tagged_seq);
    int seq_size = word_seq.size();
    for (int i = 0; i < seq_size; i++) {
        for (string raw_temp: this->raw_templates) {
            vector<string> temps = split(raw_temp, '/');
            string key_val;
            for (string temp: temps) {
                int temp_index = temp2i(temp) + i;
                char dst_word;
                if (temp_index < 0 || temp_index >= seq_size)
                    dst_word = ' ';
                else
                    dst_word = word_seq[temp_index];
                key_val.push_back(dst_word);
            }
            int tag_vals[4] = {0, 0, 0, 0};
            this->feature_funcs[raw_temp].emplace(key_val, tag_vals);
        }
    }
}

void Features::train_features_val(const vector<char> &word_seq, const vector<char> &tag_seq, const vector<char> &esti_seq) {
    int seq_len = word_seq.size();
    for (int i = 0; i < seq_len; i++) {
        for (string raw_temp: this->raw_templates) {
            map<string, int*> tmp_map;
            this->feature_funcs.emplace(raw_temp, tmp_map);
            
            auto find_res = this->feature_funcs[raw_temp].find(word_seq[i]);
            auto res_end = this->feature_funcs[raw_temp].end();
            if (find_res == res_end) {
                int tag_vals[4] = {0, 0, 0, 0};
                this->feature_funcs[raw_temp];
            }
            
           //this->feature_funcs[raw_temp][word_seq]
           //unordered_map<string, int*> temp_feature = this->feature_funcs[raw_temp];
           //auto find_res = temp_feature.find(word_seq[i]);
           //auto find_end = temp_feature.end();
           //auto
        }
    }
}

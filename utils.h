#include <string>
#include <sstream>
#include <vector>
#include <iostream>
#include <map>

std::vector<std::string> split(const std::string &s, char delim) {
    std::stringstream ss(s);
    std::string item;
    std::vector<std::string> elems;
 
    while (std::getline(ss, item, delim)) {
        elems.push_back(item);
    }

    return elems;
}

/**
 * Transform template string into int. For example, %x[1,0] => 1, %x[-2,0] => -2
 * para: temp, format: "%x[-1,0]"
*/
int temp2i(const std::string &temp) {
    int str_len = temp.length();
    // temp="%x[-1,0]" => num_str="-1,0"
    std::string num_str = temp.substr(3, str_len-4);
    std::vector<std::string> nums = split(num_str, ',');
    return std::stoi(nums[0]);
}

std::map<std::string, std::vector<char>> seq_map2vec(const std::map<char, char> &tagged_seq) {
    std::vector<char> key_vec;
    std::vector<char> val_vec;
    std::map<std::string, std::vector<char>> distri_seq;
    for (const auto &pair: tagged_seq) {
        key_vec.push_back(pair.first);
        val_vec.push_back(pair.second);
    }
    distri_seq.emplace("word", key_vec);
    distri_seq.emplace("tag", val_vec);
    return distri_seq;
}

//bool map_contain()
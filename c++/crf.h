//
// Created by Succulent on 2018/11/30.
//

#ifndef CRF_CRF_H
#define CRF_CRF_H

#include <string>
#include "features.h"
#include <fstream>
#include "utils.h"

using namespace std;
void crf_train(const string &train_name, const string &labels_name, const string &template_name) {
    //char data[100];
    // read labels from file to tags vector
    vector<char> tags;
    ifstream labels_file;
    labels_file.open(labels_name);
    if (!labels_file.is_open()) {
        cerr << "Error: labels file not open." << endl;
        return;
    }
    string label;
    while (getline(labels_file, label)) {
        tags.push_back(label[0]);
    }
    labels_file.close();

    // read templates from file to raw_templates vector
    vector<string> raw_templates;
    ifstream templates_file;
    templates_file.open(template_name);
    if (!templates_file.is_open()) {
        cerr << "Error: templates file not open." << endl;
        return;
    }
    string raw_temp;
    while (getline(templates_file, raw_temp)) {
        if (raw_temp[0] != '#')
            raw_templates.push_back(raw_temp.substr(4));
    }
    templates_file.close();

    Features features(raw_templates, tags);

    // read train data from train.utf8 and generate features
    ifstream train_file(train_name);
    if (!train_file.is_open()) {
        cerr << " Error: train file not open." << endl;
        return;
    }
    while (!train_file.eof()) {
        vector<wchar_t> word_seq;
        string word_and_tag;
        getline(train_file, word_and_tag);
        while (word_and_tag != "") {
            word_seq.push_back(split(word_and_tag, ' ')[0][0]);
            getline(train_file, word_and_tag);
        }
        features.generate_featrues_of(word_seq);
    }
    train_file.close();

}
#endif //CRF_CRF_H

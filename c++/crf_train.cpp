#include <string>
#include <iostream>
//#include "crf.h"
#include <locale>

using namespace std;

int main(int argc, char const *argv[])
{
    //locale china("chs");
    wstring a = L"留心目标";
    cout << L"留心比" << endl;
    string train_file;
    string lables_file;
    string template_file;
    if (argc == 4) {
        train_file = argv[1];
        lables_file = argv[2];
        template_file = argv[3];
    }
    else if (argc == 1) {
        train_file = "train.utf8";
        lables_file = "labels.utf8";
        template_file = "template.utf8";
    }
    else {
        cerr << "error: wrong arguments." << endl;
        return 1;
    }
    //crf_train(train_file, lables_file, template_file);

    return 0;
}
